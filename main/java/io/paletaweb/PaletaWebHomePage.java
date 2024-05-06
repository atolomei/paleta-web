package io.paletaweb;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import io.paletaweb.logging.Logger;


@WicketHomePage
public class PaletaWebHomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	
	static private Logger logger = Logger.getLogger(PaletaWebHomePage.class.getName());
	
	
	 PaletaWebHomePage(PageParameters parameters) {
		 super(parameters);
		 
		 logger.info("here ss");
		 
	 }
	 PaletaWebHomePage() {
		 super();
	  
		 
		 
		 
		 logger.info("here");
	  
	  
	  
	 }
	 

}
