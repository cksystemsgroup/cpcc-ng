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

package cpcc.vvrte.services.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import cpcc.core.services.jobs.TimeService;
import cpcc.ros.services.RosImageConverter;
import cpcc.ros.services.RosImageConverterImpl;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * DownloadServiceTest implementation.
 */
class DownloadServiceTest
{
    private static final long CURRENT_TIME = 1458492762000L;

    private static final int VV_ONE_ID = 101;
    private static final String VV_ONE_NAME = "VV01";
    private static final String VV_ONE_UUID = "f998fff0-eeba-11e5-a4f0-fff12c93f3f5";
    private static final int VV_ONE_API_VERSION = 1;
    private static final String VV_ONE_CODE = "var x = 1;";
    private static final VirtualVehicleState VV_ONE_STATE = VirtualVehicleState.FINISHED;
    private static final String VV_ONE_STATE_INFO = "vv01 state info.";
    private static final Date VV_ONE_START_TIME = null;
    private static final String VV_ONE_START_TIME_STR = null;
    // private static final String VV_ONE_START_TIME_STR = "2014-12-13 18:05:27";
    private static final Date VV_ONE_END_TIME = new Date(1418492327321L);
    // private static final String VV_ONE_END_TIME_STR = "2014-12-13 18:38:47";

    private static final int VV_TWO_ID = 202;
    private static final String VV_TWO_NAME = null;
    private static final String VV_TWO_UUID = "87a99a28-eebd-11e5-8a68-c3a85fb544ed";
    private static final int VV_TWO_API_VERSION = 1;
    private static final String VV_TWO_CODE = "var y = 2;";
    private static final byte[] VV_TWO_CONTINUATION = "Lorem ipsum dolor sit amet".getBytes();
    private static final VirtualVehicleState VV_TWO_STATE = VirtualVehicleState.INTERRUPTED;
    private static final String VV_TWO_STATE_INFO = null;
    private static final Date VV_TWO_START_TIME = new Date(1417490327123L);
    // private static final String VV_TWO_START_TIME_STR = "2014-12-02 04:18:47";
    private static final Date VV_TWO_END_TIME = null;
    private static final String VV_TWO_END_TIME_STR = null;

    private static final String ITEM_ONE_1_NAME = "itemOne1";
    private static final Date ITEM_ONE_1_TIME = new Date(1308492320000L);
    // private static final String ITEM_ONE_1_TIME_STR = "2011-06-19 16:05:20";
    private static final byte[] ITEM_ONE_1_CONTENT = new byte[]{
        123, 34, 109, 101, 115, 115, 97, 103, 101, 84, 121, 112, 101, 34, 58, 34, 115, 101, 110, 115, 111,
        114, 95, 109, 115, 103, 115, 47, 84, 101, 109, 112, 101, 114, 97, 116, 117, 114, 101, 34, 125};

    private static final String ITEM_ONE_2_NAME = "itemOne2";
    private static final Date ITEM_ONE_2_TIME = new Date(1308492320000L);
    // private static final String ITEM_ONE_2_TIME_STR = "2011-06-19 16:05:20";
    private static final byte[] ITEM_ONE_2_CONTENT = new byte[]{
        123, 34, 109, 101, 115, 115, 97, 103, 101, 84, 121, 112, 101, 34, 58, 34, 115, 101, 110, 115, 111,
        114, 95, 109, 115, 103, 115, 47, 84, 101, 109, 112, 101, 114, 97, 116, 117, 114, 101, 34, 125};

    private VvRteRepository vvRteRepo;
    private TimeService timeService;
    private DownloadServiceImpl sut;
    private List<VirtualVehicle> allVVs;
    private VirtualVehicle vv1;
    private VirtualVehicle vv2;
    private VirtualVehicleStorage itemOne1;
    private VirtualVehicleStorage itemOne2;
    private SimpleDateFormat sdf;
    private List<VirtualVehicleStorage> storageItems1;
    private List<VirtualVehicleStorage> storageItems2;
    private RosImageConverter imageConverter;
    private ScriptableObject contentOne1;
    private ScriptableObject contentOne2;

