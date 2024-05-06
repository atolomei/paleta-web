package io.paletaweb.page;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;

public abstract class BasePage extends WebPage {

	private static final long serialVersionUID = 1L;


	public BasePage() {
		initPage();
	}
	
	private void initPage(){
	}
	
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		/**
		response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
		response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getWicketAjaxReference()));
		response.render(JavaScriptHeaderItem.forReference(NotyJSReference.INSTANCE));
		response.render(JavaScriptHeaderItem.forReference(NotyPackagedJSReference.INSTANCE));
		response.render(JavaScriptHeaderItem.forReference(NotyThemeBootstrapJSReference.INSTANCE));
		
		String bootstrapPrefixPath = "bootstrap/current";
		response.render(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference(bootstrapPrefixPath + "/js/bootstrap.js")));
		response.render(CssHeaderItem.forReference(new WebjarsJavaScriptResourceReference(bootstrapPrefixPath + "/css/bootstrap.css")));
		**/
	}
	
}
