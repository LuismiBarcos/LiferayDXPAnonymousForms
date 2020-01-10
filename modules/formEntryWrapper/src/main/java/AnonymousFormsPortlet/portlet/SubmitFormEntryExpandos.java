package AnonymousFormsPortlet.portlet;


import AnonymousFormsPortlet.constants.AnonymousFormsPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.util.Enumeration;
import java.util.stream.Stream;

@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + AnonymousFormsPortletKeys.ANONYMOUSFORMS,
        "mvc.command.name=/anonymousforms/submitexpandos"
    },
    service = MVCActionCommand.class
)

public class SubmitFormEntryExpandos extends BaseMVCActionCommand {
    @Override
    protected void doProcessAction(ActionRequest req, ActionResponse res) throws Exception {
        System.out.println(String.format("action being called"));

        //String[] value = ParamUtil.getParameterValues(req, "anon_");
        req.getParameterMap().forEach((k,v)-> System.out.println(String.format("%s => %s",k,v)));
    }
}