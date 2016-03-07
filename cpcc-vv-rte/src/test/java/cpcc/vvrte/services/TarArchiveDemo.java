// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.testng.annotations.Test;

public class TarArchiveDemo
{
    private static final String GROUP_NAME_ONE = "cpcc1";
    private static final String GROUP_NAME_TWO = "cpcc2";
    private static final String USER_NAME_ONE = "vvrte1";
    private static final String USER_NAME_TWO = "vvrte2";

    private static final int CHUNK_ID_ONE = 170000;
    private static final int STORAGE_ID_ONE = 1234000;
    private static final int CHUNK_ID_TWO = 1067800;
    private static final int STORAGE_ID_TWO = 484727;

    @Test
    public void shouldWriteTarFile() throws IOException, ArchiveException
    {
        byte[] c1 = "content1\n".getBytes("UTF-8");
        byte[] c2 = "content2 text\n".getBytes("UTF-8");

        Date t1 = new Date(1000L * (System.currentTimeMillis() / 1000L));
        Date t2 = new Date(t1.getTime() - 30000);

        FileOutputStream fos = new FileOutputStream("bugger1.tar");

        ArchiveStreamFactory factory = new ArchiveStreamFactory("UTF-8");
        ArchiveOutputStream outStream = factory.createArchiveOutputStream("tar", fos);

        TarArchiveEntry archiveEntry1 = new TarArchiveEntry("entry1");
        archiveEntry1.setModTime(t1);
        archiveEntry1.setSize(c1.length);
        archiveEntry1.setIds(STORAGE_ID_ONE, CHUNK_ID_ONE);
        archiveEntry1.setNames(USER_NAME_ONE, GROUP_NAME_ONE);

        outStream.putArchiveEntry(archiveEntry1);
        outStream.write(c1);
        outStream.closeArchiveEntry();

        TarArchiveEntry archiveEntry2 = new TarArchiveEntry("data/entry2");
        archiveEntry2.setModTime(t2);
        archiveEntry2.setSize(c2.length);
        archiveEntry2.setIds(STORAGE_ID_TWO, CHUNK_ID_TWO);
        archiveEntry2.setNames(USER_NAME_TWO, GROUP_NAME_TWO);

        outStream.putArchiveEntry(archiveEntry2);
        outStream.write(c2);
        outStream.closeArchiveEntry();

        outStream.close();

        FileInputStream fis = new FileInputStream("bugger1.tar");
        ArchiveInputStream inStream = factory.createArchiveInputStream("tar", fis);

        TarArchiveEntry entry1 = (TarArchiveEntry) inStream.getNextEntry();
        assertThat(entry1.getModTime()).isEqualTo(t1);
        assertThat(entry1.getSize()).isEqualTo(c1.length);
        assertThat(entry1.getLongUserId()).isEqualTo(STORAGE_ID_ONE);
        assertThat(entry1.getLongGroupId()).isEqualTo(CHUNK_ID_ONE);
        assertThat(entry1.getUserName()).isEqualTo(USER_NAME_ONE);
        assertThat(entry1.getGroupName()).isEqualTo(GROUP_NAME_ONE);
        ByteArrayOutputStream b1 = new ByteArrayOutputStream();
        IOUtils.copy(inStream, b1);
        b1.close();
        assertThat(b1.toByteArray().length).isEqualTo(c1.length);
        assertThat(b1.toByteArray()).isEqualTo(c1);

        TarArchiveEntry entry2 = (TarArchiveEntry) inStream.getNextEntry();
        assertThat(entry2.getModTime()).isEqualTo(t2);
        assertThat(entry2.getSize()).isEqualTo(c2.length);
        assertThat(entry2.getLongUserId()).isEqualTo(STORAGE_ID_TWO);
        assertThat(entry2.getLongGroupId()).isEqualTo(CHUNK_ID_TWO);
        assertThat(entry2.getUserName()).isEqualTo(USER_NAME_TWO);
        assertThat(entry2.getGroupName()).isEqualTo(GROUP_NAME_TWO);
        ByteArrayOutputStream b2 = new ByteArrayOutputStream();
        IOUtils.copy(inStream, b2);
        b2.close();
        assertThat(b2.toByteArray().length).isEqualTo(c2.length);
        assertThat(b2.toByteArray()).isEqualTo(c2);

        TarArchiveEntry entry3 = (TarArchiveEntry) inStream.getNextEntry();
        assertThat(entry3).isNull();

        inStream.close();
    }
}
