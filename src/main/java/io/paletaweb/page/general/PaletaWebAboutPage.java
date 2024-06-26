package io.paletaweb.page.general;


import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import io.paleta.logging.Logger;
import io.paletaweb.component.GlobalFooterPanel;
import io.paletaweb.component.GlobalTopPanel;
import io.paletaweb.component.MainHeaderPanel;
import io.paletaweb.page.BasePage;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;

@MountPath("about")
public class PaletaWebAboutPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(PaletaWebAboutPage.class.getName());
	
	public PaletaWebAboutPage(PageParameters parameters) {
		 super(parameters);
		 logger.info("here ss");
	 }
			
	public PaletaWebAboutPage() {
		 super();
		 logger.info("here");
	 }
	 
	@Override
	public void onInitialize() {
		super.onInitialize();

		BreadCrumb<Void> bc=new BreadCrumb<Void>();
		
		bc.addElement(new BCElement(new Model<String>("Portada")) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage( new RedirectPage("/home"));
			}
		});
		
		bc.addElement(new BCElement(new Model<String>("Acerca")));
		
		add(new MainHeaderPanel<Void>("main-header-panel", null, new Model<String>("Federación Metropolitana de Pelota"), bc));
		add(new GlobalTopPanel<Void>("top-panel"));
		add(new GlobalFooterPanel<Void>("footer-panel"));
		
		
	}

}
