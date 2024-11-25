package io.paletaweb.exporter;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.model.JsonObject;
import io.paleta.util.Check;
import io.paletaweb.club.tournament.TorneoCuba;
import io.paletaweb.club.tournament.TournamentManager;
import io.paletaweb.club.tournament.Tournament;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;

public class BaseExporter extends JsonObject {

	static private Logger logger = Logger.getLogger(BaseExporter.class.getName());

	static final public DateTimeFormatter full_spa = DateTimeFormatter.ofPattern("dd MMM YYYY HH:mm", Locale.forLanguageTag("es"));

	@Autowired
	@JsonIgnore
	protected SettingsService settings;
	
	@Autowired
	@JsonIgnore
	protected TournamentManager tourmanentManager;
	

	@Autowired
	@JsonIgnore
	HTMLExportService htmlExportService;

	private final  String tournamentDir;
	
	
	private final String key;
	
	private final String destFile;
	private final String templateFile;
	
	public  BaseExporter(String tournamentDir, String destFile, String templateFile) {
		
		Check.requireNonNullStringArgument(destFile, "destFile is null");
		Check.requireNonNullStringArgument(tournamentDir, "tournamentDir is null");
		
		this.templateFile=templateFile;
		this.destFile=destFile;
		this.tournamentDir=tournamentDir;
		this.key=tournamentDir;
	}
	
	
	public String getTemplateFile() {
		return this.templateFile;
	}
	
	public Tournament getTournament() {
		return getTourmanentManager().getTourmanent(getKey());
	}
	
	
	public String getTournamentDirectory() {
		return this.tournamentDir;
	}
	
	public SettingsService getSettings() {
		return settings;
	}

	public void setSettings(SettingsService settings) {
		this.settings = settings;
	}

	public String getDestFile() {
		return destFile;
	}
	
	//public Tournament getTournament() {
	//	return tourmanentManager.getTourmanent("torneos"+ File.separator + "viamonte2024");
	//}


	public TournamentManager getTourmanentManager() {
		return tourmanentManager;
	}


	public void setTourmanentManager(TournamentManager tourmanentManager) {
		this.tourmanentManager = tourmanentManager;
	}


	public HTMLExportService getHtmlExportService() {
		return htmlExportService;
	}

	public void setHtmlExportService(HTMLExportService htmlExportService) {
		this.htmlExportService = htmlExportService;
	}
	
	public String getKey() {
		return key;
	}


}
