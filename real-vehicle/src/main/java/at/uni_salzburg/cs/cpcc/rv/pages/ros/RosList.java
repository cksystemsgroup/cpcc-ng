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
package at.uni_salzburg.cs.cpcc.rv.pages.ros;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import at.uni_salzburg.cs.cpcc.ros.services.RosQueryService;
import at.uni_salzburg.cs.cpcc.ros.services.RosTopicState;

/**
 * About
 */
public class RosList
{
    @Inject
    private RosQueryService rosQueryService;

    @Property
    private RosTopicState topicState;
    
    /**
     * @return a 
     * @throws URISyntaxException thrown in case of errors
     */
    @Cached
    public Iterable<RosTopicState> getTopicList() throws URISyntaxException
    {
        URI uri = new URI("http://localhost:11311/");
        
        return rosQueryService.findRegisteredTopics(uri);
    }
    
    @OnEvent("delete")
    void deleteTopic(String name)
    {
//        personService.deleteById(id);
        System.out.println("delete " + name);
    }

}
