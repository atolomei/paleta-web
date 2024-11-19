package io.paletaweb.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.model.Match;
import io.paleta.model.MatchResult;
import io.paleta.model.PaletaSet;
import io.paleta.model.Team;
import io.paleta.model.TournamentGroup;
import io.paleta.util.Check;
import io.paletaweb.service.SettingsService;
import io.paletaweb.torneo.TorneoCuba;

@Component
@Scope("prototype")
public class ResultadoImporter {
	
		static private Logger logger = Logger.getLogger(ResultadoImporter.class.getName());
		
		static int ID	 	= 0;
		static int DATE 	= 1;
		static int GROUP 	= 2;
		static int LOCAL 	= 3;
		static int VISITOR 	= 4;
		static int RESULT 	= 5;
		static int SET_1	= 6;
		static int SET_2 	= 7;
		static int SET_3 	= 8;
		static int SET_4 	= 9;
		static int SET_5 	= 10;

	
	private final String sourceFile;
	
	//private final TorneoCuba torneo;
	
	@Autowired
	@JsonIgnore
	SettingsService settings;
	
	@Autowired
	@JsonIgnore
	TorneoCuba torneoCuba;
	
	
	public  ResultadoImporter(String sourceFile) {
		Check.requireNonNullStringArgument(sourceFile, "sourceFile is null");
		this.sourceFile=sourceFile;
		//this.torneo=torneo;
	}
	
		
	long matchId=0;
	
	public List<Match> execute() throws IOException {
		
		
		List<List<String>> records;
		try (Stream<String> lines = Files.lines(Paths.get(getSettings().getDataDir() + File.separator + getSourceFile()))) {
			records = lines.filter(line -> (!line.startsWith("#")) && (!line.isBlank()))
						   .map(line -> Arrays.asList(line.split(",")))
					       .collect(Collectors.toList());
		}
		

		List<Match> partidos =  new ArrayList<Match>();
		
		records.forEach( li -> {
			
			Match match = new Match(matchId++);
			
			if (li.size()>SET_1) {
				
				match.setDate				(parseDate(li.get(DATE)));
				match.setTournamentGroup		(parseGroup(li.get(GROUP)));
				
				match.setLocal				(parseTeam (match.getTournamentGroup(), li.get(LOCAL)));
				match.setVisitor			(parseTeam (match.getTournamentGroup(), li.get(VISITOR)));
				
				if (li.size()>RESULT) {

					match.setMatchResult  (parseMatchResult(li.get(RESULT)));
					match.setCompleted(true);
				
					int n = SET_1;
					List<PaletaSet> sets = new ArrayList<PaletaSet>();
					
					while (n<li.size()) {
							sets.add( parsePaletaSet(li.get(n)));
							n++;
					}
					match.setSets(sets);
				}
				else
				{
					match.setCompleted(false);
				}
				partidos.add(match);
			}
		});
		
		//partidos.forEach(i ->logger.debug(i.toString()));
		return partidos;
		
	}


	
	private PaletaSet parsePaletaSet(String str) {

		Check.requireNonNullStringArgument(str, "str is null");
		
		String val[]=str.split("-");
		if (val.length!=2)
			throw new IllegalArgumentException("must be of the form  nn-nn, example: 25-12");
		
		PaletaSet pal = new PaletaSet(Integer.valueOf(val[0].trim()), Integer.valueOf(val[1].trim()));
		return pal;
	}




	private MatchResult parseMatchResult(String str) {
		return MatchResult.fromId(str);
	} 


	private Team parseTeam(TournamentGroup zone, String str) {
		
		Check.requireNonNullArgument(zone, "zone is null");
		Check.requireNonNullStringArgument(str, "str is null");
		
		String s = str.toLowerCase().trim();
		for (Team te:zone.getTeams()) {
			if (te.getName().toLowerCase().trim().equals(s)) 
				return te;
		}
		throw new IllegalArgumentException(" team does not exist in zone " + zone.getName() + " -> " + str);
	}


	private TournamentGroup parseGroup(String str) {

		Check.requireNonNullStringArgument(str, "str is null");
		
		String s = str.toLowerCase().trim();
		for (TournamentGroup z: getTorneoCuba().getTournamentGroups()) {
			if (z.getName().toLowerCase().trim().equals(s))
				return z;
		}
		throw new IllegalArgumentException(" zone does not exist -> " + str);
	}


	private TorneoCuba getTorneoCuba() {
		return torneoCuba;
	}


	private OffsetDateTime parseDate(String str) {
		
		if (str==null)
			return OffsetDateTime.now();
		
		if (str.length()==0)
			return OffsetDateTime.now();
		
		if (str.toLowerCase().trim().equals("today"))
			return OffsetDateTime.now();
		
		return OffsetDateTime.now();
		
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
