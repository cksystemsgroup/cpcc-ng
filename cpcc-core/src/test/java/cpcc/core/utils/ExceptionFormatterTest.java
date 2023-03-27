package cpcc.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class ExceptionFormatterTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<ExceptionFormatter> cnt = ExceptionFormatter.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(cnt.getModifiers())).isTrue();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    void shouldConvertExceptionsToString()
    {
        Throwable t = new Throwable("xxx");

        String actual = ExceptionFormatter.toString(t);

        String[] actualLines = actual.split("\n");

        assertThat(actualLines).hasSizeGreaterThan(5);
        assertThat(actualLines[0]).isEqualTo("java.lang.Throwable: xxx");
    }
}
