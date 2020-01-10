package AnonymousFormsPortlet.portlet;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalServiceUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import AnonymousFormsPortlet.constants.AnonymousFormsPortletKeys;
import formEntryWrapper.FormEntryWrapper;

/**
 * @author carlos
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=AnonymousForms",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + AnonymousFormsPortletKeys.ANONYMOUSFORMS,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class AnonymousFormsPortlet extends MVCPortlet {

    @Override
	public void doView( RenderRequest req, RenderResponse res) throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay)req.getAttribute( WebKeys.THEME_DISPLAY);
		themeDisplay.getCompanyId();
		List<DDMFormInstance> formInstances = DDMFormInstanceLocalServiceUtil.getFormInstances(themeDisplay.getSiteGroupId());
		//String portletNamespace=((ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY)).getPortletDisplay().getNamespace();
		//req.setAttribute("portletNamespace",portletNamespace);
		req.setAttribute("formInstances",formInstances);
		include(viewTemplate, req, res);
	}


	private void sendMailToAllUsers() throws AddressException, UnsupportedEncodingException {
		//MailMessage mailMessage=new MailMessage();
		//mailMessage.setBody("set body here");
		//mailMessage.setFrom(new InternetAddress("carlos.hernandez@liferay.com","carlos"));
		//mailMessage.setSubject("set mail subject here");
		//mailMessage.setTo(new InternetAddress("carlos@liferay.com"));
		//mailMessage.setHTMLFormat(true);
		//MailServiceUtil.sendEmail(mailMessage);
	}




}