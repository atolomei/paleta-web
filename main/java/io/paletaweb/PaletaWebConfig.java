package io.paletaweb;

import org.apache.wicket.protocol.http.WebApplication;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;

import io.paletaweb.logging.Logger;



@ApplicationInitExtension
public class PaletaWebConfig implements WicketApplicationInitConfiguration {
				
	static private Logger logger = Logger.getLogger(PaletaWebConfig.class.getName());
	
	@Override
	public void init(WebApplication webApplication) {

		
		
		logger.info("here");


	}

}
