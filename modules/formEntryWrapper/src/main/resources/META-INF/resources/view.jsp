<%@include file="./init.jsp" %>
<%@page import="formEntryWrapper.FormEntryWrapper" %>
<%@page import="com.liferay.dynamic.data.mapping.model.DDMFormInstance" %>
<%@page import="java.util.List" %>
<%@page import="com.liferay.portal.kernel.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>


<p>
<div class="row">
	   <div class="col-sm-12 col-md-12 col-lg-12 col-xl-12">
			<b><liferay-ui:message key="anonymousforms.caption"/></b>
	   </div>
</div>
	<portlet:actionURL name="/anonymousforms/submitexpandos" var="action1"/>
	<aui:form name="formsForm" action="${action1}" >
	
	<aui:fieldset-group markupView="lexicon">
<%
        for(DDMFormInstance fi: (List<DDMFormInstance>)request.getAttribute("formInstances")) {
			DDMFormInstance i=(DDMFormInstance)fi;
			boolean isanonymous= (boolean) i.getExpandoBridge().getAttribute(FormEntryWrapper.ANONYMOUSEXPANDONAME);
			String hashesText= (String) i.getExpandoBridge().getAttribute(FormEntryWrapper.HASHESLISTEXPANDONAME);
			//out.println(String.format("%s =>[%s][%s] <br />",i.getName(),isanonymous,hashesText));
			long id=i.getFormInstanceId();
			String checkname="anon_"+id;
			String textname="hashes_"+id;
			System.out.println(String.format("(%s,%s)=>(%s,%s)",checkname,textname,isanonymous,hashesText));
%>
	   <div class="row">
	   <div class="col-sm-6 col-md-6 col-lg-6 col-xl-6">
			<aui:input label="<%=i.getName()%>" name="<%=checkname %>" type="checkbox" value="<%=isanonymous%>"/>
	   </div>
	   <div class="col-sm-6 col-md-6 col-lg-6 col-xl-6">
			<aui:input label="" name="<%=textname%>" paceholder="hashes" required="<%= false %>" type="textarea" value="<%=hashesText%>" />
	   </div>
	   </div>
<%
		}
%>
	</aui:fieldset-group>
	<aui:button-row>
        <aui:button type="submit" value="save" onClick="submitForm();" />
        <aui:button value="cancel" />
    </aui:button-row>
	</aui:form>
</p>
<script>
var AAA;
</script>
<aui:script>
AUI().use('aui-base', function(A){
    let form=$("form[name$='formsForm']");
    jQuery('input:checkbox',form).each((i,c)=>{
    	let id=c.id.split("_anon_")[1];
    	$("textarea[id$='"+id+"']",form).each((i,o)=>o.hidden=!c.checked);
    });

    jQuery('input:checkbox',form).click(function(e){
    	let id=e.target.id.split("_anon_")[1];
    	$("textarea[id$='"+id+"']",form).each((i,o)=>o.hidden=!e.target.checked);
	});
});
</aui:script>