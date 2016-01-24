// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.slf4j.Logger;

import cpcc.core.entities.Parameter;
import cpcc.core.services.QueryManager;

/**
 * Setup Service implementation.
 */
public class SetupServiceImpl implements SetupService
{
    private Logger logger;
    private HibernateSessionManager sessionManager;
    private QueryManager qm;

    /**
     * @param logger the application logger.
     * @param sessionManager the database session manager.
     * @param qm the query manager.
     */
    public SetupServiceImpl(Logger logger, HibernateSessionManager sessionManager, QueryManager qm)
    {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.qm = qm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupRealVehicle()
    {
        findOrCreateParameter(Parameter.REAL_VEHICLE_NAME, 0, "RV-" + RandomStringUtils.randomAlphanumeric(8));
        findOrCreateParameter(Parameter.MASTER_SERVER_URI, 1, "http://localhost:" + RandomUtils.nextLong(12000, 13000));
        findOrCreateParameter(Parameter.USE_INTERNAL_ROS_CORE, 2, Boolean.TRUE.toString());

        sessionManager.commit();
    }

    /**
     * @param name the parameter name.
     * @param sort the new parameter sort index, if the parameter is undefined.
     * @param value the new parameter value, if the parameter is undefined.
     */
    private void findOrCreateParameter(String name, int sort, String value)
    {
        Parameter param = qm.findParameterByName(name);

        if (param == null)
        {
            param = new Parameter();
            param.setName(name);
            param.setSort(0);
        }

        if (StringUtils.isBlank(param.getValue()))
        {
            param.setValue(value);
            sessionManager.getSession().saveOrUpdate(param);
            logger.info("Automatic configuration: " + name + "=" + value);
        }
    }

}
