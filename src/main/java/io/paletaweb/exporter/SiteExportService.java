package io.paletaweb.exporter;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.service.BaseService;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;
import io.paletaweb.torneo.TorneoCuba;
import jakarta.annotation.PostConstruct;

@Service
public class SiteExportService extends BaseService {
	
	static public Logger logger = Logger.getLogger(SiteExportService.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	@JsonIgnore
	private ExportAgent agent;
	
	@Autowired
	@JsonIgnore
	protected SettingsService settings;
	
	@Autowired
	@JsonIgnore
	protected TorneoCuba torneo;
	
	@Autowired
	@JsonIgnore
	HTMLExportService htmlExportService;

	@JsonIgnore
	private Map<File, Long> map = new HashMap<File, Long>();
	
	public SiteExportService() {
	}
	
	public void execute() {
	}
	
	@PostConstruct
	public void init() {
	

		{
			File dataDir = new File(getSettings().getDataDir());
			
			if (dataDir.exists() && dataDir.isDirectory()) {
				File files[] = dataDir.listFiles();
				for (File fi:files) {
					if (!fi.isDirectory()) {
						if (fi.getName().endsWith(".csv") || fi.getName().endsWith(".txt") || fi.getName().endsWith(".info")) {
							map.put(fi,  Long.MIN_VALUE);
						}
					}
				}
			}
		}
		
		
		{
			File dataDir = new File(getSettings().getTemplatesDir());
			if (dataDir.exists() && dataDir.isDirectory()) {
				File files[] = dataDir.listFiles();
				for (File fi:files) {
					if (!fi.isDirectory()) {
						if ( fi.getName().endsWith(".ftl")) {	
							map.put(fi,  Long.MIN_VALUE);
						}
					}
				}
			}
		}

		
		startupLogger.info("Starting -> " + ExportAgent.class.getSimpleName());
		
		this.agent =new ExportAgent(getSettings().getScanFreqMillisecs()) {
			@Override
			public void execute() {
				try {
				SiteExportService.this.processExport();
				} catch (Exception e) {
					logger.error(e);
				}
			}
		};
		
		Thread thread = new Thread(this.agent);
		thread.setDaemon(true);
		thread.setName(this.getClass().getSimpleName());
		thread.start();
		
	}

	protected boolean requiresUpdate() {
		
		List<File> list = new ArrayList<File>();
		{
			File dataDir = new File( getSettings().getDataDir());
			File files[] = dataDir.listFiles();
			for (File fi:files) {
				if (!fi.isDirectory()) {
					if (fi.getName().endsWith(".csv") || fi.getName().endsWith(".txt") || fi.getName().endsWith(".info")) {
						list.add(fi);
					}
				}
			}
		}
		
		{
			File dataDir = new File( getSettings().getTemplatesDir());
			File files[] = dataDir.listFiles();
			for (File fi:files) {
				if (!fi.isDirectory()) {
					if (fi.getName().endsWith(".ftl")) {
						list.add(fi);
					}
				}
			}
		}

		if (list.size()!=map.size())
			return true;
		
		for (File file:list) {
			if (!map.containsKey(file)) {
				map.put(file,  Long.MIN_VALUE);
				return true;
			}
			else {
				long modified = file.lastModified();
				if (map.get(file).longValue() < modified) {
					return true;
				}
			}
		}
		
		for (File file: map.keySet()) {
			if (!list.contains(file)) {
				map.remove(file);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 */
	protected synchronized void processExport() {
		
		if (!requiresUpdate()) {
			logger.debug("Update not required");
			return;
		}
		
		logger.debug("About to execute import - export");
		
		getTorneo().execute();
		
		logger.debug("export done");
		
		final Long now = Long.valueOf(System.currentTimeMillis());
		
		List<File> files = new ArrayList<File>();
		map.keySet().forEach( f -> files.add(f));
		files.forEach(f -> map.put(f, now));
	}
	
	public SettingsService getSettings() {
		return settings;
	}

	public void setSettings(SettingsService settings) {
		this.settings = settings;
	}
	
	public TorneoCuba getTorneo() {
		return torneo;
	}

	public void setTorneo(TorneoCuba torneo) {
		this.torneo = torneo;
	}

	public HTMLExportService getHtmlExportService() {
		return htmlExportService;
	}

	public void setHtmlExportService(HTMLExportService htmlExportService) {
		this.htmlExportService = htmlExportService;
	}
	
}
