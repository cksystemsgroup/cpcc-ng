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

package cpcc.commons.pages.jobs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.test.PageTester;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cpcc.core.base.CoreConstants;
import cpcc.core.services.jobs.JobRepository;

public class JobsListCheck
{
    private static final String DB_PREFIX = "target/page_test";

    private static final String[] DB_FILE_NAMES = {DB_PREFIX + ".mv.db", DB_PREFIX + ".trace.db"};

    private static final String JDBC_URL = "jdbc:h2:file:./" + DB_PREFIX;
    private static final String JNDI_URL = "java:/comp/env/jdbc/pagetest";

    private static final String TAPESTRY_MODULES = "cpcc.tapestry.leaflet.services.LeafletModule,"
        + "cpcc.tapestry.ace.services.AceModule,"
        + "cpcc.commons.services.CommonsModule,"
        + "cpcc.core.services.CoreModule,"
        + "cpcc.com.services.CommunicationModule,"
        + "cpcc.rv.base.services.RealVehicleBaseModule,"
        + "cpcc.ros.services.RosServiceModule,"
        + "cpcc.vvrte.services.VvRteModule";

    // @BeforeMethod
    public void setUp() throws SQLException, ClassNotFoundException, NamingException
    {
        Stream.of(DB_FILE_NAMES).map(x -> new File(x)).filter(File::exists).forEach(File::delete);

        System.setProperty("tapestry.modules", TAPESTRY_MODULES);
        System.setProperty(CoreConstants.PROP_LIQUIBASE_DATABASE_URL, JNDI_URL);
        System.setProperty("hibernate.connection.datasource", JNDI_URL);

        Class.forName("org.h2.Driver");

        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenAnswer(new Answer<Connection>()
        {
            @Override
            public Connection answer(InvocationOnMock invocation) throws Throwable
            {
                System.out.println("buggerit 1!");
                return DriverManager.getConnection(JDBC_URL, "sa", "");
            }
        });

        Context initContext = new InitialContext();
        initContext.createSubcontext("java:/comp/env");
        initContext.createSubcontext("java:/comp/env/jdbc");
        initContext.bind(JNDI_URL, dataSource);
    }

    public static Predicate<Element> byTagName(String tagName)
    {
        return new Predicate<Element>()
        {
            public boolean accept(Element e)
            {
                return tagName.equals(e.getName());
            }
        };

    }

    // @Test
    public void test1()
    {
        String appPackage = "cpcc.commons";
        String appName = "CommonsTest";
        PageTester tester = new PageTester(appPackage, appName, "src/test/webapp");

        JobRepository jobRepository = tester.getRegistry().getService(JobRepository.class);
        assertThat(jobRepository).isNotNull();

        Document actual = tester.renderPage("jobs/List");

        actual.toMarkup(new PrintWriter(System.out));

        Element actualHeader = actual.getRootElement().getElement(byTagName("h4"));
        assertThat(actualHeader.toString()).isEqualTo("<h4>Job Overview</h4>");

        tester.shutdown();
    }

}
