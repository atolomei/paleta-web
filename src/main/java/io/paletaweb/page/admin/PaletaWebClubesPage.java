package io.paletaweb.page.admin;


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import io.paletaweb.component.GlobalFooterPanel;
import io.paletaweb.component.GlobalTopPanel;
import io.paletaweb.component.MainHeaderPanel;
import io.paletaweb.page.BasePage;
import io.wktui.struct.list.ListPanel;


/**
 * Club 
 * Localidad
 * Club
 * Categoria
 * Persona
 * Jugador
 * 
 */
@MountPath("clubes")
public class PaletaWebClubesPage extends BasePage {
	
	private static final long serialVersionUID = 1L;

	
	public PaletaWebClubesPage() {
	}
	
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		add(new GlobalTopPanel<Void>("top-panel"));
		add(new MainHeaderPanel<Void>("main-header-panel", null, new Model<String>("Clubes")));
		add(new GlobalFooterPanel<Void>("footer-panel"));
		
		/**
		NavBar<Void> nav = new NavBar<Void>("navbar");

		DropDownMenu<Void> menu = new DropDownMenu<Void>("item", null);
		
		menu.addItem(new io.wktui.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public AbstractMenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItemPanel<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("http://google.com"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("uno");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		

		menu.addItem(new io.wktui.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public AbstractMenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItemPanel<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						
						setResponsePage(new RedirectPage("http://google.com"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("dos");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
					 
				};
			}
		});
		
		//add(menu);

		//nav.addCollapse(new LabelPanel("item", new Model<String>("hola")));
		nav.addNoCollapseLeft(menu);

		
		add(nav);
		*/
		
		// ServiceLocator.getInstance().getApplicationContext().getBean();
		
		List<IModel<String>> list = new ArrayList<IModel<String>>();
		
		list.add( new Model<String>("CUBA"));
		list.add( new Model<String>("GEBA"));
		list.add( new Model<String>("Belgrano Social"));
		list.add( new Model<String>("Gure Echea"));
		list.add( new Model<String>("Navarro"));
		
		ListPanel<String> clubes = new ListPanel<String>("clubes");
		clubes.setHasExpander(true);
		clubes.setListModel(list);
		add(clubes);

		

		
		
		
		
		
		
		
		
	}
}
