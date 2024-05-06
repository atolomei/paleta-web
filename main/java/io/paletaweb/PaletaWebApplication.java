package io.paletaweb;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.paletaweb.logging.Logger;
import jakarta.annotation.PostConstruct;

@SpringBootApplication


@ComponentScan({"io.paletaweb"})
public class PaletaWebApplication {
			
	static private Logger logger = Logger.getLogger(PaletaWebApplication.class.getName());
	static private Logger std_logger = Logger.getLogger("StartupLogger");
	
	
	static public String[] cmdArgs = null;
	

	
	static public final String SEPARATOR = "---------------------------------";
	
	
	public static void main(String[] args) throws Exception {
	    
		logger.info("here");
		
		SpringApplication application = new SpringApplication(PaletaWebApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		cmdArgs = args;
		application.run(args);
		
	  }
	
	
	@PostConstruct
	public void onInitialize() {
		
		std_logger.info("");
		for (String s : PaletaWebVersion.getAppCharacterName())
			std_logger.info(s);
		
		std_logger.info(SEPARATOR);
		std_logger.info("This software is licensed under the Apache License, Version 2.0");
		std_logger.info("http://www.apache.org/licenses/LICENSE-2.0");

		initShutdownMessage();
	}
	
	/**
	 *
	 *
	 */
	private void initShutdownMessage() {
	    Runtime.getRuntime().addShutdownHook(new Thread() {
           public void run() {
           	std_logger.info("");
           	std_logger.info("As the roman legionaries used to say when falling in battle");
           	std_logger.info("'Dulce et decorum est pro patria mori'...Shuting down... goodbye.");
           	std_logger.info("");
           }
       });
	}
	

}
