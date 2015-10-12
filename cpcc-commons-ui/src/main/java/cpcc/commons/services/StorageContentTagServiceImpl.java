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

package cpcc.commons.services;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.RequestGlobals;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.ScriptableObject;

import cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * StorageContentTagServiceImpl
 */
public class StorageContentTagServiceImpl implements StorageContentTagService
{
    private static final String STORAGE_CONTENT_TAG_ALT = "storage.content.alt";
    private static final String STORAGE_CONTENT_TAG_TITLE = "storage.content.title";
    private static final String DATE_FORMAT = "dateFormat";

    private Map<String, StorageContentTagService> serviceMap = new HashMap<String, StorageContentTagService>();
    private StorageContentTagService defaultService;

    /**
     * @param requestGlobals the request globals.
     * @param messages the messages service.
     * @param contextPath the context path.
     */
    public StorageContentTagServiceImpl(RequestGlobals requestGlobals, Messages messages
        , @Symbol(SymbolConstants.CONTEXT_PATH) String contextPath)
    {
        defaultService = new JSONContentTagService(contextPath, messages);
        serviceMap.put("sensor_msgs/Image", new ImageContentTagService(contextPath, messages));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStorageContentTag(VirtualVehicleStorage item)
    {
        ScriptableObject obj = item.getContent();
        String messageType = (String) obj.get("messageType", obj);
        StorageContentTagService converter = serviceMap.get(messageType);

        if (converter == null)
        {
            return defaultService.getStorageContentTag(item);
        }

        return converter.getStorageContentTag(item);
    }

    /**
     * ImageContentTagService
     */
    private static class ImageContentTagService implements StorageContentTagService
    {
        private static final String PAGE = "commons/vehicle/storageImage";

        private static final String TAG = "<a id=\"link_%4$d\" href=\"#\">"
            + "  <img src=\"%1$s/%2$s/%3$d/%4$d\" width=\"%5$d\" height=\"%6$d\" alt=\"%7$s\" title=\"%8$s\" />"
            + "</a>"
            + "<script>$('#link_%4$d').click(function(){"
            + "document.getElementById('dialogImage').src='%1$s/%2$s/%3$d/%4$d';"
            + "$('#dialog').dialog('option', { "
            + "  title: '%8$s', width: 'auto', height: 'auto', position: { my: 'center', at: 'center', of: window } }"
            + ").dialog('open'); return false;});</script>";

        private String contextPath;
        private Messages messages;

        /**
         * @param requestGlobals the request globals.
         * @param messages the messages service.
         */
        public ImageContentTagService(String contextPath, Messages messages)
        {
            this.contextPath = contextPath;
            this.messages = messages;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getStorageContentTag(VirtualVehicleStorage item)
        {
            // ScriptableObject obj = item.getContent();
            // int realHeight = (int) obj.get("height", obj);
            // int realWidth = (int) obj.get("width", obj);
            int height = 60;
            int width = 80;

            String alt = messages.get(STORAGE_CONTENT_TAG_ALT);
            String title = String.format(
                messages.get(STORAGE_CONTENT_TAG_TITLE),
                item.getVirtualVehicle().getName(),
                item.getName(),
                (new SimpleDateFormat(messages.get(DATE_FORMAT))).format(item.getModificationTime())
                );

            long time = item.getModificationTime().getTime();

            return String.format(TAG, contextPath, PAGE, time, item.getId(), width, height, alt, title);
        }
    }

    /**
     * JSONContentTagService
     */
    private static class JSONContentTagService implements StorageContentTagService
    {
        /**
         * @param requestGlobals the request globals.
         * @param messages the messages service.
         */
        public JSONContentTagService(String contextPath, Messages messages)
        {
            // intentionally empty.
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getStorageContentTag(VirtualVehicleStorage item)
        {
            ScriptableObject obj = item.getContent();

            String json = "";
            try
            {
                Context cx = Context.enter();
                cx.setOptimizationLevel(-1);
                ScriptableObject scope = cx.initStandardObjects();
                json = (String) NativeJSON.stringify(cx, scope, obj, null, null);
            }
            finally
            {
                Context.exit();
            }
            
            return StringEscapeUtils.escapeHtml4(json);
        }
    }
}
