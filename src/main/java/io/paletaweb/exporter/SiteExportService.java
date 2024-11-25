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
import io.paletaweb.club.tournament.TorneoCuba;
import io.paletaweb.club.tournament.TournamentManager;
import io.paletaweb.club.tournament.Tournament;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;
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
	
	//@Autowired
	//@JsonIgnore
	//protected TorneoCuba torneo;
	
	@Autowired
	@JsonIgnore
	protected TournamentManager tm;

	
	@Autowired
	@JsonIgnore
	HTMLExportService htmlExportService;

	
	private Map<String, Map<File, Long>> map = new HashMap<String, Map<File, Long>>();
	

	@JsonIgnore
	private Map<String, Boolean> ref = new HashMap<String, Boolean>();
	
	
	
	public SiteExportService() {
	}
	
	public void execute() {
	}
	
	@PostConstruct
	public void init() {
		
		{
			
			
			String key ="viamonte2024";
			Tournament to = tm.getTourmanent(key);
			map.put(to.getKey(), new HashMap<File, Long>());
			
			ref.put(to.getKey(), Boolean.valueOf(false));
			{
				String path = getSettings().getTournamentDataDir(to.getKey()); 
				File dataDir = new File(path);
				
				if (dataDir.exists() && dataDir.isDirectory()) {
					File files[] = dataDir.listFiles();
					for (File fi:files) {
						if (!fi.isDirectory()) {
							if (fi.getName().endsWith(".csv") || fi.getName().endsWith(".txt") || fi.getName().endsWith(".info") || fi.getName().endsWith(".html")) {
								map.get( to.getKey() ).put(fi,  Long.MIN_VALUE);
							}
						}
					}
				}
			}
			
			String path = getSettings().getTemplatesDir();
			File dataDir = new File(path);
			
			if (dataDir.exists() && dataDir.isDirectory()) {
				File files[] = dataDir.listFiles();
				for (File fi:files) {
					if (!fi.isDirectory()) {
						if ( fi.getName().endsWith(".ftl") && fi.getName().startsWith(key)) {	
							map.get( to.getKey() ).put(fi,  Long.MIN_VALUE);
						}
					}
				}
			}
		}
		
		
		
		{
			
			
			String key ="cubab2024";
			Tournament to = tm.getTourmanent(key);
			map.put(to.getKey(), new HashMap<File, Long>());
			
			ref.put(to.getKey(), Boolean.valueOf(false));
			{
				
				String path = getSettings().getTournamentDataDir(to.getKey()); 
				File dataDir = new File(path);
				
				if (dataDir.exists() && dataDir.isDirectory()) {
					File files[] = dataDir.listFiles();
					for (File fi:files) {
						if (!fi.isDirectory()) {
							if (fi.getName().endsWith(".csv") || fi.getName().endsWith(".txt") || fi.getName().endsWith(".info") || fi.getName().endsWith(".html")) {
								map.get( to.getKey() ).put(fi,  Long.MIN_VALUE);
							}
						}
					}
				}
			}
			
			{
				String path = getSettings().getTemplatesDir();
				File dataDir = new File(path);
				
				if (dataDir.exists() && dataDir.isDirectory()) {
					File files[] = dataDir.listFiles();
					for (File fi:files) {
						if (!fi.isDirectory()) {
							if ( fi.getName().endsWith(".ftl") && fi.getName().startsWith(key)) {	
								map.get( to.getKey() ).put(fi,  Long.MIN_VALUE);
							}
						}
					}
				}
			}
		}

		/**
		{
			map.put("templates", new HashMap<File, Long>());
		
			String path = getSettings().getTemplatesDir();
			File dataDir = new File(path);
			
			if (dataDir.exists() && dataDir.isDirectory()) {
				File files[] = dataDir.listFiles();
				for (File fi:files) {
					if (!fi.isDirectory()) {
						if ( fi.getName().endsWith(".ftl")) {	
							map.get( "templates").put(fi,  Long.MIN_VALUE);
						}
					}
				}
			}
		}**/

		
		startupLogger.info("Starting -> " + ExportAgent.class.getSimpleName());
		
		this.agent = new ExportAgent(getSettings().getScanFreqMillisecs()) {
			@Override
			public void execute() {
				try {
					
				SiteExportService.this.processExport("viamonte2024");
				SiteExportService.this.processExport("cubab2024");
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


	/**
	 * 
	 * @param key
	 * @return
	 */
	protected boolean requiresUpdate(String key) {
		
		Tournament to = tm.getTourmanent(key);
		
		{
			List<File> list = new ArrayList<File>();
			{
				
				String path = getSettings().getTournamentDataDir(to.getKey());
				File dataDir = new File(path);
				File files[] = dataDir.listFiles();
				for (File fi:files) {
					if (!fi.isDirectory()) {
						if (fi.getName().endsWith(".csv") || fi.getName().endsWith(".txt") || fi.getName().endsWith(".info")  || fi.getName().endsWith(".html")) {
							list.add(fi);
						}
					}
				}
			}
			
			for (File file:list) {
				if (!map.get(key).containsKey(file)) {
					map.get(key).put(file,  Long.MIN_VALUE);
					logger.debug("File causing update -> " + file.getName());
					return true;
				}
				else {
					long modified = file.lastModified();
					if (map.get(key).get(file).longValue() < modified) {
						logger.debug("File causing update -> " + file.getName());
						return true;
					}
				}
			}
			
			for (File file: map.get(key).keySet()) {
				if (!list.contains(file)) {
					logger.debug("File causing update -> " + file.getName());
					map.get(key).remove(file);
					return true;
				}
			}
		}
		

	
		/**
	{
		List<File> list = new ArrayList<File>();

		{
			String path = getSettings().getTemplatesDir();
			File dataDir = new File(path);
			File files[] = dataDir.listFiles();
			for (File fi:files) {
				if (!fi.isDirectory()) {
					if (fi.getName().endsWith(".ftl")) {
						list.add(fi);
					}
				}
			}
			
			
			String tem="templates";
			for (File file:list) {
				if (!map.get(tem).containsKey(file)) {
					map.get(tem).put(file,  Long.MIN_VALUE);
					logger.debug("File causing update -> " + file.getName());
					return true;
				}
				else {
					long modified = file.lastModified();
					if (map.get(tem).get(file).longValue() < modified) {
						logger.debug("File causing update -> " + file.getName());
						return true;
					}
				}
			}
			
			for (File file: map.get(tem).keySet()) {
				if (!list.contains(file)) {
					logger.debug("File causing update -> " + file.getName());
					map.get(tem).remove(file);
					return true;
				}
			}

		}
	}
	*/
		

	return false;
}

	
	/**
	 * 
	 * 
	 */
	protected synchronized void processExport(String key) {
		
		
		if (!requiresUpdate(key)) {
			logger.debug("Update not required -> " + key);
			return;
		}
		
		logger.debug("About to execute import - export for -> " + key);
		
		this.tm.getTourmanent(key).execute();
		
		logger.debug("done.");
		
		final Long now = Long.valueOf(System.currentTimeMillis());
		
		{
		List<File> files = new ArrayList<File>();
		map.get(key).keySet().forEach(f -> files.add(f));
		files.forEach(f -> map.get(key).put(f, now));
		}
		
		//{
		//	List<File> files = new ArrayList<File>();
		//	map.get("templates").keySet().forEach(f -> files.add(f));
		//	files.forEach(f -> map.get("templates").put(f, now));
		//}
	}
	
	
	public SettingsService getSettings() {
		return settings;
	}

	public void setSettings(SettingsService settings) {
		this.settings = settings;
	}
	
	public HTMLExportService getHtmlExportService() {
		return htmlExportService;
	}

	public void setHtmlExportService(HTMLExportService htmlExportService) {
		this.htmlExportService = htmlExportService;
	}
	
}
