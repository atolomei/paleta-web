package io.paletaweb.page.home;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import io.paleta.logging.Logger;
import io.paletaweb.component.GlobalFooterPanel;
import io.paletaweb.component.GlobalTopPanel;
import io.paletaweb.page.BasePage;


@WicketHomePage
@MountPath("home")
public class PaletaWebHomePage extends BasePage {

	private static final long serialVersionUID = 1L;

	
	static private Logger logger = Logger.getLogger(PaletaWebHomePage.class.getName());
	
	
	public  PaletaWebHomePage(PageParameters parameters) {
		 super(parameters);
		 
		 logger.info("here ss");
		 
	 }
	
	public  PaletaWebHomePage() {
		 super();
	  
		 
		 logger.info("here");
	  
	  
	  
	 }
	 
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel("footer-panel"));
	
		
	}

}
