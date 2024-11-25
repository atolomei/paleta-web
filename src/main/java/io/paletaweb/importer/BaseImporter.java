package io.paletaweb.importer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.util.Check;
import io.paletaweb.club.tournament.TorneoCuba;
import io.paletaweb.club.tournament.TournamentManager;
import io.paletaweb.club.tournament.Tournament;
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
	protected TournamentManager tm;
	
	private final  String tournamentDir;
	
	private final String sourceFile;
	
	private final String key;
	

	

	public  BaseImporter(String tournamentDir, String sourceFile) {
		Check.requireNonNullStringArgument(sourceFile, "sourceFile is null");
		this.sourceFile=sourceFile;
		this.tournamentDir=tournamentDir;
		this.key=tournamentDir;
	}
	
	public Tournament getTournament() {
		return tm.getTourmanent(key);
	}
	
	public String getTournamentDirectory() {
		return this.tournamentDir;
	}
	
	public String getKey() {
		return key;
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

	 


	
	
}
