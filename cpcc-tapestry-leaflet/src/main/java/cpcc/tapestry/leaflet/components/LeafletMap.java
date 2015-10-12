package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet map component.
 */
@Import(stylesheet = {"leaflet.css", "leaflet.draw.css"})
public class LeafletMap
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources componentResources;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "")
    private String cssClass;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String height;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean drawControl;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "[0,0]")
    private String center;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "13")
    private Integer zoom;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String iconBaseUrl;

    @Inject
    @Path("images/dummyAsset.txt")
    private Asset dummyAsset;

    void afterRender()
    {
        if (iconBaseUrl == null)
        {
            iconBaseUrl = dummyAsset.toClientURL().replace("dummyAsset.txt", "");
        }

        JSONObject params = new JSONObject();
        params.put("height", height);
        params.put("options", new JSONObject("drawControl", drawControl));
        params.put("center", new JSONArray(center));
        params.put("zoom", zoom);
        params.put("iconBaseUrl", iconBaseUrl);

        javaScriptSupport.require("leaflet/map").with(componentResources.getId(), params);
    }
}
