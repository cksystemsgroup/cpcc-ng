package at.uni_salzburg.cs.cpcc.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception Formatter
 */
public final class ExceptionFormatter
{
    private ExceptionFormatter()
    {
        // Intentionally empty.
    }

    /**
     * @param t the throwable to be converted.
     * @return the throwable as a {@code String}.
     */
    public static String toString(Throwable t)
    {
        StringWriter w = new StringWriter();
        PrintWriter pw = new PrintWriter(w);
        t.printStackTrace(pw);
        pw.close();
        return w.toString();
    }
}
