package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet region layer component.
 */
@MixinAfter
public class LeafletRegionLayer
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private Request request;

    @InjectComponent
    private Zone regionZone;

    @Property
    @Parameter(required = true, principal = true, autoconnect = true)
    private String value;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "[No Name]")
    private String name;

    @Parameter
    private Object[] context;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private int frequencySecs;

    void afterRender()
    {
        String eventURL = componentResources.createEventLink("regionUpdate", context).toAbsoluteURI();

        javaScriptSupport
            .require("leaflet/regionLayer")
            .invoke("initialize")
            .with(componentResources.getId(), mapId, name, regionZone.getClientId(), eventURL, frequencySecs);

        javaScriptSupport
            .require("leaflet/regionLayer")
            .invoke("updateRegionData")
            .with(componentResources.getId(), mapId);
    }

    void onRegionUpdate()
    {
        if (request.isXHR())
        {
            ajaxResponseRenderer.addRender(regionZone);
            addRegionDataUpdateCallback();
        }
    }

    void addRegionDataUpdateCallback()
    {
        ajaxResponseRenderer.addCallback(new JavaScriptCallback()
        {
            public void run(JavaScriptSupport jsSupport)
            {
                jsSupport
                    .require("leaflet/regionLayer")
                    .invoke("updateRegionData")
                    .with(componentResources.getId(), mapId);
            }
        });
    }

    public String getRegions()
    {
        return value;
    }
}
