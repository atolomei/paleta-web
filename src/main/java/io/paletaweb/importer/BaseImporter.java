package io.paletaweb.importer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.util.Check;
import io.paletaweb.club.tournament.TorneoCuba;
import io.paletaweb.service.SettingsService;


@Component
@Scope("prototype")
public class BaseImporter {
	
	static private Logger logger = Logger.getLogger(BaseImporter.class.getName());
	

	@Autowired
	@JsonIgnore
	protected SettingsService settings;
	
	@Autowired
	@JsonIgnore
	protected TorneoCuba torneo;
	
	
	private final String sourceFile;
	
	

	public  BaseImporter(String sourceFile) {
		Check.requireNonNullStringArgument(sourceFile, "sourceFile is null");
		this.sourceFile=sourceFile;
	}
	
	
	public String getSourceFile() {
		return sourceFile;
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


	
	
}
