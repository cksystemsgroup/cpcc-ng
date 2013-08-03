/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2012  Clemens Krainer
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
package at.uni_salzburg.cs.cpcc.javascript;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class UtilsTestCase
{

    @Test
    public void compactPathTest01() throws FileNotFoundException
    {
        assertEquals("", Utils.compactPath(""));
        assertEquals("", Utils.compactPath("/"));
        assertEquals("", Utils.compactPath("//"));
        assertEquals("", Utils.compactPath("///"));
        assertEquals("", Utils.compactPath("////"));
    }

    @Test
    public void compactPathTest02() throws FileNotFoundException
    {
        String expected = "name1";
        assertEquals(expected, Utils.compactPath("name1"));
        assertEquals(expected, Utils.compactPath("/name1"));
    }

    @Test
    public void compactPathTest03() throws FileNotFoundException
    {
        String expected = "name1" + File.separator + "name2";
        assertEquals(expected, Utils.compactPath("name1/name2"));
        assertEquals(expected, Utils.compactPath("name1//name2"));
        assertEquals(expected, Utils.compactPath("name1///name2"));
        assertEquals(expected, Utils.compactPath("name1////name2"));

        assertEquals(expected, Utils.compactPath("/name1/name2"));
        assertEquals(expected, Utils.compactPath("//name1//name2"));
        assertEquals(expected, Utils.compactPath("///name1///name2"));
        assertEquals(expected, Utils.compactPath("////name1////name2"));
    }

    @Test
    public void compactPathTest04() throws FileNotFoundException
    {
        String expected = "name1" + File.separator + "name2";
        assertEquals(expected, Utils.compactPath("name1\\name2"));
        assertEquals(expected, Utils.compactPath("name1\\\\name2"));
        assertEquals(expected, Utils.compactPath("name1\\\\\\name2"));
        assertEquals(expected, Utils.compactPath("name1\\\\\\\\name2"));

        assertEquals(expected, Utils.compactPath("\\name1\\name2"));
        assertEquals(expected, Utils.compactPath("\\\\name1\\\\name2"));
        assertEquals(expected, Utils.compactPath("\\\\\\name1\\\\\\name2"));
        assertEquals(expected, Utils.compactPath("\\\\\\\\name1\\\\\\\\name2"));
    }

    @Test
    public void compactPathTest05() throws FileNotFoundException
    {
        String expected = "name1" + File.separator + "name2" + File.separator + "name3";
        assertEquals(expected, Utils.compactPath("name1/name2/name3"));
        assertEquals(expected, Utils.compactPath("name1//name2//name3"));
        assertEquals(expected, Utils.compactPath("name1///name2///name3"));
        assertEquals(expected, Utils.compactPath("name1////name2////name3"));

        assertEquals(expected, Utils.compactPath("/name1/name2/name3"));
        assertEquals(expected, Utils.compactPath("//name1//name2//name3"));
        assertEquals(expected, Utils.compactPath("///name1///name2///name3"));
        assertEquals(expected, Utils.compactPath("////name1////name2////name3"));
    }

    @Test
    public void compactPathTest06() throws FileNotFoundException
    {
        String expected = "name1" + File.separator + "name2" + File.separator + "name3";
        assertEquals(expected, Utils.compactPath("name1\\name2\\name3"));
        assertEquals(expected, Utils.compactPath("name1\\\\name2\\\\name3"));
        assertEquals(expected, Utils.compactPath("name1\\\\\\name2\\\\\\name3"));
        assertEquals(expected, Utils.compactPath("name1\\\\\\\\name2\\\\\\\\name3"));

        assertEquals(expected, Utils.compactPath("\\name1\\name2\\name3"));
        assertEquals(expected, Utils.compactPath("\\\\name1\\\\name2\\\\name3"));
        assertEquals(expected, Utils.compactPath("\\\\\\name1\\\\\\name2\\\\\\name3"));
        assertEquals(expected, Utils.compactPath("\\\\\\\\name1\\\\\\\\name2\\\\\\\\name3"));
    }

    @Test
    public void compactPathTest10() throws FileNotFoundException
    {
        assertEquals("", Utils.compactPath("."));
        assertEquals("", Utils.compactPath("./."));
        assertEquals("", Utils.compactPath("././."));
        assertEquals("", Utils.compactPath("./././."));

        assertEquals("", Utils.compactPath("."));
        assertEquals("", Utils.compactPath(".\\."));
        assertEquals("", Utils.compactPath(".\\.\\."));
        assertEquals("", Utils.compactPath(".\\.\\.\\."));
    }

    @Test
    public void compactPathTest11() throws FileNotFoundException
    {
        assertEquals("", Utils.compactPath("/."));
        assertEquals("", Utils.compactPath("//./."));
        assertEquals("", Utils.compactPath("///././."));
        assertEquals("", Utils.compactPath("///./././."));

        assertEquals("", Utils.compactPath("\\."));
        assertEquals("", Utils.compactPath("\\\\.\\."));
        assertEquals("", Utils.compactPath("\\\\\\.\\.\\."));
        assertEquals("", Utils.compactPath("\\\\\\\\.\\.\\.\\."));
    }

    @Test
    public void compactPathTest20() throws FileNotFoundException
    {
        assertEquals("", Utils.compactPath("name1/.."));
        assertEquals("", Utils.compactPath("name1/name2/../.."));
        assertEquals("", Utils.compactPath("name1/name2/name3/../../.."));
        assertEquals("", Utils.compactPath("name1/name2/name3/name4/../../../.."));

        assertEquals("", Utils.compactPath("name1\\.."));
        assertEquals("", Utils.compactPath("name1\\name2\\..\\.."));
        assertEquals("", Utils.compactPath("name1\\name2\\name3\\..\\..\\.."));
        assertEquals("", Utils.compactPath("name1\\name2\\name3\\name4\\..\\..\\..\\.."));
    }

    @Test
    public void compactPathTest21() throws FileNotFoundException
    {
        assertEquals("", Utils.compactPath("name1/.."));
        assertEquals("", Utils.compactPath("name1/../name2/.."));
        assertEquals("", Utils.compactPath("name1/../name2/../name3/.."));
        assertEquals("", Utils.compactPath("name1/../name2/../name3/../name4/.."));

        assertEquals("", Utils.compactPath("name1\\.."));
        assertEquals("", Utils.compactPath("name1\\..\\name2\\.."));
        assertEquals("", Utils.compactPath("name1\\..\\name2\\..\\name3\\.."));
        assertEquals("", Utils.compactPath("name1\\..\\name2\\..\\name3\\..\\name4\\.."));
    }

    @Test
    public void compactPathTest22() throws FileNotFoundException
    {
        assertEquals("name2", Utils.compactPath("name1/../name2"));
        assertEquals("name1", Utils.compactPath("name1//name2/.."));
        assertEquals("name3", Utils.compactPath("name1/../name2/../name3/"));
        assertEquals("name1/name4", Utils.compactPath("name1//name2/../name3/../name4"));

        assertEquals("name2", Utils.compactPath("/name1/../name2/"));
        assertEquals("name1", Utils.compactPath("name1//name2/../"));
        assertEquals("name3", Utils.compactPath("name1/../name2/../name3/"));
        assertEquals("name4", Utils.compactPath("name1/../name2/../name3/../name4"));
    }

    @Test
    public void compactPathTest30()
    {
        try
        {
            Utils.compactPath("..");
        }
        catch (FileNotFoundException e)
        {
            assertEquals("Invalid path name: ..", e.getMessage());
        }
    }

    @Test
    public void compactPathTest31()
    {
        try
        {
            Utils.compactPath("../..");
        }
        catch (FileNotFoundException e)
        {
            assertEquals("Invalid path name: ../..", e.getMessage());
        }
    }

    @Test
    public void compactPathTest38()
    {
        try
        {
            Utils.compactPath("../name1");
        }
        catch (FileNotFoundException e)
        {
            assertEquals("Invalid path name: ../name1", e.getMessage());
        }
    }

    @Test
    public void compactPathTest39()
    {
        try
        {
            Utils.compactPath("name1/../../name2");
        }
        catch (FileNotFoundException e)
        {
            assertEquals("Invalid path name: name1/../../name2", e.getMessage());
        }
    }

}
