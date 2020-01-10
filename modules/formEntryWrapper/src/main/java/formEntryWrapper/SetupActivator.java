package formEntryWrapper;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.expando.kernel.exception.DuplicateTableNameException;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.service.ExpandoColumnLocalServiceUtil;
import com.liferay.expando.kernel.service.ExpandoTableLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.HashMap;

public class SetupActivator implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
	    _log.info("*** \n\n\ncreate expando tables \n\n\n***");

	    HashMap<String,Object[]> columns=new HashMap<String,Object[]>();
		columns.put(FormEntryWrapper.HASHESLISTEXPANDONAME, new Object[]{ExpandoColumnConstants.STRING,""});
		columns.put(FormEntryWrapper.ANONYMOUSEXPANDONAME, new Object[]{ExpandoColumnConstants.BOOLEAN,false});

		for(long companyId: PortalUtil.getCompanyIds()){
			_log.info("for company "+companyId);
			createExpandos(companyId,columns);
		}

	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	private void createExpandos(long companyId, HashMap<String, Object[]> columns) {
		ExpandoTable table = null;
		try {
			try { table = ExpandoTableLocalServiceUtil.addDefaultTable(companyId, DDMFormInstance.class.getName()); }
			catch (DuplicateTableNameException dtne) { table = ExpandoTableLocalServiceUtil.getDefaultTable(companyId, DDMFormInstance.class.getName()); }
			long tableId = table.getTableId();

			ArrayList<ExpandoColumn> cols= new ArrayList<ExpandoColumn>();
			columns.forEach((k,v)->{
				try {
					ExpandoColumn col = ExpandoColumnLocalServiceUtil.addColumn(tableId,k,(int)v[0],v[1]);
					cols.add(col);
				} catch (PortalException e) {
						_log.info("the column already exists, no need to create it");
				}
			});
			cols.forEach(c->{ ExpandoColumnLocalServiceUtil.updateExpandoColumn(c); });
		} catch (PortalException pe) {
			pe.printStackTrace();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(SetupActivator.class);

}
