/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2013  Clemens Krainer
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.javascript.preemption;

import org.junit.Test;


public class PreemptionTestCase
{

    @Test
    public void testCase01() throws InterruptedException
    {

        String js = "function VV() { "
            + "println(\"hello world!\"); "
            + "    for (var x=0; x < 10; ++x) { "
            + "        println(\"Iteration: \" + x); "
            + "        for (var t=0; t < 10000; ++t) { continue; } "
            + "        sleep(1000); "
            + "    }"
            + "}";

        JScriptRunner runner = new JScriptRunner();
        runner.setJs(js);

        runner.start();

        Thread.sleep(5000);
        runner.interrupt();

        System.out.println("before preempt");
        runner.preempt();
        System.out.println("after preempt");

        runner.join();
    }

}
