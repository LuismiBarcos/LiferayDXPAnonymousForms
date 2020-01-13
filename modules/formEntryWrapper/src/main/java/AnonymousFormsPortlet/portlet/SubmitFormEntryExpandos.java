package AnonymousFormsPortlet.portlet;


import AnonymousFormsPortlet.constants.AnonymousFormsPortletKeys;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalServiceUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import formEntryWrapper.FormEntryWrapper;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

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
        req.getParameterMap().forEach((k,v)->{
            if(k.startsWith("hashes_")){
                try {
                    long formId = Long.parseLong(k.split("_")[1]);
                    DDMFormInstance formInstance = DDMFormInstanceLocalServiceUtil.getFormInstance(formId);
                    formInstance.getExpandoBridge().setAttribute(FormEntryWrapper.HASHESLISTEXPANDONAME,v[0]);
                    System.out.println(String.format("saving %s => %s(%s)",FormEntryWrapper.HASHESLISTEXPANDONAME,v[0],v.getClass().getName()));
                    System.out.println(String.format("saved %s => %s",FormEntryWrapper.HASHESLISTEXPANDONAME,formInstance.getExpandoBridge().getAttribute(FormEntryWrapper.HASHESLISTEXPANDONAME)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(k.startsWith("anon_")){
                try {
                    long formId = Long.parseLong(k.split("_")[1]);
                    DDMFormInstance formInstance = DDMFormInstanceLocalServiceUtil.getFormInstance(formId);
                    formInstance.getExpandoBridge().setAttribute(FormEntryWrapper.ANONYMOUSEXPANDONAME,Boolean.parseBoolean(v[0]));
                    System.out.println(String.format("saving %s => %s(%s)",FormEntryWrapper.ANONYMOUSEXPANDONAME,v[0],v.getClass().getName()));
                    System.out.println(String.format("saved %s => %s",FormEntryWrapper.ANONYMOUSEXPANDONAME,formInstance.getExpandoBridge().getAttribute(FormEntryWrapper.ANONYMOUSEXPANDONAME)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
}