package io.paletaweb.page.general;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import io.paletaweb.component.GlobalFooterPanel;
import io.paletaweb.component.GlobalTopPanel;
import io.paletaweb.component.MainHeaderPanel;
import io.paletaweb.page.BasePage;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;

@MountPath("contacto")
public class PaletaWebContactoPage extends BasePage {

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
		BCElement b2 = new BCElement(new Model<String>("Contacto"));
		bc.addElement(b2);
		
		add(new MainHeaderPanel<Void>("main-header-panel", null, new Model<String>("Contacto"), bc));
		add(new GlobalTopPanel<Void>("top-panel"));
		add(new GlobalFooterPanel<Void>("footer-panel"));
		
		
		
	}
	
	
}