    @BeforeEach
    void setUp() throws IOException
    {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        vv1 = mock(VirtualVehicle.class);
        when(vv1.getId()).thenReturn(VV_ONE_ID);
        when(vv1.getApiVersion()).thenReturn(VV_ONE_API_VERSION);
        when(vv1.getCode()).thenReturn(VV_ONE_CODE);
        when(vv1.getContinuation()).thenReturn(null);
        when(vv1.getEndTime()).thenReturn(VV_ONE_END_TIME);
        when(vv1.getName()).thenReturn(VV_ONE_NAME);
        when(vv1.getStartTime()).thenReturn(VV_ONE_START_TIME);
        when(vv1.getState()).thenReturn(VV_ONE_STATE);
        when(vv1.getStateInfo()).thenReturn(VV_ONE_STATE_INFO);
        when(vv1.getUuid()).thenReturn(VV_ONE_UUID);

        vv2 = mock(VirtualVehicle.class);
        when(vv2.getId()).thenReturn(VV_TWO_ID);
        when(vv2.getApiVersion()).thenReturn(VV_TWO_API_VERSION);
        when(vv2.getCode()).thenReturn(VV_TWO_CODE);
        when(vv2.getContinuation()).thenReturn(VV_TWO_CONTINUATION);
        when(vv2.getEndTime()).thenReturn(VV_TWO_END_TIME);
        when(vv2.getName()).thenReturn(VV_TWO_NAME);
        when(vv2.getStartTime()).thenReturn(VV_TWO_START_TIME);
        when(vv2.getState()).thenReturn(VV_TWO_STATE);
        when(vv2.getStateInfo()).thenReturn(VV_TWO_STATE_INFO);
        when(vv2.getUuid()).thenReturn(VV_TWO_UUID);

        contentOne1 = new NativeObject();
        contentOne1.put("messageType", contentOne1, sensor_msgs.Temperature._TYPE);

        contentOne2 = new NativeObject();
        contentOne2.put("messageType", contentOne2, sensor_msgs.Image._TYPE);
        contentOne2.put("encoding", contentOne2, "png");
        contentOne2.put("height", contentOne2, 24);
        contentOne2.put("width", contentOne2, 32);
        contentOne2.put("step", contentOne2, 0);
        contentOne2.put("data", contentOne2,
            IOUtils.toByteArray(DownloadServiceTest.class.getResourceAsStream("downloadservicetest/cpcc-test.png")));

        itemOne1 = mock(VirtualVehicleStorage.class);
        when(itemOne1.getName()).thenReturn(ITEM_ONE_1_NAME);
        when(itemOne1.getContentAsByteArray()).thenReturn(ITEM_ONE_1_CONTENT);
        when(itemOne1.getModificationTime()).thenReturn(ITEM_ONE_1_TIME);
        when(itemOne1.getContent()).thenReturn(contentOne1);

        itemOne2 = mock(VirtualVehicleStorage.class);
        when(itemOne2.getName()).thenReturn(ITEM_ONE_2_NAME);
        when(itemOne2.getContentAsByteArray()).thenReturn(ITEM_ONE_2_CONTENT);
        when(itemOne2.getModificationTime()).thenReturn(ITEM_ONE_2_TIME);
        when(itemOne2.getContent()).thenReturn(contentOne2);
        //        sensor_msgs.Image image = VirtualVehicleStorageUtils.itemToRosImageMessage(itemOne2);
        //        BufferedImage bufferedImage = new RosImageConverterImpl().messageToBufferedImage(image);

        storageItems1 = Collections.emptyList();
        storageItems2 = Arrays.asList(itemOne1, itemOne2);

        allVVs = Arrays.asList(vv1, vv2);

        vvRteRepo = mock(VvRteRepository.class);
        when(vvRteRepo.findAllVehicles()).thenReturn(allVVs);
        when(vvRteRepo.findStorageItemsByVirtualVehicle(VV_ONE_ID)).thenReturn(storageItems1);
        when(vvRteRepo.findStorageItemsByVirtualVehicle(VV_TWO_ID)).thenReturn(storageItems2);

        timeService = mock(TimeService.class);
        when(timeService.currentTimeMillis()).thenReturn(new Date(CURRENT_TIME).getTime());

        imageConverter = new RosImageConverterImpl();

        sut = new DownloadServiceImpl(vvRteRepo, timeService, imageConverter);
    }

