package at.uni_salzburg.cs.cpcc.core.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;

public class SensorDefinitionSelectHelpersTest
{
    private static final String SENSOR_DEFINOTION_DESCRIPTION = "name1";
    private SensorDefinitionSelectHelpers sut;
    private ValueEncoder<SensorDefinition> encoder;
    private SensorDefinition sensorDefinition;

    @BeforeMethod
    public void setUp()
    {
        sensorDefinition = mock(SensorDefinition.class);
        when(sensorDefinition.getDescription()).thenReturn(SENSOR_DEFINOTION_DESCRIPTION);

        QueryManager qm = Mockito.mock(QueryManager.class);
        when(qm.findSensorDefinitionByDescription(SENSOR_DEFINOTION_DESCRIPTION)).thenReturn(sensorDefinition);

        sut = new SensorDefinitionSelectHelpers(qm);
        encoder = sut.valueEncoder();
    }

    @Test
    public void shouldReturnNullForClientValueNull()
    {
        SensorDefinition actual = encoder.toValue(null);
        assertThat(actual).isNull();
    }

    @Test
    public void shouldReturnSensorDefinitionClientValueNotNull()
    {
        SensorDefinition actual = encoder.toValue(SENSOR_DEFINOTION_DESCRIPTION);
        assertThat(actual).isEqualTo(sensorDefinition);
    }

    @Test
    public void shouldReturnEmptyStringForClientValueNull()
    {
        String actual = encoder.toClient(null);
        assertThat(actual).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void shouldReturnNonEmptyStringForClientValueNotNull()
    {
        String actual = encoder.toClient(sensorDefinition);
        assertThat(actual).isEqualTo(SENSOR_DEFINOTION_DESCRIPTION);
    }

    @Test
    public void shouldCreateEmptySelectModel()
    {
        SelectModel actual = SensorDefinitionSelectHelpers.selectModel(Arrays.asList(new SensorDefinition[0]));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(0);
    }

    @Test
    public void shouldCreateSelectModelForOneSensorDefinition()
    {
        SelectModel actual = SensorDefinitionSelectHelpers.selectModel(Arrays.asList(sensorDefinition));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(1);
    }
}
