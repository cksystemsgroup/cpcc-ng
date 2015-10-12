package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet empty layer component.
 */
@MixinAfter
public class LeafletEmptyLayer
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "[No Name]")
    private String name;

    @Inject
    private ComponentResources componentResources;

    void afterRender()
    {
        javaScriptSupport
            .require("leaflet/emptyLayer")
            .with(componentResources.getId(), mapId, name);
    }
}
