package io.paletaweb.page.torneo;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import io.paletaweb.component.GlobalFooterPanel;
import io.paletaweb.component.GlobalTopPanel;
import io.paletaweb.component.MainHeaderPanel;
import io.paletaweb.page.BasePage;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;

@MountPath("torneo/info")
public class PaletaWebInfoTorneoPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
	BreadCrumb<Void> bc=new BreadCrumb<Void>();
		
	 
		
		BCElement b1 = new BCElement(new Model<String>("Portada")) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage( new RedirectPage("/home"));
			}
		};
		
		bc.addElement(b1);
		
		BCElement b2 = new BCElement(new Model<String>("Torneo Metropolintano"));
		bc.addElement(b2);
		
		
		BCElement b3 = new BCElement(new Model<String>("Fixture"));
		bc.addElement(b3);
		
		add(new MainHeaderPanel<Void>("main-header-panel", null, new Model<String>("Torneo Metropolitano"), bc));
		add(new GlobalTopPanel<Void>("top-panel"));
		add(new GlobalFooterPanel<Void>("footer-panel"));
		
	}
}
