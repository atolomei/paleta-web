package io.paletaweb;



import java.time.OffsetDateTime;
import java.util.Locale;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paletaweb.logging.Logger;



@Component
public class PaletaWebStartupApplicationRunner implements ApplicationRunner {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(PaletaWebStartupApplicationRunner.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	
	static public final String SEPARATOR = "---------------------------------";
	
	@JsonIgnore
	private final ApplicationContext appContext;

	public PaletaWebStartupApplicationRunner(ApplicationContext appContext) {
		this.appContext = appContext;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Command line args:");
			args.getNonOptionArgs().forEach( item -> startupLogger.debug(item));
			//startupLogger.debug(ServerConstant.SEPARATOR);
		}

		Locale.setDefault(Locale.ENGLISH);
		
		//boolean iGeneral = initGeneral();
		//if(iGeneral)
		//	startupLogger.info(ServerConstant.SEPARATOR);
		
		startupLogger.info	(SEPARATOR);
		
		startupLogger.info	("Startup at -> " + OffsetDateTime.now().toString());
	}
	
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
	
	
}