    @Test
    void shouldGetAllVirtualVehicles() throws IOException
    {
        byte[] actual = sut.getAllVirtualVehicles();

        ByteArrayInputStream bis = new ByteArrayInputStream(actual);
        ZipInputStream zis = new ZipInputStream(bis, Charset.forName("UTF-8"));

        ZipEntry entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_ONE_UUID + "/");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_ONE_UUID + "/vv.properties");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);
        Properties actualProps = new Properties();
        actualProps.load(zis);
        assertThat(actualProps.getProperty("api-version")).isEqualTo(Integer.toString(VV_ONE_API_VERSION));
        assertThat(actualProps.getProperty("end-time")).isEqualTo(sdf.format(VV_ONE_END_TIME));
        assertThat(actualProps.getProperty("name")).isEqualTo(VV_ONE_NAME);
        assertThat(actualProps.getProperty("start-time")).isEqualTo(VV_ONE_START_TIME_STR);
        assertThat(actualProps.getProperty("state")).isEqualTo(VV_ONE_STATE.name());

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_ONE_UUID + "/code.js");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);
        assertThat(IOUtils.toString(zis)).isEqualTo(VV_ONE_CODE);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_ONE_UUID + "/state-info.txt");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);
        assertThat(IOUtils.toString(zis)).isEqualTo(VV_ONE_STATE_INFO);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_ONE_UUID + "/storage/");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_TWO_UUID + "/");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_TWO_UUID + "/vv.properties");
        actualProps = new Properties();
        actualProps.load(zis);
        assertThat(actualProps.getProperty("api-version")).isEqualTo(Integer.toString(VV_TWO_API_VERSION));
        assertThat(actualProps.getProperty("end-time")).isEqualTo(VV_TWO_END_TIME_STR);
        assertThat(actualProps.getProperty("name")).isEqualTo(VV_TWO_NAME);
        assertThat(actualProps.getProperty("start-time")).isEqualTo(sdf.format(VV_TWO_START_TIME));
        assertThat(actualProps.getProperty("state")).isEqualTo(VV_TWO_STATE.name());

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_TWO_UUID + "/code.js");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);
        assertThat(IOUtils.toString(zis)).isEqualTo(VV_TWO_CODE);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_TWO_UUID + "/continuation.dat");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);
        assertThat(IOUtils.toByteArray(zis)).isEqualTo(VV_TWO_CONTINUATION);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_TWO_UUID + "/storage/");
        assertThat(entry.getTime()).isEqualTo(CURRENT_TIME);

        entry = zis.getNextEntry();
        assertThat(entry).isNotNull();
        assertThat(entry.getName()).isEqualTo(VV_TWO_UUID + "/storage/itemOne1.json");
        assertThat(entry.getTime()).isEqualTo(ITEM_ONE_1_TIME.getTime());
        assertThat(IOUtils.toByteArray(zis)).isEqualTo(ITEM_ONE_1_CONTENT);
    }

    // @Test
    void shouldListContent() throws IOException
    {
        byte[] actual = sut.getAllVirtualVehicles();

        FileUtils.writeByteArrayToFile(new File("target/lala.zip"), actual);

        ByteArrayInputStream bis = new ByteArrayInputStream(actual);
        ZipInputStream zis = new ZipInputStream(bis, Charset.forName("UTF-8"));

        for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry())
        {
            System.out.printf("%s %5d %5d %s %s\n",
                entry.isDirectory() ? "d" : "-",
                entry.getSize(),
                entry.getCompressedSize(),
                sdf.format(entry.getTime()),
                entry.getName());

            // byte[] data = IOUtils.toByteArray(zis);
            // System.out.write(data);
            // System.out.println();
        }
    }
}
