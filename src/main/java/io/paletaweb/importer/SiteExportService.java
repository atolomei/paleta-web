package io.paletaweb.importer;


import java.io.File;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.service.BaseService;
import io.paleta.util.Check;
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

		//List<File> files = new ArrayList<File>();
	
		File dataDir = new File( getSettings().getDataDir() );
		
		if (dataDir.exists() && dataDir.isDirectory()) {
			
			File files[] = dataDir.listFiles();
			
			for (File fi:files) {
				if (!fi.isDirectory()) {
					if ( fi.getName().endsWith(".csv")) {
						map.put(fi,  Long.MIN_VALUE);
					}
				}
			}
		}
		
		startupLogger.info("Starting -> " + ExportAgent.class.getSimpleName());
		
		this.agent =new ExportAgent() {
			@Override
			public void execute() {
				SiteExportService.this.processExport();
			}
		};
		
		Thread thread = new Thread(agent);
		thread.setDaemon(true);
		thread.setName(this.getClass().getSimpleName());
		thread.start();
		
	}


	
	protected boolean requiresUpdate() {
		
		
		File dataDir = new File( getSettings().getDataDir() );
		
		List<File> list = new ArrayList<File>();
		File files[] = dataDir.listFiles();
		for (File fi:files) {
			if (!fi.isDirectory()) {
				if (fi.getName().endsWith(".csv")) {
					list.add(fi);
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
				if (map.get(file).longValue()<modified) {
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
	

	
	protected synchronized void processExport() {
		
		
		if (!requiresUpdate())
			return;
		
		logger.debug("Torneo execute");
		
		
		
		getTorneo().execute();
		
		final Long now = Long.valueOf(System.currentTimeMillis());
		
		List<File> files = new ArrayList<File>();
		map.keySet().forEach( f -> files.add(f));
		files.forEach( f -> map.put(f, now));
		
		
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
