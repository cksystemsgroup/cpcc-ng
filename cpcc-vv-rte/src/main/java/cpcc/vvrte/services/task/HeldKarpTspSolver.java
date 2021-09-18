// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com> and others.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation,
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

package cpcc.vvrte.services.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.entities.Task;

/**
 * A TSP solver for {@code Task} lists. This is an adapted TSP Java implementation found on Stack Overflow.
 * 
 * @see http://stackoverflow.com/questions/7159259/optimized-tsp-algorithms
 * @see http://www2.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/tsp
 */
public class HeldKarpTspSolver extends AbstractTspSolver
{
    private static final Logger LOG = LoggerFactory.getLogger(HeldKarpTspSolver.class);

    private static final long MAX_CALCULATION_TIME = 10000;

    private TimeService timeService;

    /**
     * @param timeService the time service.
     */
    public HeldKarpTspSolver(TimeService timeService)
    {
        this.timeService = timeService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> calculateBestPath(PolarCoordinate position, List<Task> path) throws TimeoutException
    {
        if (path.size() < 2)
        {
            return path;
        }

        double[][] cost = setupCostMatrix(position, path);

        Node bestNode = solve(cost);
        List<Task> r = new ArrayList<>();
        List<Integer> pathIndices = new ArrayList<>();

        for (int j = bestNode.parent[0]; j != 0; j = bestNode.parent[j])
        {
            r.add(path.get(j - 1));
            pathIndices.add(j);
        }

        if (pathIndices.isEmpty())
        {
            return path;
        }

        int firstIx = pathIndices.get(0);
        int lastIx = pathIndices.get(pathIndices.size() - 1);

        if (cost[0][firstIx] > cost[0][lastIx])
        {
            Collections.reverse(r);
        }

        return r;
    }

    /**
     * @param cost the cost matrix.
     * @return the best node.
     * @throws TimeoutException in case of timeouts.
     */
    private Node solve(double[][] cost) throws TimeoutException
    {
        long start = timeService.currentTimeMillis();

        int n = cost.length;
        double[][] costWithPi = new double[n][n];

        Node bestNode = new Node(n, costWithPi, cost);
        Node currentNode = new Node(n, costWithPi, cost);

        currentNode.computeHeldKarp(bestNode.lowerBound);

        PriorityQueue<Node> pq = new PriorityQueue<>(11, new NodeComparator());

        do
        {
            do
            {
                checkForTimeout(start);

                int i = -1;

                for (int j = 0; j < n; j++)
                {
                    if (currentNode.degree[j] > 2 && (i < 0 || currentNode.degree[j] < currentNode.degree[i]))
                    {
                        i = j;
                    }
                }

                if (i < 0)
                {
                    if (currentNode.lowerBound < bestNode.lowerBound)
                    {
                        bestNode = currentNode;
                    }
                    break;
                }

                PriorityQueue<Node> children = new PriorityQueue<>(11, new NodeComparator());

                addToChildren(n, bestNode, currentNode, i, children);

                currentNode = children.poll();
                pq.addAll(children);

            } while (currentNode.lowerBound < bestNode.lowerBound);

            currentNode = pq.poll();

        } while (currentNode != null && currentNode.lowerBound < bestNode.lowerBound);

        return bestNode;
    }

    /**
     * @param start the start time in milliseconds.
     * @throws TimeoutException in case of an time out.
     */
    private void checkForTimeout(long start) throws TimeoutException
    {
        if (timeService.currentTimeMillis() - start > MAX_CALCULATION_TIME)
        {
            throw new TimeoutException();
        }
    }

    /**
     * @param n the path length.
     * @param bestNode the best node.
     * @param currentNode the current node.
     * @param i the index.
     * @param children the queue of children.
     */
    private void addToChildren(int n, Node bestNode, Node currentNode, int i, PriorityQueue<Node> children)
    {
        children.add(currentNode.exclude(bestNode.lowerBound, i, currentNode.parent[i]));

        for (int j = 0; j < n; j++)
        {
            if (currentNode.parent[j] == i)
            {
                children.add(currentNode.exclude(bestNode.lowerBound, i, j));
            }
        }
    }

    /**
     * Node implementation.
     */
    private static class Node
    {
        private int n;
        private double[][] costWithPi;
        private double[][] cost;

        private boolean[][] excluded;

        // Held--Karp solution
        private double[] pi;
        private double lowerBound;
        private int[] degree;
        private int[] parent;
        private int chk;

        Node(int n, double[][] costWithPi, double[][] cost)
        {
            this.n = n;
            this.costWithPi = costWithPi;
            this.cost = cost;

            this.chk = 0;

            excluded = new boolean[n][n];
            pi = new double[n];
            lowerBound = Double.MAX_VALUE;
            degree = new int[n];
            parent = new int[n];
        }

        /**
         * @param bestNodelowerBound the lower bound of the best node.
         * @param i node index one.
         * @param j node index two.
         * @return
         */
        public Node exclude(double bestNodelowerBound, int i, int j)
        {
            Node child = new Node(n, costWithPi, cost);

            child.excluded = excluded.clone();
            child.excluded[i] = excluded[i].clone();
            child.excluded[j] = excluded[j].clone();
            child.excluded[i][j] = true;
            child.excluded[j][i] = true;

            child.computeHeldKarp(bestNodelowerBound);

            return child;
        }

        /**
         * @param bestNodelowerBound the lower bound of the best node.
         */
        public void computeHeldKarp(double bestNodelowerBound)
        {
            if (++chk > 1)
            {
                LOG.info("computeHeldKarp: {}", chk);
            }

            lowerBound = Double.MIN_VALUE;

            double lambda = 0.1;

            while (lambda > 1e-06)
            {
                double previousLowerBound = lowerBound;
                computeOneTree();

                if (lowerBound >= bestNodelowerBound)
                {
                    return;
                }

                if (lowerBound >= previousLowerBound)
                {
                    lambda *= 0.9;
                }

                int denom = 0;
                for (int i = 1; i < n; i++)
                {
                    int d = degree[i] - 2;
                    denom += d * d;
                }

                if (denom == 0)
                {
                    return;
                }

                double t = lambda * lowerBound / denom;
                for (int i = 1; i < n; i++)
                {
                    pi[i] += t * (degree[i] - 2);
                }
            }
        }

        /**
         * Compute one tree.
         */
        private void computeOneTree()
        {
            // compute adjusted costs
            lowerBound = 0.0;
            Arrays.fill(degree, 0);

            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    costWithPi[i][j] = excluded[i][j] ? Double.MAX_VALUE : cost[i][j] + pi[i] + pi[j];
                }
            }

            int firstNeighbor;
            int secondNeighbor;

            // find the two cheapest edges from 0
            if (costWithPi[0][2] < costWithPi[0][1])
            {
                firstNeighbor = 2;
                secondNeighbor = 1;
            }
            else
            {
                firstNeighbor = 1;
                secondNeighbor = 2;
            }

            for (int j = 3; j < n; j++)
            {
                if (costWithPi[0][j] < costWithPi[0][secondNeighbor])
                {
                    if (costWithPi[0][j] < costWithPi[0][firstNeighbor])
                    {
                        secondNeighbor = firstNeighbor;
                        firstNeighbor = j;
                    }
                    else
                    {
                        secondNeighbor = j;
                    }
                }
            }

            addEdge(0, firstNeighbor);
            Arrays.fill(parent, firstNeighbor);
            parent[firstNeighbor] = 0;

            // compute the minimum spanning tree on nodes 1..n-1
            double[] minCost = costWithPi[firstNeighbor].clone();

            computeOneTreeHelper(minCost);

            addEdge(0, secondNeighbor);
            parent[0] = secondNeighbor;
            lowerBound = Math.rint(lowerBound);
        }

