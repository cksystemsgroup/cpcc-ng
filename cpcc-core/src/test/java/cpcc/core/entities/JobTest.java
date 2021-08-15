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

package cpcc.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JobTest
{
    private String resultText;

    static Stream<Arguments> jobDataProvider()
    {
        return Stream.of(
            arguments(1, new Date(10000001L), new Date(10000002L), new Date(10000003L), new Date(10000004L),
                JobStatus.CREATED, "parm1", new byte[]{1, 2, 3, 4}, "queue1"),
            arguments(1, new Date(20000001L), new Date(20000002L), new Date(20000003L), new Date(20000004L),
                JobStatus.FAILED, "parm2", new byte[]{5, 6, 7, 8}, "queue2"),
            arguments(1, new Date(30000001L), new Date(30000002L), new Date(30000003L), new Date(30000004L),
                JobStatus.QUEUED, "parm3", new byte[]{2, 4, 6, 8}, "queue3"));
    }

    @ParameterizedTest
    @MethodSource("jobDataProvider")
    void should(Integer id, Date created, Date queued, Date start, Date end, JobStatus status,
        String parameters, byte[] data, String queueName)
    {
        Job sut = new Job();

        sut.setId(id);
        sut.setCreated(created);
        sut.setQueued(queued);
        sut.setStart(start);
        sut.setEnd(end);
        sut.setStatus(status);
        sut.setParameters(parameters);
        sut.setData(data);
        sut.setQueueName(queueName);
        sut.setResultText(resultText);

        assertThat(sut.getId()).describedAs("id").isEqualTo(id);
        assertThat(sut.getCreated()).describedAs("created").isEqualTo(created);
        assertThat(sut.getQueued()).describedAs("queued").isEqualTo(queued);
        assertThat(sut.getStart()).describedAs("start").isEqualTo(start);
        assertThat(sut.getEnd()).describedAs("end").isEqualTo(end);
        assertThat(sut.getStatus()).describedAs("status").isEqualTo(status);
        assertThat(sut.getParameters()).describedAs("parameters").isEqualTo(parameters);
        assertThat(sut.getData()).describedAs("data").isEqualTo(data);
        assertThat(sut.getQueueName()).describedAs("queueName").isEqualTo(queueName);
        assertThat(sut.getResultText()).describedAs("resultText").isEqualTo(resultText);
    }
}
