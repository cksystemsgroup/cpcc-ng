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

package at.uni_salzburg.cs.cpcc.core.services;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import org.slf4j.Logger;

/**
 * LiquibaseServiceImpl
 */
public class LiquibaseServiceImpl implements LiquibaseService
{
    // private static final String CREATE_TIME_TO_DATE_ALIAS = "CREATE ALIAS IF NOT EXISTS TimeToDate AS "
    //     + "$$ java.util.Date TimeToDate(long t) { return new java.util.Date(t); } $$;";

    private final Logger logger;

    /**
     * @param logger the system logger.
     */
    public LiquibaseServiceImpl(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update()
    {
        try
        {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/LIQUIBASE");
            try (Connection connection = dataSource.getConnection())
            {
                JdbcConnection jdbcConnection = new JdbcConnection(connection);
                // setupMigration(jdbcConnection);
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
                ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();

                Liquibase l = new Liquibase("dbchange/update.xml", resourceAccessor, database);
                l.update("");
            }
        }
        catch (LiquibaseException | NamingException | SQLException e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    //    /**
    //     * @param jdbcConnection the JDBC connection.
    //     * @throws SQLException in case of errors.
    //     * @throws DatabaseException in case of errors.
    //     */
    //    private void setupMigration(JdbcConnection jdbcConnection) throws SQLException, DatabaseException
    //    {
    //        Statement statement = jdbcConnection.createStatement();
    //        statement.executeUpdate(CREATE_TIME_TO_DATE_ALIAS);
    //    }
}
