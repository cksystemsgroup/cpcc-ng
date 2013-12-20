/*
 * @(#) FileLoader.java
 *
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
package at.uni_salzburg.cs.cpcc.core.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.utils.FileLoader;

/**
 * FileLoaderTest
 */
public class FileLoaderTest
{
    @Test
    public void shouldLoadFile() throws IOException
    {
        URL url = FileLoaderTest.class.getResource("fileLoaderData.txt");
        Assert.assertNotNull(url);
        Assert.assertNotNull(url.getFile());
        File file = new File(url.getFile());
        Assert.assertTrue(file.exists());

        String data = FileLoader.loadFileAsString(file, "UTF-8");
        Assert.assertNotNull(data);
        Assert.assertEquals(data, "testdata\n");
    }

    @Test(expectedExceptions = IOException.class)
    public void shouldFailNonExistentFile() throws IOException
    {
        File file = new File("nonExistentFile");
        Assert.assertFalse(file.exists());
        FileLoader.loadFileAsString(file, "UTF-8");
    }

    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<FileLoader> cnt = FileLoader.class.getDeclaredConstructor();
        Assert.assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }
}
