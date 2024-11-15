package io.paletaweb.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ZonasImporter extends BaseImporter {

	static final int GROUP = 0;
	static final int NAME  = 1;
	static final int PLAYERS  = 2;
	
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ZonaImporter.class.getName());
	
	private final String sourceFile;
	
	private List<TournamentGroup> groups;
	
	
	

	@JsonIgnore
	private Map<String, TournamentGroup> mg;
	
	
	public  ZonasImporter(String sourceFile) {
		Check.requireNonNullStringArgument(sourceFile, "sourceFile is null");
		this.sourceFile=sourceFile;
	}

	
	public List<TournamentGroup> execute() throws IOException {
		
		List<List<String>> records = null;
		
		try (Stream<String> lines = Files.lines(Paths.get(getSettings().getDataDir() + File.separator + getSourceFile()))) {
			records = lines.filter(line -> (!line.startsWith("#")) && (!line.isBlank()))
						   .map(line -> Arrays.asList(line.split(",")))
					       .collect(Collectors.toList());
		}
		
		this.groups = new ArrayList<TournamentGroup>();
		this.mg = new HashMap<String, TournamentGroup>();
		

		/** group, name, players */
		
		records.forEach( li ->  {
			if (li.size()>GROUP) {
				List<String> players = new ArrayList<String>();
				if (li.size()>NAME) {
					for (int n=PLAYERS; n<li.size();n++) {
						players.add(li.get(n).trim());
					}
				}
				
				
				String groupName = li.get(GROUP).trim();
				
				String name = li.get(NAME).trim();
				String key = keyFromName(name);
				
				TournamentGroup group = getGroupByName(groupName);
				
				group.addTeam(new Team(key, name, players));
			}
		});
		
		getMapGroups().values().forEach( group -> {
			if (group.getTeams()!=null) {
				group.getTeams().sort(new Comparator<Team>() {
					@Override
					public int compare(Team o1, Team o2) {
						try {
							return o1.getName().compareToIgnoreCase(o2.getName());
						} catch (Exception e) {
							return 0;	
						}
					}
				});	
			}
			getGroups().add(group);
		});
		

		
		if (getGroups()!=null) {
			getGroups().sort(new Comparator<TournamentGroup>() {
				@Override
				public int compare(TournamentGroup o1, TournamentGroup o2) {
					try {
						return o1.getName().compareToIgnoreCase(o2.getName());
					} catch (Exception e) {
						return 0;	
					}
				}
			});	
		}
		
		return getGroups();
	}
	
	
	private String keyFromName(String name) {
		return name.toLowerCase().trim();
	}
	private TournamentGroup getGroupByName(String name) {
		
		Check.requireNonNullStringArgument(name, "name is null");
		
		String key = keyFromName(name); 
		
		if (!getMapGroups().containsKey(key)) {
			getMapGroups().put(key, new TournamentGroup(key, name));
		}
		return getMapGroups().get(key);
		
	}


	
	
	private Map<String, TournamentGroup> getMapGroups() {
		return mg;
	}
	
	private List<TournamentGroup> getGroups() {
		return groups;
	}


	public String getSourceFile() {
		return sourceFile;
	}
}
