package cpcc.tapestry.ace.mixins;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Ace editor mixin.
 */
@MixinAfter
public class AceEditor
{
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean animatedScroll;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean hScrollBarAlwaysVisible;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "0")
    private Integer padding;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "80")
    private Integer printMarginColumn;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean readOnly;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean showGutter;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean showInvisibles;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean showPrintMargin;

    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, value = "monokai")
    private String theme;

    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, value = "javascript")
    private String mode;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectContainer
    private AbstractField attachedTo;

    /**
     * After rendering actions.
     */
    public void afterRender()
    {
        JSONObject options = new JSONObject();

        options.put("animatedScroll", animatedScroll);
        options.put("hScrollBarAlwaysVisible", hScrollBarAlwaysVisible);
        options.put("printMarginColumn", printMarginColumn);
        options.put("padding", padding);
        options.put("readOnly", readOnly);
        options.put("showGutter", showGutter);
        options.put("showInvisibles", showInvisibles);
        options.put("showPrintMargin", showPrintMargin);
        options.put("theme", theme);
        options.put("mode", mode);

        javaScriptSupport
            .require("ace/ace-editor")
            .invoke("init")
            .with(attachedTo.getClientId(), options);
    }
}
