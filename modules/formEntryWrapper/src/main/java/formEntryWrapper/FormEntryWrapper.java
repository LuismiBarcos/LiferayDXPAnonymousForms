package formEntryWrapper;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalServiceWrapper;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;

import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author carlos
 */
@Component(
	immediate = true,
	property = {
	},
	service = ServiceWrapper.class
)
public class FormEntryWrapper extends DDMFormInstanceRecordLocalServiceWrapper {

	public static String HASHESLISTEXPANDONAME="incognitoHashesList";
	public static String ANONYMOUSEXPANDONAME="isAnonymous";
	private static String hashField="hash";

	@Reference(unbind = "-")
	private void serviceSetter(DDMFormInstanceRecordLocalService s){setWrappedService(s);}

	public FormEntryWrapper() { super(null); }
	public FormEntryWrapper(DDMFormInstanceRecordLocalService ddmFormInstanceRecordLocalService) {
		super(ddmFormInstanceRecordLocalService);
		//init();
	}

	public void init(){
			Map<String,Boolean>validHashes=new HashMap<String,Boolean>();
			//UserLocalServiceUtil.getCompanyUsers(sc.getCompanyId(),0,Integer.MAX_VALUE)
			UserLocalServiceUtil.getUsers(0,Integer.MAX_VALUE).stream()
					.map(u-> new Object[]{UUID.randomUUID().toString(),u}).forEach(t->{
						String url=String.format("http://localhost:8080/mypagewithfom?hash=%s",t[0]);
						//TODO: send email or whatever (improve this)
						//TODO: add to custom field
                		//TODO: triggering it from a portlet, would be better
                	System.out.println(String.format("sending email to %s => %s",t[1],t[0]));
			});

	};


	@Override
	public com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord addFormInstanceRecord(
		long userId, long groupId, long ddmFormInstanceId,
		com.liferay.dynamic.data.mapping.storage.DDMFormValues ddmFormValues,
		ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		System.out.println(String.format("adding form entry %s!",ddmFormInstanceId));
		DDMFormInstance form = DDMFormInstanceLocalServiceUtil.getDDMFormInstance(ddmFormInstanceId);
		String[] hashes = form.getExpandoBridge().getAttribute(HASHESLISTEXPANDONAME).toString().split("\n");


		//IF THE HASH IS LIKE ANY OF THE HASHES
		if (ddmFormValues.getDDMFormFieldValues().stream().
				anyMatch(k->(k.getName().equals(hashField))&&
						Stream.of(hashes).map(String::trim).anyMatch(h->h.equals(k.getValue().getString(k.getValue().
								getDefaultLocale()).toString().trim())))){

			Value value = ddmFormValues.getDDMFormFieldValuesMap().get(hashField).get(0).getValue();
			String submittedHash = value.getString(value.getDefaultLocale()).toString().trim();

			System.out.println(String.format("hash [%s] is correct",submittedHash));

			String newVal = Stream.of(hashes).filter(h -> !h.trim().equals(submittedHash)).reduce("", (acc, l) -> l + "\n" + acc);
			form.getExpandoBridge().setAttribute(HASHESLISTEXPANDONAME,newVal);
			return super.addFormInstanceRecord(userId,groupId,ddmFormInstanceId, ddmFormValues,serviceContext);
		}else{
			int index = ddmFormValues.getDDMFormFieldValues().indexOf(hashField);
			SessionErrors.add(serviceContext.getLiferayPortletRequest(),"WRONG HASH you are not allowed to participate on this form");
			System.out.println(String.format("hash is INcorrect"));
			//SessionErrors.add(serviceContext.getRequest().getSession(),"WRONG HASH you are not allowed to participate on this form");
		    throw new PortalException("WRONG HASH you are not allowed to participate on this form ");
        }

	}

}