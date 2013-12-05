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
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.hibernate.util.SerializationHelper;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.persistence.entities.Parameter;
import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleStorage;

public class VirtualVehicleMigratorTest
{
    private static final int VV_ID1 = 123456;
    private static final int VV_ID2 = 654321;

    private VvRteRepository repo;
    private VirtualVehicleMigrator migrator;
    private QueryManager qm;
    private Parameter paramChunkSize;

    @BeforeMethod
    public void setUp()
    {
        paramChunkSize = mock(Parameter.class);

        qm = mock(QueryManager.class);
        when(qm.findParameterByName(eq(Parameter.VIRTUAL_VEHICLE_MIGRATION_CHUNK_SIZE), anyString()))
            .thenReturn(paramChunkSize);

        repo = mock(VvRteRepository.class);

        when(repo.getQueryManager()).thenReturn(qm);

        setUpVv1();
        setUpVv2();

        migrator = new VirtualVehicleMigratorImpl(repo);
        assertThat(migrator).isNotNull();
    }

    public void setUpVv1()
    {
        Date startTime = new Date();
        Date modificationTime1 = new Date(startTime.getTime() + 100);
        Date modificationTime2 = new Date(startTime.getTime() + 200);
        Date modificationTime3 = new Date(startTime.getTime() + 300);
        Date modificationTime4 = new Date(startTime.getTime() + 400);
        Date endTime = new Date(startTime.getTime() + 1000);

        VirtualVehicle vv1 = mock(VirtualVehicle.class);
        when(vv1.getId()).thenReturn(VV_ID1);
        when(vv1.getUuid()).thenReturn("efc6ef21-6d90-4f4b-95cf-baf5a9000467");
        when(vv1.getName()).thenReturn("rv23");
        when(vv1.getApiVersion()).thenReturn(1);
        when(vv1.getCode()).thenReturn("the code");
        when(vv1.getState()).thenReturn(VirtualVehicleState.FINISHED);
        when(vv1.getContinuation()).thenReturn(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        when(vv1.getStartTime()).thenReturn(startTime);
        when(vv1.getEndTime()).thenReturn(endTime);

        ScriptableObject scriptableObject1 = new NativeObject();
        scriptableObject1.put("a", scriptableObject1, "a");
        scriptableObject1.put("b", scriptableObject1, null);
        scriptableObject1.put("c", scriptableObject1, Double.valueOf(3));
        scriptableObject1.put("d", scriptableObject1, Boolean.TRUE);
        scriptableObject1.put("e", scriptableObject1, Integer.valueOf(13));

        VirtualVehicleStorage storage1 = mock(VirtualVehicleStorage.class);
        when(storage1.getId()).thenReturn(1789);
        when(storage1.getVirtualVehicle()).thenReturn(vv1);
        when(storage1.getModificationTime()).thenReturn(modificationTime1);
        when(storage1.getName()).thenReturn("storage1");
        when(storage1.getContent()).thenReturn(scriptableObject1);
        when(storage1.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject1));

        ScriptableObject scriptableObject2 = new NativeObject();
        scriptableObject2.put("A", scriptableObject2, "A");
        scriptableObject2.put("B", scriptableObject2, scriptableObject1);

        VirtualVehicleStorage storage2 = mock(VirtualVehicleStorage.class);
        when(storage2.getId()).thenReturn(2789);
        when(storage2.getVirtualVehicle()).thenReturn(vv1);
        when(storage2.getModificationTime()).thenReturn(modificationTime2);
        when(storage2.getName()).thenReturn("storage2");
        when(storage2.getContent()).thenReturn(scriptableObject2);
        when(storage2.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject2));

        ScriptableObject scriptableObject3 = new NativeObject();
        scriptableObject3.put("f", scriptableObject3, "FFF");
        scriptableObject3.put("g", scriptableObject3, "GGGGG");

