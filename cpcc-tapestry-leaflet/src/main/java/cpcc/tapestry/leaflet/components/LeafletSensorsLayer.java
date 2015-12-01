package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet sensors layer component.
 */
@MixinAfter
@Import(stylesheet = {"css/vehicle.css"})
public class LeafletSensorsLayer
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources componentResources;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String iconBaseUrl;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "[No Name]")
    private String name;

    @Parameter
    private Object[] context;

    @Inject
    @Path("images/dummyAsset.txt")
    private Asset dummyAsset;

    void afterRender()
    {
        if (iconBaseUrl == null)
        {
            iconBaseUrl = dummyAsset.toClientURL().replace("dummyAsset.txt", "");
        }

        javaScriptSupport
            .require("leaflet/sensorsLayer")
            .invoke("initialize")
            .with(componentResources.getId(), mapId, name, iconBaseUrl);
    }
}
