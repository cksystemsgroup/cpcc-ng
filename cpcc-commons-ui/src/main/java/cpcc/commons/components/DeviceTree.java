// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.components;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.tree.DefaultTreeExpansionModel;
import org.apache.tapestry5.tree.DefaultTreeModel;
import org.apache.tapestry5.tree.TreeExpansionModel;
import org.apache.tapestry5.tree.TreeModel;
import org.apache.tapestry5.tree.TreeNode;

import cpcc.core.entities.ITreeNode;
import cpcc.core.entities.TreeNodeAdapter;

/**
 * DeviceTree
 */
public class DeviceTree
{
    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    private List<ITreeNode> devices;

    @Property
    private TreeNode<ITreeNode> treeNode;

    @Property
    private ITreeNode currentNode;

    @SessionState(create = false)
    private TreeExpansionModel<ITreeNode> expansionModel;

    @Property
    private List<ITreeNode> deviceList;

    void setupRender()
    {
        deviceList = devices != null ? devices : Collections.emptyList();
    }

    /**
     * @return the tree model
     */
    public TreeModel<ITreeNode> getTreeModel()
    {
        ValueEncoder<ITreeNode> encoder = new ValueEncoder<ITreeNode>()
        {
            @Override
            public String toClient(ITreeNode value)
            {
                return value.getLabel();
            }

            @Override
            public ITreeNode toValue(String clientValue)
            {
                return null;
            }
        };

        return new DefaultTreeModel<ITreeNode>(encoder, new TreeNodeAdapter(), deviceList);
    }

    /**
     * @return the expansion model.
     */
    public TreeExpansionModel<ITreeNode> getExpansionModel()
    {
        if (expansionModel == null)
        {
            expansionModel = new DefaultTreeExpansionModel<ITreeNode>();
        }

        return expansionModel;
    }

}
