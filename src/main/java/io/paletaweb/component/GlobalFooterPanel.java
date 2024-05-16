package io.paletaweb.component;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import wktui.base.BasePanel;
import wktui.base.LabelPanel;


/**
 * 
 * 
 */
public class GlobalFooterPanel extends BasePanel {
			
	private static final long serialVersionUID = 1L;

	public GlobalFooterPanel(String id) {
		this(id, null);
	}
	
	public GlobalFooterPanel(String id, IModel<?> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		NavBar<Void> nav = new NavBar<Void>("navbar");
		
		nav.addCollapse(new LinkMenuItem<Void>("item") {
							
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/home"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Portada");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				});
		
		
		nav.addCollapse(new LinkMenuItem<Void>("item") {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new RedirectPage("/fixture"));
			}

			@Override
			public IModel<String> getLabel() {
				return new Model<String>("Fixture");
			}

			@Override
			public String getBeforeClick() {
				return null;
			}
		});
		


		nav.addCollapse(new LinkMenuItem<Void>("item") {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new RedirectPage("/tabla"));
			}

			@Override
			public IModel<String> getLabel() {
				return new Model<String>("Tabla");
			}

			@Override
			public String getBeforeClick() {
				return null;
			}
		});

		
		nav.addCollapse(new LinkMenuItem<Void>("item") {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new RedirectPage("/contact"));
			}

			@Override
			public IModel<String> getLabel() {
				return new Model<String>("Contacto");
			}

			@Override
			public String getBeforeClick() {
				return null;
			}
		});
		
		
		
		
		add(nav);
		
	}	
		
}
