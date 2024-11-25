package io.paletaweb.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import io.paleta.model.schedule.Schedule;
import io.paleta.util.Check;
import io.paletaweb.club.tournament.TorneoCuba;
import io.paletaweb.service.SettingsService;

@Component
@Scope("prototype")
public class ResultadoImporter extends BaseImporter {
	
		static private Logger logger = Logger.getLogger(ResultadoImporter.class.getName());
		

		static int ID	 	= 0;
		static int GROUP 	= 1;
		static int LOCAL 	= 2;
		static int VISITOR 	= 3;
		static int RESULT 	= 4;
		static int SET_1	= 5;
		static int SET_2 	= 6;
		static int SET_3 	= 7;
		static int SET_4 	= 8;
		static int SET_5 	= 9;

	
	@Autowired
	@JsonIgnore
	SettingsService settings;
	
	
	
	long matchId=0;
	
	static  final int NA = -1;
	static  final int CLASI = 0;
	static  final int SEMI = 1;
	static  final int FINAL = 2;
	
	private int state = CLASI;

	
	//List<Match> matches;
	
	
	public  ResultadoImporter(String key, String sourceFile) {
		super(key, sourceFile);
	}
	
		
	
	
	public void execute() throws IOException {
		
		
		//matches = new ArrayList<Match>();
		
		String path = getSettings().getTournamentDataDir( getTournamentDirectory() ) + File.separator + getSourceFile();
		

		File f = new File( path );
		
		if (!f.exists())
			return;
		
		List<List<String>> records;
		
		
		//this.schedule = new Schedule();
		//this.schedule.setFileName(getSourceFile());

		
		try (Stream<String> lines = Files.lines(Paths.get( path ))) {
			records = lines.filter(line -> (!line.startsWith("#")) && (!line.isBlank()))
						   .map(line -> Arrays.asList(line.split(",")))
					       .collect(Collectors.toList());
		}
		
		
		records.forEach( li -> {
			
			if (li.get(0).toLowerCase().startsWith("$clasi")) {
				setState(CLASI);
			}
			else if (li.get(0).toLowerCase().startsWith("$semi")) {
				setState(SEMI);
			}
			else if (li.get(0).toLowerCase().startsWith("$final")) {
				setState(FINAL);
			}
		
			else {
			
					if (li.size()<=VISITOR)
						throw new IllegalArgumentException(" invalid line -> " + li.toString() + " | (items: " + li.size()+" and must be :" + String.valueOf(VISITOR+1) +") ");
					
					
					//Match match = new Match(Long.valueOf(li.get(ID)));
					
					
					Match match = null;
					
					if (getState()==CLASI) {
						for (Match m: getTournament().getSchedule().getMatchesClasificacion()) {
								if (m.getId()==Long.valueOf(li.get(ID).trim()).longValue()) {
									match=m;
									break;
								}
						}
					}
					else {
						match = new Match( matchId++);
					}
						
					if (getState()==CLASI) {
						match.setTournamentGroup( parseGroup(li.get(GROUP)) );
					}
					
			
					match.setLocal					(parseTeam (match.getTournamentGroup(), li.get(LOCAL)));
					match.setVisitor				(parseTeam (match.getTournamentGroup(), li.get(VISITOR)));
					
					if (li.size()>RESULT && (!li.get(RESULT).isBlank())) {
							
						boolean isResult = false;
						
						int setLocal = 0;
						int setVisitor = 0;
						
						try {
							MatchResult res=parseMatchResult(li.get(RESULT));
							match.setMatchResult(res);
							isResult=true;
						}
						catch (NumberFormatException e) {
							isResult=false;
						}
						catch (Exception e) {
							throw (e);
						}
						
							match.setCompleted(true);
							
							int n = SET_1;
							
							List<PaletaSet> sets = new ArrayList<PaletaSet>();
							
							while (n<li.size()) {
									PaletaSet set=parsePaletaSet(li.get(n));
									set.completado=true;
									if (set.isLocalWinner()) {
										setLocal += 1;
									}
									else {
										setVisitor += 1;
									}
									sets.add(set);
									n++;
							}
							match.setSets(sets);
							
							if (!isResult) {
								if (setLocal>setVisitor)
									match.setMatchResult(MatchResult.LOCAL);
								else if (setLocal<setVisitor)
									match.setMatchResult(MatchResult.VISITOR);
								else
									match.setMatchResult(MatchResult.DRAW);
							}
					}
					else {
						match.setCompleted(false);
					}
					
					
					//matches.add(match);

					//if (getState()==CLASI)
					//	getSchedule().addMatchClasificacion(match);
					//else if (getState()==SEMI) {
					//	getSchedule().addMatchSemifinal(match);
					//	
					//}
					//else if (getState()==FINAL) {
					//	getSchedule().setMatchFinal(match);
					//}
					//else
					//	throw new IllegalArgumentException(" invalid state -> " + li.toString() + String.valueOf(getState()));
			}
		});
		
		
		
		/**
		matches.sort(new Comparator<Match>() {

			@Override
			public int compare(Match o1, Match o2) {
				
				try {
				if (o1.getDate()==null) {
					if (o2.getDate()==null) {
						return 0;
					}
					return -1;
				}
				else if (o2.getDate()==null) {
					return 1;
				}
				
				if (o2.getDate().isBefore(o1.getDate())) 
					return 1;
				
				if (o1.getDate().isBefore(o2.getDate())) 
					return -1;
				
				return o1.getLocal().getName().compareToIgnoreCase(o2.getLocal().getName());
				} catch (Exception e) {
					logger.error(e);
					return 0;
				}
				
			}
		});
		**/
		

		/**
		boolean first = true;
		int currDate = -1;
		
		for (Match ma:schedule.getMatchesClasificacion()) {
			if (!first) {
				if (ma.getDate()!=null) {
					if (currDate!=ma.getDate().getDayOfYear()) {
						ma.daybreak=Integer.valueOf(1);
					}
					else
						ma.daybreak=Integer.valueOf(-1);
				}
			}
			else {
				ma.daybreak =Integer.valueOf(-1);
			}
			
			first = false;
			currDate = ma.getDate().getDayOfYear();
		}
		
		//for (Match ma:schedule.getMatches()) {
		//	logger.debug(ma.getMatchDateStr() + " " + String.valueOf(ma.daybreak)); 
		//}
		
		return schedule;
		*/
		//return matches;
	}


	
	private Team parseTeam(String str) {
		
		String s = str.toLowerCase().trim();
		for (TournamentGroup zone: getTournament().getTournamentGroups()) {
			for (Team te:zone.getTeams()) {
				if (te.getName().toLowerCase().trim().equals(s)) 
					return te;
			}
		}
		throw new IllegalArgumentException(" team does not exist -> " + str);
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
		for (TournamentGroup z: getTournament().getTournamentGroups()) {
			if (z.getName().toLowerCase().trim().equals(s))
				return z;
		}
		throw new IllegalArgumentException(" group does not exist -> " + str);
	}


	
	private MatchResult parseMatchResult(String str) {
		return MatchResult.fromId(str);
	}
	
	
	private PaletaSet parsePaletaSet(String str) {

		Check.requireNonNullStringArgument(str, "str is null");
		
		String val[]=str.trim().split("-");
		if (val.length!=2)
			throw new IllegalArgumentException("must be of the form  nn-nn, example: 25-12");
		
		PaletaSet pal = new PaletaSet(Integer.valueOf(val[0].trim()), Integer.valueOf(val[1].trim()));
		return pal;
	}

	
	private void setState( int state) {
		this.state=state;
	}
	
	private int getState() {
		return state;
	}	

}
