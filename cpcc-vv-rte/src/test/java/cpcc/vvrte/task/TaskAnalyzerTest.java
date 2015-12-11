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

package cpcc.vvrte.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.QueryManager;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.task.TaskAnalyzerImpl;

/**
 * TaskAnalyzerTest
 */
public class TaskAnalyzerTest
{
    private static final int SENSOR_ID = 1;
    private static final String SENSOR_DESCRIPTION = "camera";
    private static final String SENSOR_MESSAGE_TYPE = "sensor_msgs/Image";
    private static final SensorType SENSOR_TYPE = SensorType.CAMERA;
    private static final SensorVisibility SENSOR_VISIBILITY = SensorVisibility.ALL_VV;
    private static final String MIN_TOLERANCE_DIST = "3.0";

    private QueryManager qm;
    private TaskAnalyzerImpl analyzer;
    private NativeObject taskParameters;
    private NativeObject position;
    private NativeObject sensor;
    private NativeArray sensors;

    @BeforeMethod
    public void setUp()
    {
        SensorDefinition sensorDefinition = mock(SensorDefinition.class);
        when(sensorDefinition.getId()).thenReturn(SENSOR_ID);
        when(sensorDefinition.getDescription()).thenReturn(SENSOR_DESCRIPTION);
        when(sensorDefinition.getMessageType()).thenReturn(SENSOR_MESSAGE_TYPE);
        when(sensorDefinition.getType()).thenReturn(SENSOR_TYPE);
        when(sensorDefinition.getVisibility()).thenReturn(SENSOR_VISIBILITY);

        qm = mock(QueryManager.class);
        when(qm.findSensorDefinitionByDescription(anyString())).thenReturn(sensorDefinition);

        analyzer = new TaskAnalyzerImpl(MIN_TOLERANCE_DIST, qm);

        position = new NativeObject();
        position.put("lat", position, Double.valueOf(47.2));
        position.put("lng", position, Double.valueOf(13.8));
        position.put("alt", position, Double.valueOf(50.0));

        sensor = new NativeObject();
        sensor.put("id", sensor, SENSOR_ID);
        sensor.put("description", sensor, SENSOR_DESCRIPTION);
        sensor.put("messageType", sensor, SENSOR_MESSAGE_TYPE);
        sensor.put("type", sensor, SENSOR_TYPE);
        sensor.put("visibility", sensor, SENSOR_VISIBILITY);

        sensors = new NativeArray(new NativeObject[]{sensor});

        taskParameters = new NativeObject();
        taskParameters.put("type", taskParameters, "unknownTask");
        taskParameters.put("tolerance", taskParameters, Integer.valueOf(10));
        taskParameters.put("position", taskParameters, position);
        taskParameters.put("sensors", taskParameters, sensors);
    }

    @Test
    public void shouldAnalyzePointTask()
    {
        taskParameters.put("type", taskParameters, "point");
        Task task = analyzer.analyzeTaskParameters(taskParameters, 0);

        assertThat(task).isNotNull();
        assertThat(task.getCreationTime().getTime()).isGreaterThan(System.currentTimeMillis() - 10000);
        assertThat(task.getTolerance()).isEqualTo(10.0, offset(1E-9));
        assertThat(task.getPosition().getLatitude()).isEqualTo((Double) position.get("lat"));
        assertThat(task.getPosition().getLongitude()).isEqualTo((Double) position.get("lng"));
        assertThat(task.getPosition().getAltitude()).isEqualTo((Double) position.get("alt"));

        assertThat(task.getSensors()).isNotNull().hasSize(1);
        assertThat(task.getSensors().get(0)).isNotNull();
        assertThat(task.getSensors().get(0).getId()).isNotNull()
            .isEqualTo((Integer) sensor.get("id"));
        assertThat(task.getSensors().get(0).getDescription()).isNotNull()
            .isEqualTo((String) sensor.get("description"));
        assertThat(task.getSensors().get(0).getMessageType()).isNotNull()
            .isEqualTo((String) sensor.get("messageType"));
        assertThat(task.getSensors().get(0).getType()).isNotNull()
            .isEqualTo((SensorType) sensor.get("type"));
        assertThat(task.getSensors().get(0).getVisibility()).isNotNull()
            .isEqualTo((SensorVisibility) sensor.get("visibility"));
    }

    @Test
    public void shouldNotAnalyzeUnknownTask()
    {
        Task task = analyzer.analyzeTaskParameters(taskParameters, 0);
        assertThat(task).isNull();
    }

    @Test
    public void shouldLimitTheToleranceDistance()
    {
        taskParameters.put("type", taskParameters, "point");
        taskParameters.put("tolerance", taskParameters, Double.valueOf(2.0));

        Task task = analyzer.analyzeTaskParameters(taskParameters, 0);

        assertThat(task).isNotNull();
        assertThat(task.getTolerance()).isEqualTo(Double.valueOf(MIN_TOLERANCE_DIST), offset(1E-9));
    }

    @Test
    public void shouldIgnoreNullSensors()
    {
        taskParameters.put("type", taskParameters, "point");
        sensors.put(0, sensors, null);

        Task task = analyzer.analyzeTaskParameters(taskParameters, 0);

        assertThat(task).isNotNull();

        assertThat(task.getSensors()).isEmpty();
    }
}
