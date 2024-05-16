package io.paletaweb.page.admin;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import io.paleta.logging.Logger;
import io.paletaweb.component.GlobalTopPanel;
import io.paletaweb.page.BasePage;



@MountPath("admin")
public class PaletaWebAdminPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(PaletaWebAdminPage.class.getName());
	
	public  PaletaWebAdminPage(PageParameters parameters) {
		 super(parameters);
	 }
	
	public  PaletaWebAdminPage() {
		 super();
	 }

	@Override
	public void onInitialize() {
		super.onInitialize();
		add(new GlobalTopPanel("top-panel"));
		
	}

}
