package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet vehicle layer component.
 */
@MixinAfter
@Import(stylesheet = {"css/vehicle.css"})
public class LeafletVehicleLayer
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
    private Zone vehicleZone;

    @Property
    @Parameter(required = true, principal = true, autoconnect = true)
    private String value;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "[No Name]")
    private String name;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String iconBaseUrl;

    @Parameter
    private Object[] context;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private int frequencySecs;

    @Inject
    @Path("images/dummyAsset.txt")
    private Asset dummyAsset;

    void afterRender()
    {
        if (iconBaseUrl == null)
        {
            iconBaseUrl = dummyAsset.toClientURL().replace("dummyAsset.txt", "");
        }

        String eventURL = componentResources.createEventLink("vehicleUpdate", context).toAbsoluteURI();

        javaScriptSupport
            .require("leaflet/vehicleLayer")
            .invoke("initialize")
            .with(componentResources.getId(), mapId, name, vehicleZone.getClientId(), eventURL, frequencySecs
                , iconBaseUrl);

        javaScriptSupport
            .require("leaflet/vehicleLayer")
            .invoke("updateVehicleData")
            .with(componentResources.getId(), mapId);
    }

    void onVehicleUpdate()
    {
        if (request.isXHR())
        {
            ajaxResponseRenderer.addRender(vehicleZone);
            addVehicleDataUpdateCallback();
        }
    }

    void addVehicleDataUpdateCallback()
    {
        ajaxResponseRenderer.addCallback(new JavaScriptCallback()
        {
            public void run(JavaScriptSupport jsSupport)
            {
                jsSupport
                    .require("leaflet/vehicleLayer")
                    .invoke("updateVehicleData")
                    .with(componentResources.getId(), mapId);
            }
        });
    }

    public String getVehicles()
    {
        return value;
    }
}
