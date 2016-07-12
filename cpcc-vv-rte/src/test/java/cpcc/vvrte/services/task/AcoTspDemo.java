// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.services.jobs.TimeServiceImpl;
import cpcc.vvrte.entities.Task;

/**
 * AcoTspDemo implementation.
 */
public class AcoTspDemo extends JPanel implements ActionListener
{
    private static final Logger LOG = LoggerFactory.getLogger(AcoTspDemo.class);
    
    private static final int PATH_LENGTH = 50;
    private static final String BTN_REFRESH = "Refresh";
    private static final String BTN_AGAIN = "Again";

    private static final long serialVersionUID = 5162212166316352654L;

    public static final int DIM_X = 500;
    public static final int DIM_Y = 500;

    public static final double MIN_LAT = 47.822120373304436;
    public static final double MAX_LAT = 47.82367624317964;
    public static final double MIN_LNG = 13.037455272729183;
    public static final double MAX_LNG = 13.040131100849377;

    public static final PolarCoordinate CURRENT = new PolarCoordinate(toLat(0.2), toLng(0.5), 0.0);
    public static final PolarCoordinate DEPOT = new PolarCoordinate(toLat(0.8), toLng(0.5), 0.0);

    private static int toXpos(double longitude)
    {
        return (int) (DIM_X * (longitude - MIN_LNG) / (MAX_LNG - MIN_LNG));
    }

    private static int toYpos(double latitude)
    {
        return (int) (DIM_Y * (latitude - MIN_LAT) / (MAX_LAT - MIN_LAT));
    }

    private static double transform(double p, double minValue, double maxValue)
    {
        return minValue + (maxValue - minValue) * p;
    }

    public static double toLat(double p)
    {
        return transform(p, MIN_LAT, MAX_LAT);
    }

    public static double toLng(double p)
    {
        return transform(p, MIN_LNG, MAX_LNG);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new AcoTspDemo(false).createAndShowGUI();
            }
        });
    }

    private static List<Task> generateTaskList(int length)
    {
        List<Task> tasks = new ArrayList<>();

        for (int k = 0; k < length; ++k)
        {
            Task newTask = new Task();
            newTask.setPosition(new PolarCoordinate(toLat(Math.random()), toLng(Math.random()), 0.0));
            tasks.add(newTask);
        }

        return tasks;
    }

    private MyPanel p;
    private TspSolver solver;

    private List<Task> path = Collections.emptyList();

    private void createAndShowGUI()
    {
        System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("ACO TSP Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.pack();
        f.setVisible(true);
    }

    public AcoTspDemo(boolean useAco)
    {
        solver = useAco ? new AcoTspTasks() : new HeldKarpTspSolver(LOG, new TimeServiceImpl());

        JPanel buttonPane = new JPanel();
        Stream.of(BTN_REFRESH, BTN_AGAIN).forEach(
            x -> {
                JButton b = new JButton(x);
                b.addActionListener(this);
                buttonPane.add(b);
            });

        add(buttonPane);
        p = new MyPanel();
        add(p);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (BTN_REFRESH.equals(command))
        {
            path = generateTaskList(PATH_LENGTH);
        }

        long start = System.nanoTime();
        List<Task> tasks = solver.calculateBestPath(CURRENT, path);
        long duration = System.nanoTime() - start;

        System.out.println("Time = " + duration / 1.0E9 + ", pathLen=" + path.size() + ", taskLen=" + tasks.size());

        p.setTasks(tasks);
        p.revalidate();
        p.repaint();
    }

    static class MyPanel extends JPanel
    {
        private static final long serialVersionUID = -6810031437595593004L;

        private List<Task> tasks;
        private double pathLength = 0.0;

        public MyPanel()
        {
            setBorder(BorderFactory.createLineBorder(Color.black));
        }

        public void setTasks(List<Task> tasks)
        {
            this.tasks = tasks;
        }

        public Dimension getPreferredSize()
        {
            return new Dimension(DIM_X, DIM_Y);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            if (tasks == null || tasks.size() < 2)
            {
                return;
            }

            pathLength = 0.0;

            PolarCoordinate pos = CURRENT;
            int oldX = toXpos(pos.getLongitude());
            int oldY = toYpos(pos.getLatitude());
            g.drawOval(oldX, oldY, 8, 8);

            for (int k = 0; k < tasks.size(); ++k)
            {
                pos = tasks.get(k).getPosition();
                int newX = toXpos(pos.getLongitude());
                int newY = toYpos(pos.getLatitude());
                g.drawOval(newX, newY, 3, 3);

                drawLine(g, oldX, oldY, newX, newY);
                oldX = newX;
                oldY = newY;
            }

            pos = DEPOT;
            int newX = toXpos(pos.getLongitude());
            int newY = toYpos(pos.getLatitude());
            g.drawOval(newX, newY, 8, 8);

            System.out.printf("Pathlength = %.2f%n", pathLength);
        }

        private void drawLine(Graphics g, int oldX, int oldY, int newX, int newY)
        {
            g.drawLine(oldX, oldY, newX, newY);
            double dx = newX - oldX;
            double dy = newY - oldY;
            pathLength += Math.sqrt(dx * dx + dy * dy);
        }

    }

}
