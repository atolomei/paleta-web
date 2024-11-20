package io.paletaweb.exporter;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.model.JsonObject;
import io.paletaweb.club.tournament.TorneoCuba;
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
	protected TorneoCuba torneo;
	

	@Autowired
	@JsonIgnore
	HTMLExportService htmlExportService;

	
	
	public  BaseExporter() {
	
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
