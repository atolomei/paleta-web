package io.paletaweb.service;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import io.paleta.logging.Logger;
import io.paleta.service.BaseService;
import io.paleta.util.Check;
import jakarta.annotation.PostConstruct;


@Service
public class HTMLExportService extends BaseService {
	
	static private Logger logger = Logger.getLogger(HTMLExportService.class.getName());
	
	@Autowired
	@JsonIgnore
	private final SettingsService settings;
	
	@JsonIgnore
	Configuration cfg;
	
	
	
	@Autowired
	public HTMLExportService(SettingsService settings) {
		this.settings=settings;	
	}
	
	
	public SettingsService getSettingsService() {
		return settings;
	}
	
	
	@JsonIgnore
	public Configuration getConfiguration() {
		return cfg;
	}
	
	
	@PostConstruct
	protected void init() {
		
		Check.requireNonNullStringArgument(getSettingsService().getTemplatesDir(), "templates dir is null");
		
		this.cfg = new Configuration(Configuration.VERSION_2_3_33);
		//cfg.setClassForTemplateLoading(this.getClass(), "/");
		try {
			cfg.setDirectoryForTemplateLoading(new File(getSettingsService().getTemplatesDir()));
		} catch (IOException e) {
			logger.error(e);
		}
		
		cfg.setDefaultEncoding("UTF-8");
		
		if (logger.isDebugEnabled())
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		else
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		cfg.setFallbackOnNullLoopVariable(false);
		cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault()); 	
	
		
		
		
		
		
		
		
		
		
		
	}
	
	
	

}
