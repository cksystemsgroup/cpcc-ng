package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Hidden;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet draw control component.
 */
@MixinAfter
public class LeafletDrawControl
{
    @InjectComponent
    private Hidden drawData;

    @InjectComponent
    private Submit drawButton;

    @InjectComponent
    private Form popupTemplate;

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources componentResources;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "topleft")
    private String position;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean polygonEnabled;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean rectangleEnabled;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean polylineEnabled;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean circleEnabled;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private JSONObject data;

    void afterRender()
    {
        JSONObject draw = new JSONObject();

        draw.put("polygon", polygonEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: true }")
            : Boolean.FALSE);

        draw.put("rectangle", rectangleEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: true }")
            : Boolean.FALSE);

        draw.put("polyline", polylineEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: false} ")
            : Boolean.FALSE);

        draw.put("circle", circleEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: true }")
            : Boolean.FALSE);

        JSONObject params = new JSONObject("position", position, "draw", draw);
        params.put("position", position);
        params.put("dataId", drawData.getClientId());
        params.put("submitId", drawButton.getClientId());
        params.put("popupTemplateId", popupTemplate.getClientId());

        javaScriptSupport
            .require("leaflet/drawControl")
            .with(componentResources.getId(), mapId, params);
    }

}
