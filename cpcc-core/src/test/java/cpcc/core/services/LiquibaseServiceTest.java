package cpcc.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import liquibase.exception.LiquibaseException;

class LiquibaseServiceTest
{
    private static final String JNDI_COMP_ENV = "java:/comp/env";
    private static final String JNDI_RESOURCE = "jdbc/DATABASE";
    private static final String JNDI_URL = JNDI_COMP_ENV + "/" + JNDI_RESOURCE;

    private static final String CHANGE_LOG = "test-dbchanges/update.xml";

    @Test
    void shouldUpdateDatabase() throws LiquibaseException, NamingException, SQLException
    {
        System.setProperty("liquibaseServiceTestShouldFail", "false");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MyInitialContextFactory.class.getName());

        LiquibaseServiceImpl sut = new LiquibaseServiceImpl(CHANGE_LOG, JNDI_URL);

        sut.update();

        assertThat(sut.getUpdateOk())
            .isNotNull()
            .isTrue();
    }

    @Test
    void shouldLogJndiErrors() throws LiquibaseException, NamingException
    {
        System.setProperty("liquibaseServiceTestShouldFail", "NamingException");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MyInitialContextFactory.class.getName());

        LiquibaseServiceImpl sut = new LiquibaseServiceImpl(CHANGE_LOG, JNDI_URL);

        sut.update();

        assertThat(sut.getUpdateOk())
            .isNotNull()
            .isFalse();
    }

    @Test
    void shouldLogSqlErrors() throws LiquibaseException, NamingException
    {
        System.setProperty("liquibaseServiceTestShouldFail", "SQLException");
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MyInitialContextFactory.class.getName());

        LiquibaseServiceImpl sut = new LiquibaseServiceImpl(CHANGE_LOG, JNDI_URL);

        sut.update();

        assertThat(sut.getUpdateOk())
            .isNotNull()
            .isFalse();
    }

    @Test
    void shouldThrowExceptionOnWrongJndiUrl()
    {
        try
        {
            new LiquibaseServiceImpl(null, "wrong://database.URL");
            failBecauseExceptionWasNotThrown(LiquibaseException.class);
        }
        catch (LiquibaseException e)
        {
            assertThat(e).hasMessage("Only JNDI URLs starting with 'java:/comp/env' are allowed!");
        }
    }

    public static class MyInitialContextFactory implements InitialContextFactory
    {
        private static final String DB_URL = "jdbc:h2:mem:liquibaseTest";

        private static Context context = null;
        private static DataSource dataSource = null;
        private static Connection connection = null;

        public MyInitialContextFactory() throws NamingException, SQLException, ClassNotFoundException
        {
            if (context != null)
            {
                return;
            }

            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, "sa", "");

            dataSource = mock(DataSource.class);
            // when(dataSource.getConnection()).thenReturn(connection);
            // when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
            when(dataSource.getConnection()).thenAnswer(new Answer<Connection>()
            {
                @Override
                public Connection answer(InvocationOnMock invocation) throws Throwable
                {
                    String fail = System.getProperty("liquibaseServiceTestShouldFail", "false");
                    if ("SQLException".equals(fail))
                    {
                        throw new SQLException("Thrown on purpose: " + fail + "!");
                    }
                    return connection;
                }
            });

            when(dataSource.getConnection(anyString(), anyString())).thenAnswer(new Answer<Connection>()
            {
                @Override
                public Connection answer(InvocationOnMock invocation) throws Throwable
                {
                    String fail = System.getProperty("liquibaseServiceTestShouldFail", "false");
                    if ("SQLException".equals(fail))
                    {
                        throw new SQLException("Thrown on purpose: " + fail + "!");
                    }
                    return connection;
                }
            });

            context = mock(Context.class);

            when(context.lookup(anyString())).thenAnswer(new Answer<Object>()
            {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable
                {
                    String queryString = invocation.getArgument(0);
                    System.out.println("String lookup " + queryString);
                    String fail = System.getProperty("liquibaseServiceTestShouldFail", "false");

                    if (queryString.matches(JNDI_COMP_ENV))
                    {
                        return context;
                    }

                    if (queryString.matches(JNDI_RESOURCE))
                    {
                        if ("NamingException".equals(fail))
                        {
                            throw new NamingException("Thrown on purpose: " + fail + "!");
                        }
                        return dataSource;
                    }

                    return null;
                }
            });
        }

        @Override
        public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException
        {
            return context;
        }

        public static DataSource getDataSourceMock()
        {
            return dataSource;
        }

        public static Connection getConnectionMock()
        {
            return connection;
        }
    }
}
