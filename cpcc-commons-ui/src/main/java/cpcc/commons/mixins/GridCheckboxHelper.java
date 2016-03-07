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

package cpcc.commons.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * The Grid Checkbox Helper searches for check-boxes in the attached grid and enables the required elements each time
 * the number of checked boxes exceeds one. If the number of checked boxes is zero, the according elements are disabled.
 */
public class GridCheckboxHelper
{
    @Parameter(name = "checkBoxId", defaultPrefix = BindingConstants.LITERAL, required = true)
    private String checkBoxId;

    @Parameter(name = "enableIds", defaultPrefix = BindingConstants.LITERAL, required = true)
    private String enableIds;

    @Parameter(name = "allCheckboxColumn", defaultPrefix = BindingConstants.LITERAL, value = "-1")
    private int allCheckboxColumn;

    @Parameter(name = "allCheckboxChecked", defaultPrefix = BindingConstants.LITERAL, value = "true")
    private boolean allCheckboxChecked;

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @InjectContainer
    private Grid grid;

    void afterRender()
    {
        if (grid.getDataSource().getAvailableRows() == 0)
        {
            return;
        }

        javaScriptSupport
            .require("grid-checkbox-helper")
            .with(grid.getClientId(), checkBoxId, enableIds, allCheckboxColumn, allCheckboxChecked);
    }
}
