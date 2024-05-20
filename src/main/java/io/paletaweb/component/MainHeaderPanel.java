package io.paletaweb.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import wktui.base.BasePanel;
import wktui.base.ModelPanel;

public class MainHeaderPanel<T> extends ModelPanel<T> {

	IModel<String> title;
	
	public MainHeaderPanel() {
		this("main-header", null, null);
	}

	public MainHeaderPanel(String id, IModel<T> model, IModel<String> title) {
		super(id, model);
		this.title=title;
	}

	
	public IModel<String> getTitle() {
		return title;
	}
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		
		Label t = new Label ("title", getTitle()) {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return getTitle()!=null;
			};
		};
		add(t);
		
		BreadCrumb<Void> bc=new BreadCrumb<Void>();
		
		add(bc);
		
		BCElement b1 = new BCElement(new Model<String>("bc1")) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage( new RedirectPage("/about"));
			}
		};
		
		bc.addElement(b1);

		
		BCElement b2 = new BCElement(new Model<String>("bc2"));
		bc.addElement(b2);
		
		
		
	}
		
		
}
