package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet tile layer component.
 */
@MixinAfter
public class LeafletTileLayer
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "[No Name]")
    private String name;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String urlTemplate;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "{}")
    private String options;

    @Inject
    private ComponentResources componentResources;

    void afterRender()
    {
        JSONObject jsonOptions = new JSONObject(options);

        javaScriptSupport
            .require("leaflet/tileLayer")
            .with(componentResources.getId(), mapId, name, urlTemplate, jsonOptions);
    }
}
