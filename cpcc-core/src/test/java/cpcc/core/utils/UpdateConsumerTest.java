// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.h2.util.Task;
import org.hibernate.Session;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Update Consumer Test implementation.
 */
public class UpdateConsumerTest
{
    private Session session;
    private UpdateConsumer<Task> sut;

    @BeforeMethod
    public void setUp()
    {
        session = mock(Session.class);

        sut = new UpdateConsumer<Task>(session);
    }

    @DataProvider
    public Object[][] taskDataProvider()
    {
        Task t1 = mock(Task.class);
        Task t2 = mock(Task.class);
        Task t3 = mock(Task.class);
        Task t4 = mock(Task.class);

        return new Object[][]{
            new Object[]{Arrays.asList(t1)},
            new Object[]{Arrays.asList(t1, t2)},
            new Object[]{Arrays.asList(t1, t2, t3)},
            new Object[]{Arrays.asList(t1, t2, t3, t4)},
        };
    }

    @Test(dataProvider = "taskDataProvider")
    public void shouldUpdateTasksInDatabase(List<Task> data)
    {
        List<Task> taskList = new ArrayList<>();
        taskList.addAll(data);

        taskList.stream().forEach(sut);

        for (Task task : taskList)
        {
            verify(session).update(task);
        }
    }
}
