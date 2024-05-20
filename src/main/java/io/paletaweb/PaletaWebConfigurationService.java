package io.paletaweb;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


import io.paleta.logging.Logger;
import io.paleta.service.BaseService;

@Configuration
@PropertySource("paleta.properties")
@Service
public class PaletaWebConfigurationService extends BaseService implements SystemService {

	
	static private Logger logger = Logger.getLogger(PaletaWebConfigurationService.class.getName());
	static private Logger startuplogger = Logger.getLogger("StartupLogger");

	private static final OffsetDateTime systemStarted = OffsetDateTime.now();
	
	// SERVER ------------------------
	
		/* default -> paleta */
		@Value("${accessKey:paleta}")
		@NonNull
		protected String accessKey;
		
		/* default -> paleta */
		@Value("${secretKey:paleta}")
		@NonNull
		protected String secretKey;

		/* default port -> 8080 */
		@Value("${server.port:8080}")
		protected int port;

		
		@Value("${server.url:http://localhost}")
		@NonNull
		protected String url;
		
		
		
	/**
	 * 
	 */
	@Autowired
	public PaletaWebConfigurationService() {
	}

	public OffsetDateTime getSystemStartTime() {
		return systemStarted;
	}
	
	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getUrl() {
		return url;
	}
	
	public int getPort() {
		return port;
	}
	

}