        /**
         * @param minCost the minimum cost vector.
         */
        private void computeOneTreeHelper(double[] minCost)
        {
            for (int k = 2; k < n; k++)
            {
                int i;

                for (i = 1; i < n; i++)
                {
                    if (degree[i] == 0)
                    {
                        break;
                    }
                }

                for (int j = i + 1; j < n; j++)
                {
                    if (degree[j] == 0 && minCost[j] < minCost[i])
                    {
                        i = j;
                    }
                }

                addEdge(parent[i], i);

                for (int j = 1; j < n; j++)
                {
                    if (degree[j] == 0 && costWithPi[i][j] < minCost[j])
                    {
                        minCost[j] = costWithPi[i][j];
                        parent[j] = i;
                    }
                }
            }
        }

        /**
         * @param i node index one.
         * @param j node index two.
         */
        private void addEdge(int i, int j)
        {
            lowerBound += costWithPi[i][j];
            degree[i]++;
            degree[j]++;
        }
    }

    /**
     * Node Comparator implementation.
     */
    private static class NodeComparator implements Comparator<Node>, Serializable
    {
        private static final long serialVersionUID = -8066243818129778631L;

        @Override
        public int compare(Node a, Node b)
        {
            return Double.compare(a.lowerBound, b.lowerBound);
        }
    }

}