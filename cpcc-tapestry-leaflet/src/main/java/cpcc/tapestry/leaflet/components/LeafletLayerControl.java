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
 * Leaflet layer control component.
 */
@MixinAfter
public class LeafletLayerControl
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Inject
    private ComponentResources componentResources;

    void afterRender()
    {
        javaScriptSupport
            .require("leaflet/layerControl")
            .with(componentResources.getId(), mapId);
    }
}
