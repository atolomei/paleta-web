package io.paletaweb.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wkyui.breadcrumb.BCElement;
import io.wkyui.breadcrumb.BreadCrumb;
import wktui.base.BasePanel;

public class MainHeaderPanel extends BasePanel {

	IModel<String> title;
	
	public MainHeaderPanel() {
		this("main-header", null);

	}

	public MainHeaderPanel(String id, IModel<String> title) {
		super(id);
		this.title=title;

	}

	
	public IModel<String> getTitle() {
		return title;
	}
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		
		Label t = new Label ("title", getTitle()) {
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
