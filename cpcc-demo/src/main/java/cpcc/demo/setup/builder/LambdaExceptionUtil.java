// This code has been copied from Stack Overflow.
//
// http://stackoverflow.com/questions/27644361/how-can-i-throw-checked-exceptions-from-inside-java-8-streams

package cpcc.demo.setup.builder;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * LambdaExceptionUtil implementation.
 */
public final class LambdaExceptionUtil
{
    /**
     * @param <T> the target type.
     * @param <E> the exception.
     */
    @FunctionalInterface
    public interface Consumer_WithExceptions<T, E extends Exception>
    {
        /**
         * @param t the function parameter.
         * @throws E in case of errors.
         */
        void accept(T t) throws E;
    }

    /**
     * @param <T> the target type.
     * @param <U> the other type.
     * @param <E> the exception.
     */
    @FunctionalInterface
    public interface BiConsumer_WithExceptions<T, U, E extends Exception>
    {
        /**
         * @param t the function parameter.
         * @param u the function parameter.
         * @throws E in case of errors.
         */
        void accept(T t, U u) throws E;
    }

    /**
     * @param <T> the target type.
     * @param <R> the other type.
     * @param <E> the exception.
     */
    @FunctionalInterface
    public interface Function_WithExceptions<T, R, E extends Exception>
    {
        /**
         * @param t the function parameter.
         * @return the applied function.
         * @throws E in case of errors.
         */
        R apply(T t) throws E;
    }

    /**
     * @param <T> the target type.
     * @param <E> the exception.
     */
    @FunctionalInterface
    public interface Supplier_WithExceptions<T, E extends Exception>
    {
        /**
         * @return the supplier result.
         * @throws E in case of errors.
         */
        T get() throws E;
    }

    /**
     * @param <E> the exception.
     */
    @FunctionalInterface
    public interface Runnable_WithExceptions<E extends Exception>
    {
        /**
         * @throws E in case of errors.
         */
        void run() throws E;
    }

    private LambdaExceptionUtil()
    {
        // Intentionally empty.
    }

    /**
     * .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or
     * .forEach(rethrowConsumer(ClassNameUtil::println));
     * 
     * @param <T> the target type.
     * @param <E> the exception.
     * @param consumer the consumer.
     * @return the consumer result.
     */
    public static <T, E extends Exception> Consumer<T> rethrowConsumer(Consumer_WithExceptions<T, E> consumer)
    {
        return t -> {
            try
            {
                consumer.accept(t);
            }
            catch (Exception exception)
            {
                throwAsUnchecked(exception);
            }
        };
    }

    /**
     * @param <T> the target type.
     * @param <U> the other type.
     * @param <E> the exception.
     * @param biConsumer the bi-consumer.
     * @return the consumer result.
     */
    public static <T, U, E extends Exception> BiConsumer<T, U> rethrowBiConsumer(
        BiConsumer_WithExceptions<T, U, E> biConsumer)
    {
        return (t, u) -> {
            try
            {
                biConsumer.accept(t, u);
            }
            catch (Exception exception)
            {
                throwAsUnchecked(exception);
            }
        };
    }

    /**
     * .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName))
     * 
     * @param <T> the target type.
     * @param <R> the return type.
     * @param <E> the exception.
     * @param function the function.
     * @return the function result.
     */
    public static <T, R, E extends Exception> Function<T, R> rethrowFunction(Function_WithExceptions<T, R, E> function)
    {
        return t -> {
            try
            {
                return function.apply(t);
            }
            catch (Exception exception)
            {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * rethrowSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),
     *
     * @param <T> the target type.
     * @param <E> the exception.
     * @param function the supplier function.
     * @return the supplier result.
     */
    public static <T, E extends Exception> Supplier<T> rethrowSupplier(Supplier_WithExceptions<T, E> function)
    {
        return () -> {
            try
            {
                return function.get();
            }
            catch (Exception exception)
            {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     *
     * @param t the runnable.
     */
    @SuppressWarnings("rawtypes")
    public static void uncheck(Runnable_WithExceptions t)
    {
        try
        {
            t.run();
        }
        catch (Exception exception)
        {
            throwAsUnchecked(exception);
        }
    }

    /**
     * uncheck(() -> Class.forName("xxx"));
     *
     * @param <R> the return type.
     * @param <E> the exception.
     * @param supplier the supplier.
     * @return the supplier result.
     */
    public static <R, E extends Exception> R uncheck(Supplier_WithExceptions<R, E> supplier)
    {
        try
        {
            return supplier.get();
        }
        catch (Exception exception)
        {
            throwAsUnchecked(exception);
            return null;
        }
    }

    /**
     * uncheck(Class::forName, "xxx");
     *
     * @param <T> the target type.
     * @param <R> the return type.
     * @param <E> the exception.
     * @param function the function.
     * @param t the target.
     * @return the function result.
     */
    public static <T, R, E extends Exception> R uncheck(Function_WithExceptions<T, R, E> function, T t)
    {
        try
        {
            return function.apply(t);
        }
        catch (Exception exception)
        {
            throwAsUnchecked(exception);
            return null;
        }
    }

    /**
     * @param exception the exception.
     * @throws E in case of errors.
     */
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E
    {
        throw (E) exception;
    }

}
