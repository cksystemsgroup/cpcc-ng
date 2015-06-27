package at.uni_salzburg.cs.cpcc.core.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.DeviceType;

public class DeviceTypeSelectHelpersTest
{
    private static final String DEVICE_TYPE_NAME = "name1";
    private DeviceTypeSelectHelpers sut;
    private ValueEncoder<DeviceType> encoder;
    private DeviceType deviceType;

    @BeforeMethod
    public void setUp()
    {
        deviceType = mock(DeviceType.class);
        when(deviceType.getName()).thenReturn(DEVICE_TYPE_NAME);

        QueryManager qm = Mockito.mock(QueryManager.class);
        when(qm.findDeviceTypeByName(DEVICE_TYPE_NAME)).thenReturn(deviceType);

        sut = new DeviceTypeSelectHelpers(qm);
        encoder = sut.valueEncoder();
    }

    @Test
    public void shouldReturnNullForClientValueNull()
    {
        DeviceType actual = encoder.toValue(null);
        assertThat(actual).isNull();
    }

    @Test
    public void shouldReturnDeviceTypeClientValueNotNull()
    {
        DeviceType actual = encoder.toValue(DEVICE_TYPE_NAME);
        assertThat(actual).isEqualTo(deviceType);
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
        String actual = encoder.toClient(deviceType);
        assertThat(actual).isEqualTo(DEVICE_TYPE_NAME);
    }

    @Test
    public void shouldCreateEmptySelectModel()
    {
        SelectModel actual = DeviceTypeSelectHelpers.selectModel(Arrays.asList(new DeviceType[0]));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(0);
    }

    @Test
    public void shouldCreateSelectModelForOneDeviceType()
    {
        SelectModel actual = DeviceTypeSelectHelpers.selectModel(Arrays.asList(deviceType));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(1);
    }
}
