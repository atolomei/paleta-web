package io.paletaweb.service;


import java.io.File;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import io.paleta.logging.Logger;
import io.paleta.service.BaseService;
import io.paletaweb.SystemService;
import jakarta.annotation.PostConstruct;


@Configuration
//@PropertySource("classpath:application.properties")
public class SettingsService extends BaseService implements SystemService {
	
	static private Logger logger = Logger.getLogger(SettingsService.class.getName());
	static private Logger startuplogger = Logger.getLogger("StartupLogger");
	
	private static final OffsetDateTime systemStarted = OffsetDateTime.now();
	
	
	@Value("${scanFreqmillisecs:10000}")
	private int scanFreqMillisecs;

	public int getScanFreqMillisecs() {return this.scanFreqMillisecs;}
	
	@Value("${templates:null}")
	private String templatesDir;
	
	@Value("${export:null}")
	private String exportDir;
	
	@Value("${data:null}")
	private String dataDir;

	@Value("${indexexport:null}")
	private String indexExportDir;
	
	
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
		@Value("${server.port:8092}")
		protected int port;
		
		@Value("${server.url:http://localhost}")
		@NonNull
		protected String url;
	
	@Autowired
	public SettingsService() {
	}

	public OffsetDateTime getSystemStartTime() {
		return systemStarted;
	}
	
	//public String getTournamentTemplatesDir(String tournament) {return "."+ File.separator + getTemplatesDir() + File.separator + tournament;}
	public String getTemplatesDir() {return this.templatesDir;}
	
	//public String getTournamentExportDir(String tournament) {return "."+ File.separator + "torneos" + File.separator + tournament + File.separator + getExportDir();}
	//public String getExportDir() {return this.exportDir;}
	
	public String getTournamentDataDir(String tournament) {return "." + File.separator + "torneos" + File.separator + tournament + File.separator + getDataDir();}
	public String getDataDir() {return this.dataDir;}
	
	public String getTournamentIndexExportDir(String tournament) {return "."+ File.separator + getIndexExportDir() + File.separator + "torneos" + File.separator + tournament;}
	public String getIndexExportDir() {    return this.indexExportDir;}


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
	
	@PostConstruct
	protected void init() {

		if (templatesDir==null || (templatesDir.trim().length()==0) || templatesDir.trim().equals("null"))
			templatesDir="templates";
		templatesDir=templatesDir.trim();
		
		if (exportDir==null || exportDir.trim().length()==0 || exportDir.trim().equals("null"))
			exportDir="export";
		exportDir=exportDir.trim();
		
		if (dataDir==null || dataDir.trim().length()==0  || dataDir.trim().equals("null"))
			dataDir="data";
		dataDir=dataDir.trim();
		
		if (indexExportDir==null || indexExportDir.trim().length()==0 || indexExportDir.trim().equals("null"))
			indexExportDir="torneos-web";
		indexExportDir=indexExportDir.trim();
		
	}
	
	
}
