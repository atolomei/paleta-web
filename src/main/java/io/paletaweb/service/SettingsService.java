package io.paletaweb.service;

import java.io.File;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.paleta.logging.Logger;
import io.paleta.service.BaseService;
import jakarta.annotation.PostConstruct;


@Configuration
@PropertySource("classpath:application.properties")
public class SettingsService extends BaseService {

	private static final OffsetDateTime systemStarted = OffsetDateTime.now();
	
	static private Logger logger = Logger.getLogger(SettingsService.class.getName());
	static private Logger startuplogger = Logger.getLogger("StartupLogger");
	

	@Value("${scanFreqmillisecs:5000}")
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

	
	
	@Autowired
	public SettingsService() {
	}

	public OffsetDateTime getSystemStartTime() {
		return systemStarted;
	}
	
	public String getTemplatesDir() {
		return this.templatesDir;
	}
	
	public String getDataDir() {
		return this.dataDir;
	}
	
	public String getExportDir() {
		return this.exportDir;
	}
	

	
	public String getIndexExportDir() {
		return this.indexExportDir;
	}

	
	@PostConstruct
	protected void init() {

		if (templatesDir==null || (templatesDir.trim().length()==0) || templatesDir.trim().equals("null"))
			templatesDir="."+File.separator+"templates";
		templatesDir=templatesDir.trim();
		
		if (exportDir==null || exportDir.trim().length()==0 || exportDir.trim().equals("null"))
			exportDir="."+File.separator+"export";
		exportDir=exportDir.trim();
		
		if (dataDir==null || dataDir.trim().length()==0  || dataDir.trim().equals("null"))
			dataDir="."+File.separator+"data";
		dataDir=dataDir.trim();
		
		if (indexExportDir==null || indexExportDir.trim().length()==0  || indexExportDir.trim().equals("null"))
			indexExportDir="."+File.separator+"torneo-cuba";
		indexExportDir=indexExportDir.trim();
		
		
	}

	

	
	
	
	
	
}
