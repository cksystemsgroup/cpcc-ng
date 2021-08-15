package cpcc.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

public class ExceptionFormatterTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<ExceptionFormatter> cnt = ExceptionFormatter.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
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
