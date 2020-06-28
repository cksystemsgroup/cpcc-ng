// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2017 Clemens Krainer <clemens.krainer@gmail.com>
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

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;

/**
 * MxBean utility functions.
 */
public class MxBeanUtils
{
    public static final String PATH_SEPARATOR = "/";

    private MBeanServer pmbs = ManagementFactory.getPlatformMBeanServer();

    /**
     * @param attributeName the attribute name.
     * @return the attribute value.
     * @throws JMException in case of errors.
     */
    @SuppressWarnings("rawtypes")
    public String readMxBeanAttribute(String attributeName) throws JMException
    {
        String[] x = attributeName.split("(?<!\\\\)" + PATH_SEPARATOR);

        if (x.length < 2)
        {
            throw new MalformedObjectNameException("Can not handle object " + attributeName);
        }

        x[0] = x[0].replaceAll("\\\\", "");
        ObjectName on = new ObjectName(x[0]);
        Object a = pmbs.getAttribute(on, x[1]);

        if (x.length == 3)
        {
            CompositeDataSupport cd = (CompositeDataSupport) a;
            return cd.get(x[2]).toString();
        }

        if ("[Ljava.lang.String;".equals(a.getClass().getName()))
        {
            return String.join(",", (String[]) a);
        }

        if ("javax.management.openmbean.TabularDataSupport".equals(a.getClass().getName()))
        {
            HashMap<String, String> m = new HashMap<>();
            TabularDataSupport data = (TabularDataSupport) a;
            for (Iterator iter = data.keySet().iterator(); iter.hasNext();)
            {
                Object key = iter.next();
                for (Iterator iter1 = ((List) key).iterator(); iter1.hasNext();)
                {
                    Object key1 = iter1.next();
                    CompositeData valuedata = data.get(new Object[]{key1});
                    String value = (String) valuedata.get("value");
                    value = value.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\t", "\\\\t");
                    m.put(key1.toString(), value);
                }
            }

            Object[] key = m.keySet().toArray();
            Arrays.sort(key);

            boolean first = true;
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < key.length; i++)
            {
                if (!first)
                {
                    b.append("\t");
                }
                first = false;
                b.append(key[i]).append('=').append(m.get(key[i]));
            }

            return b.toString();
        }

        return a.toString();
    }

    /**
     * @return the list of available MxBean names.
     */
    @SuppressWarnings("rawtypes")
    public List<String> listMxBeans()
    {
        ArrayList<String> beans = new ArrayList<>();

        Set mbNameSet = pmbs.queryNames(null, null);
        Iterator mbNameSetIterator = mbNameSet.iterator();
        while (mbNameSetIterator.hasNext())
        {
            Object o = mbNameSetIterator.next();
            ObjectName on = (ObjectName) o;
            try
            {
                MBeanInfo info = pmbs.getMBeanInfo(on);
                MBeanAttributeInfo[] ai = info.getAttributes();
                for (int k = 0, l = ai.length; k < l; k++)
                {
                    String s = o.toString();
                    String attributeName =
                        s.replace(PATH_SEPARATOR, "\\\\" + PATH_SEPARATOR) + PATH_SEPARATOR + ai[k].getName();
                    if ("javax.management.openmbean.CompositeData".equals(ai[k].getType()))
                    {
                        CompositeData cmp = (CompositeData) pmbs.getAttribute(on, ai[k].getName());
                        if (cmp == null)
                        {
                            continue;
                        }
                        Set keySet = cmp.getCompositeType().keySet();
                        Iterator cIterator = keySet.iterator();
                        while (cIterator.hasNext())
                        {
                            Object n = cIterator.next();
                            beans.add(attributeName + PATH_SEPARATOR + n.toString());
                        }
                    }
                    else
                    {
                        beans.add(attributeName);
                        String[] x = attributeName.split("(?<!\\\\)" + PATH_SEPARATOR);
                        x[0] = x[0].replaceAll("\\\\", "");
                    }
                }
            }
            catch (JMException e)
            {
                // intentionally empty
            }
        }

        return beans;
    }

}
