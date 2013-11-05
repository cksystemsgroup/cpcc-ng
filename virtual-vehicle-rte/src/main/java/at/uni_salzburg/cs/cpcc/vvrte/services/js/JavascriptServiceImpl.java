/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services.js;

import java.util.HashSet;
import java.util.Set;

import org.mozilla.javascript.ContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavascriptServiceImpl
 */
public class JavascriptServiceImpl implements JavascriptService
{
    private static final Logger LOG = LoggerFactory.getLogger(JavascriptServiceImpl.class);
    
    private Set<String> allowedClasses = new HashSet<String>();
    
    /**
     * JavascriptServiceImpl
     */
    public JavascriptServiceImpl()
    {
        try
        {
            if (!ContextFactory.hasExplicitGlobal())
            {
                ContextFactory.initGlobal(new SandboxContextFactory());
            }
        }
        catch (IllegalStateException e)
        {
            LOG.debug("ContextFactory already initialized");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsWorker execute(String script, int apiVersion)
    {
        JsWorker w = new JsWorker(script, apiVersion, allowedClasses);
        new Thread(w).start();
        return w;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JsWorker execute(byte[] continuation)
    {
        JsWorker w = new JsWorker(continuation, allowedClasses);
        new Thread(w).start();
        return w;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVvRteFunctions(BuiltInFunctions functions)
    {
        VvRteFunctions.setVvRte(functions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAllowedClass(String className)
    {
        allowedClasses.add(className);
    }
}