        VirtualVehicleStorage storage3 = mock(VirtualVehicleStorage.class);
        when(storage3.getId()).thenReturn(3789);
        when(storage3.getVirtualVehicle()).thenReturn(vv1);
        when(storage3.getModificationTime()).thenReturn(modificationTime3);
        when(storage3.getName()).thenReturn("storage3");
        when(storage3.getContent()).thenReturn(scriptableObject3);
        when(storage3.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject3));

        ScriptableObject scriptableObject4 = null;

        VirtualVehicleStorage storage4 = mock(VirtualVehicleStorage.class);
        when(storage4.getId()).thenReturn(4789);
        when(storage4.getVirtualVehicle()).thenReturn(vv1);
        when(storage4.getModificationTime()).thenReturn(modificationTime4);
        when(storage4.getName()).thenReturn("storage4");
        when(storage4.getContent()).thenReturn(scriptableObject4);
        when(storage4.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject4));

        when(repo.findVirtualVehicleById(vv1.getId())).thenReturn(vv1);
        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq((String) null), eq(1)))
            .thenReturn(Arrays.asList(storage1));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq("storage1"), eq(1)))
            .thenReturn(Arrays.asList(storage2));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq("storage2"), eq(1)))
            .thenReturn(Arrays.asList(storage3));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq("storage3"), eq(1)))
            .thenReturn(Arrays.asList(storage4));

        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq((String) null), eq(2)))
            .thenReturn(Arrays.asList(storage1, storage2));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq("storage2"), eq(2)))
            .thenReturn(Arrays.asList(storage3, storage4));

        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq((String) null), eq(3)))
            .thenReturn(Arrays.asList(storage1, storage2, storage3));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq("storage3"), eq(3)))
            .thenReturn(Arrays.asList(storage4));

        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq((String) null), eq(4)))
            .thenReturn(Arrays.asList(storage1, storage2, storage3, storage4));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv1.getId()), eq("storage4"), eq(4)))
            .thenReturn(new ArrayList<VirtualVehicleStorage>());
    }

    public void setUpVv2()
    {
        Date startTime = new Date();
        Date modificationTime1 = new Date(startTime.getTime() + 100);
        Date modificationTime2 = new Date(startTime.getTime() + 200);
        Date modificationTime3 = new Date(startTime.getTime() + 300);
        Date modificationTime4 = new Date(startTime.getTime() + 400);
        Date endTime = new Date(startTime.getTime() + 1000);

        VirtualVehicle vv2 = mock(VirtualVehicle.class);
        when(vv2.getId()).thenReturn(VV_ID2);
        when(vv2.getUuid()).thenReturn("fec6ef21-6d90-4f4b-95cf-baf5a9000764");
        when(vv2.getName()).thenReturn("rv32");
        when(vv2.getApiVersion()).thenReturn(1);
        when(vv2.getCode()).thenReturn("the 2nd code");
        when(vv2.getState()).thenReturn(VirtualVehicleState.FINISHED);
        when(vv2.getContinuation()).thenReturn(null);
        when(vv2.getStartTime()).thenReturn(startTime);
        when(vv2.getEndTime()).thenReturn(endTime);

        ScriptableObject scriptableObject1 = new NativeObject();
        scriptableObject1.put("z", scriptableObject1, "a");
        scriptableObject1.put("y", scriptableObject1, null);
        scriptableObject1.put("x", scriptableObject1, Double.valueOf(3));
        scriptableObject1.put("w", scriptableObject1, Boolean.TRUE);
        scriptableObject1.put("v", scriptableObject1, Integer.valueOf(13));

        VirtualVehicleStorage storage1 = mock(VirtualVehicleStorage.class);
        when(storage1.getId()).thenReturn(1789);
        when(storage1.getVirtualVehicle()).thenReturn(vv2);
        when(storage1.getModificationTime()).thenReturn(modificationTime1);
        when(storage1.getName()).thenReturn("storage1");
        when(storage1.getContent()).thenReturn(scriptableObject1);
        when(storage1.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject1));

        ScriptableObject scriptableObject2 = new NativeObject();
        scriptableObject2.put("X", scriptableObject2, "Y");
        scriptableObject2.put("Y", scriptableObject2, scriptableObject1);

        VirtualVehicleStorage storage2 = mock(VirtualVehicleStorage.class);
        when(storage2.getId()).thenReturn(2789);
        when(storage2.getVirtualVehicle()).thenReturn(vv2);
        when(storage2.getModificationTime()).thenReturn(modificationTime2);
        when(storage2.getName()).thenReturn("storage2");
        when(storage2.getContent()).thenReturn(scriptableObject2);
        when(storage2.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject2));

        ScriptableObject scriptableObject3 = new NativeObject();
        scriptableObject3.put("f", scriptableObject3, "FFF");
        scriptableObject3.put("g", scriptableObject3, "GGGGG");

        VirtualVehicleStorage storage3 = mock(VirtualVehicleStorage.class);
        when(storage3.getId()).thenReturn(3789);
        when(storage3.getVirtualVehicle()).thenReturn(vv2);
        when(storage3.getModificationTime()).thenReturn(modificationTime3);
        when(storage3.getName()).thenReturn("storage3");
        when(storage3.getContent()).thenReturn(scriptableObject3);
        when(storage3.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject3));

        ScriptableObject scriptableObject4 = null;

        VirtualVehicleStorage storage4 = mock(VirtualVehicleStorage.class);
        when(storage4.getId()).thenReturn(4789);
        when(storage4.getVirtualVehicle()).thenReturn(vv2);
        when(storage4.getModificationTime()).thenReturn(modificationTime4);
        when(storage4.getName()).thenReturn("storage4");
        when(storage4.getContent()).thenReturn(scriptableObject4);
        when(storage4.getContentAsByteArray()).thenReturn(SerializationHelper.serialize(scriptableObject4));

        when(repo.findVirtualVehicleById(vv2.getId())).thenReturn(vv2);
        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq((String) null), eq(1)))
            .thenReturn(Arrays.asList(storage1));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq("storage1"), eq(1)))
            .thenReturn(Arrays.asList(storage2));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq("storage2"), eq(1)))
            .thenReturn(Arrays.asList(storage3));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq("storage3"), eq(1)))
            .thenReturn(Arrays.asList(storage4));

        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq((String) null), eq(2)))
            .thenReturn(Arrays.asList(storage1, storage2));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq("storage2"), eq(2)))
            .thenReturn(Arrays.asList(storage3, storage4));

        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq((String) null), eq(3)))
            .thenReturn(Arrays.asList(storage1, storage2, storage3));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq("storage3"), eq(3)))
            .thenReturn(Arrays.asList(storage4));

        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq((String) null), eq(4)))
            .thenReturn(Arrays.asList(storage1, storage2, storage3, storage4));
        when(repo.findStorageItemsByVirtualVehicle(eq(vv2.getId()), eq("storage4"), eq(4)))
            .thenReturn(new ArrayList<VirtualVehicleStorage>());
    }

    @DataProvider
    public Object[][] chunkDataProvider()
    {
        return new Object[][]{
            new Object[]{VV_ID1, 1, 4,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 8, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-continuation.js", 10, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 727, 1789, 0, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 792, 2789, 1, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 2, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 2, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 3, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 3, "vvrte", "cpcc"},
                    }
                }
            },
            new Object[]{VV_ID1, 2, 2,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 8, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-continuation.js", 10, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 727, 1789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 792, 2789, 0, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 1, "vvrte", "cpcc"},
                    }
                }
            },
            new Object[]{VV_ID1, 3, 2,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 8, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-continuation.js", 10, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 727, 1789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 792, 2789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 0, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 1, "vvrte", "cpcc"},
                    }
                }
            },
            new Object[]{VV_ID1, 4, 1,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 8, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-continuation.js", 10, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 727, 1789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 792, 2789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 0, "vvrte", "cpcc"},
                    }
                }
            },

            new Object[]{VV_ID2, 1, 4,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 12, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 726, 1789, 0, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 791, 2789, 1, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 2, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 2, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 3, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 3, "vvrte", "cpcc"},
                    }
                }
            },
            new Object[]{VV_ID2, 2, 2,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 12, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 726, 1789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 791, 2789, 0, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 1, "vvrte", "cpcc"},
                    }
                }
            },
            new Object[]{VV_ID2, 3, 2,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 12, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 726, 1789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 791, 2789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 0, "vvrte", "cpcc"},
                    },
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 1, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 1, "vvrte", "cpcc"},
                    }
                }
            },
            new Object[]{VV_ID2, 4, 1,
                new Object[]{
                    new Object[]{
                        new Object[]{"vv/vv.properties", 187, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"vv/vv-source.js", 12, 0, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage1.dat", 726, 1789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage2.dat", 791, 2789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage3.dat", 514, 3789, 0, "vvrte", "cpcc"},
                        new Object[]{"storage/storage4.dat", 5, 4789, 0, "vvrte", "cpcc"},
                    }
                }
            },
        };
    }

    @Test(dataProvider = "chunkDataProvider")
    public void shouldZipVirtualVehicleToByteArrayChunks(int vvId, int chunkSize, int numberOfChunks, Object[] params)
        throws IOException, ArchiveException
    {
        when(paramChunkSize.getValue()).thenReturn(Integer.toString(chunkSize));

        VirtualVehicle vv = repo.findVirtualVehicleById(vvId);
        assertThat(vv).isNotNull();

        Iterable<byte[]> chunkIterator = migrator.getChunkIterator(vv);

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.setEntryEncoding("UTF-8");

        int chunkNumber = 0;
        for (byte[] chunk : chunkIterator)
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(chunk);
            ArchiveInputStream inStream = factory.createArchiveInputStream("tar", bis);

            Object[] chunkParams = (Object[]) params[chunkNumber];

            int entryNumber = 0;
            for (TarArchiveEntry entry = readEntry(inStream); entry != null; entry = readEntry(inStream), ++entryNumber)
            {
                byte[] content = readContent(inStream);

                Object[] entryParams = (Object[]) chunkParams[entryNumber];

                String name = (String) entryParams[0];
                int length = (Integer) entryParams[1];
                int storageId = (Integer) entryParams[2];
                int chunkId = (Integer) entryParams[3];
                String userName = (String) entryParams[4];
                String groupName = (String) entryParams[5];

                assertThat(entry.getName()).isNotNull().isEqualTo(name);
                assertThat(entry.getSize()).isNotNull().isEqualTo(length);
                assertThat(entry.getUserId()).isNotNull().isEqualTo(storageId);
                assertThat(entry.getGroupId()).isNotNull().isEqualTo(chunkId);
                assertThat(entry.getUserName()).isNotNull().isEqualTo(userName);
                assertThat(entry.getGroupName()).isNotNull().isEqualTo(groupName);
                assertThat(content.length).isEqualTo(length);
            }

            inStream.close();
            bis.close();

            ++chunkNumber;
        }

        
        assertThat(chunkNumber).isEqualTo(numberOfChunks);

    }

    @Test
    private void shouldReturnNullOnNullVirtualVehicle()
    {
        Iterable<byte[]> x = migrator.getChunkIterator(null);

        assertThat(x.iterator().hasNext()).isFalse();
    }

    @Test(expectedExceptions = {IllegalStateException.class})
    private void shouldRefuseRemovingFromIterator()
    {
        Iterable<byte[]> x = migrator.getChunkIterator(null);
        x.iterator().remove();
    }
    
    @Test
    private void shouldReturnNullIfNoMoreChunksAreAvailable()
    {
        when(paramChunkSize.getValue()).thenReturn(Integer.toString(1));

        VirtualVehicle vv = repo.findVirtualVehicleById(VV_ID1);
        assertThat(vv).isNotNull();

        Iterable<byte[]> chunkIterable = migrator.getChunkIterator(vv);

        Iterator<byte[]> iterator = chunkIterable.iterator();

        while (iterator.hasNext())
        {
            iterator.next();
        }

        assertThat(iterator.hasNext()).isFalse();
        assertThat(iterator.next()).isNull();
    }

    private TarArchiveEntry readEntry(ArchiveInputStream inStream) throws IOException
    {
        TarArchiveEntry entry1 = (TarArchiveEntry) inStream.getNextEntry();
        if (entry1 == null)
        {
            return null;
        }
        System.out.printf("entry: %s, l=%d, (%d, %d), (%s/%s) %d\n",
            entry1.getName(),
            entry1.getSize(),
            entry1.getUserId(),
            entry1.getGroupId(),
            entry1.getUserName(),
            entry1.getGroupName(),
            entry1.getModTime().getTime()
            );
        return entry1;
    }

    private byte[] readContent(ArchiveInputStream inStream) throws IOException
    {
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        IOUtils.copy(inStream, content);
        content.close();
        return content.toByteArray();
    }
}
