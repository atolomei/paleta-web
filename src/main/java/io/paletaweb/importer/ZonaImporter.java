package io.paletaweb.importer;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import io.paleta.model.Team;
import io.paleta.model.TournamentGroup;
import io.paleta.util.Check;


@Component
@Scope("prototype")
public class ZonaImporter extends BaseImporter {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ZonaImporter.class.getName());
	
	private final String sourceFile;
	
	private String name;
	
	private List<Team> teams;
	
	@JsonIgnore
	private TournamentGroup zona;

	public  ZonaImporter(String sourceFile, String name) {
		Check.requireNonNullStringArgument(sourceFile, "sourceFile is null");
		this.sourceFile=sourceFile;
		this.name=name;
	}
	
	public TournamentGroup execute() throws IOException {
		
		this.teams = new ArrayList<Team>();
		
		List<List<String>> records;
																	
		try (Stream<String> lines = Files.lines(Paths.get(getSettings().getDataDir() + File.separator + getSourceFile()))) {
			records = lines.filter(line -> (!line.startsWith("#")) && (!line.isBlank()))
						   .map(line -> Arrays.asList(line.split(",")))
					       .collect(Collectors.toList());
		}
		
		records.forEach( li ->  {
			
			if (li.size()>1) {
				List<String> list = new ArrayList<String>();
				for (int n=1; n<li.size();n++) {
					list.add(li.get(n));
				}
				addTeam(new Team(li.get(0).trim(), list));
			
			}
			else {
				addTeam(new Team(li.get(0).trim()));
			}
				
			
		});
		
		
		getTeams().sort(new Comparator<Team>() {
			@Override
			public int compare(Team o1, Team o2) {
				try {
					return o1.getName().compareToIgnoreCase(o2.getName());
				} catch (Exception e) {
					return 0;	
				}
				
			}
			
		});
		this.zona = new TournamentGroup( getName(), getTeams());
		return zona;
	}

	private String getName() {
		return name;
	}

	private List<Team> getTeams() {
		return teams;
	}

	private void addTeam(Team team) {
		teams.add(team);
		
	}

	public String getSourceFile() {
		return sourceFile;
	}
}


//teams.forEach(i->logger.debug(i));

//if ((!li.isEmpty()) && (li.get(0).length()>0))