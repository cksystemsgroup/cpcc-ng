/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.testng.annotations.Test;

public class TarArchiveTest
{

    @Test
    public void shouldWriteTarFile() throws IOException, ArchiveException
    {
        byte[] c1 = "content1\n".getBytes();
        byte[] c2 = "content2 text\n".getBytes();
        Date t1 = new Date();
        int chunkId = 170000;
        int storageId = 1234000;
        
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        FileOutputStream fos = new FileOutputStream("bugger1.tar");
//        TarArchiveOutputStream o = new TarArchiveOutputStream(baos);
        
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.setEntryEncoding("UTF-8");
        ArchiveOutputStream outStream = factory.createArchiveOutputStream("tar", fos);
        
        TarArchiveEntry archiveEntry1 = new TarArchiveEntry("entry1");
        archiveEntry1.setModTime(t1);
        archiveEntry1.setSize(c1.length);
        archiveEntry1.setIds(storageId, chunkId);
        archiveEntry1.setNames("vvrte", "cpcc");
        outStream.putArchiveEntry(archiveEntry1);
        outStream.write(c1);
        outStream.closeArchiveEntry();

        
        TarArchiveEntry archiveEntry2 = new TarArchiveEntry("data/entry2");
        archiveEntry2.setModTime(t1.getTime()-300000);
        archiveEntry2.setSize(c2.length);
        archiveEntry2.setIds(storageId+1, chunkId);
        archiveEntry2.setNames("vvrte", "cpcc");
        outStream.putArchiveEntry(archiveEntry2);
        outStream.write(c2);
        outStream.closeArchiveEntry();
        
        outStream.close();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        FileInputStream fis = new FileInputStream("bugger1.tar");
        ArchiveInputStream inStream = factory.createArchiveInputStream("tar", fis);
        
        TarArchiveEntry entry1 = (TarArchiveEntry)inStream.getNextEntry();
        System.out.printf("entry: %s, l=%d, (%d, %d), (%s/%s) %s\n",
            entry1.getName(),
            entry1.getSize(),
            entry1.getUserId(),
            entry1.getGroupId(),
            entry1.getUserName(),
            entry1.getGroupName(),
            df.format(entry1.getModTime())
            );
        ByteArrayOutputStream b1 = new ByteArrayOutputStream();
        IOUtils.copy(inStream, b1);
        b1.close();
        
        TarArchiveEntry entry2 = (TarArchiveEntry)inStream.getNextEntry();
        System.out.printf("entry: %s, l=%d, (%d, %d), (%s/%s) %s\n",
            entry2.getName(),
            entry2.getSize(),
            entry2.getUserId(),
            entry2.getGroupId(),
            entry2.getUserName(),
            entry2.getGroupName(),
            df.format(entry2.getModTime())
            );
        ByteArrayOutputStream b2 = new ByteArrayOutputStream();
        IOUtils.copy(inStream, b2);
        b2.close();
        
        TarArchiveEntry entry3 = (TarArchiveEntry)inStream.getNextEntry();
        assertThat(entry3).isNull();
        
        inStream.close();
    }
}
