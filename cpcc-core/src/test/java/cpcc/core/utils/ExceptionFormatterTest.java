package cpcc.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;

import org.testng.annotations.Test;

import cpcc.core.utils.ExceptionFormatter;

public class ExceptionFormatterTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<ExceptionFormatter> cnt = ExceptionFormatter.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    public void shouldConvertExceptionsToString()
    {
        Throwable t = new Throwable("xxx");

        String actual = ExceptionFormatter.toString(t);

        String[] actualLines = actual.split("\n");

        assertThat(actualLines.length).isGreaterThan(5);
        assertThat(actualLines[0]).isEqualTo("java.lang.Throwable: xxx");
    }
}
