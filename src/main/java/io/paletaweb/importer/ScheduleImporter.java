package io.paletaweb.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
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

import io.paleta.logging.Logger;
import io.paleta.model.Match;
import io.paleta.model.MatchResult;
import io.paleta.model.PaletaSet;
import io.paleta.model.Schedule;
import io.paleta.model.Team;
import io.paleta.model.TournamentGroup;
import io.paleta.util.Check;
import io.paletaweb.torneo.TorneoCuba;

@Component
@Scope("prototype")
public class ScheduleImporter extends BaseImporter {
			
	static private Logger logger = Logger.getLogger(ScheduleImporter.class.getName());
	
	static int DATE 	= 0;
	static int HOUR 	= 1;
	static int GROUP 	= 2;
	static int LOCAL 	= 3;
	static int VISITOR 	= 4;
	
	static int RESULT 	= 5;
	static int SET_1	= 6;
	static int SET_2 	= 7;
	static int SET_3 	= 8;
	static int SET_4 	= 9;
	static int SET_5 	= 10;
	
	static  final int NA = -1;
	static  final int CLASI = 0;
	static  final int SEMI = 1;
	static  final int FINAL = 2;
	

	
	private int state = CLASI;
	
	protected String src;
	protected Schedule schedule;
	
	public ScheduleImporter(String src) {
		Check.requireNonNullStringArgument(src, "src is null");
		this.src=src;
	}
	
	public String getSource() {
		return src;
	}
	
	private String parseHour(String str) {
		
		if (str==null)
			return "";
		
		if (str.length()==0)
			return "";
		
		return str.trim();
	}

	private String parseDate(String str) {
		
		if (str==null)
			return "";
		
		if (str.length()==0)
			return "";
		
		return str.trim();
	}

	
	private Schedule getSchedule() {
		return this.schedule;
	}

	
	private void setState( int state) {
		this.state=state;
	}
	
	private int getState() {
		return state;
	}
	
	
	
	public Schedule execute() throws IOException {
		
		
		File f = new File( getSettings().getDataDir() + File.separator + getSource());
		
		if (!f.exists())
			return null;
		
		List<List<String>> records;
		
		
		this.schedule = new Schedule();
		
		
		try (Stream<String> lines = Files.lines(Paths.get(getSettings().getDataDir() + File.separator + getSource()))) {
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
			
					if (li.size()<=VISITOR) {
						throw new IllegalArgumentException(" invalid line -> " + li.toString() + " | (items: " + li.size()+" and must be :" + String.valueOf(VISITOR+1) +") ");
					}
					
					Match match = new Match();
					
					match.setMatchDate				(parseDate(li.get(DATE)));
					match.setMatchHour				(parseHour(li.get(HOUR)));
					
					if (getState()==CLASI) {
						match.setTournamentZone		(parseGroup(li.get(GROUP)));
						match.setLocal					(parseTeam (match.getTournamentZone(), li.get(LOCAL)));
						match.setVisitor				(parseTeam (match.getTournamentZone(), li.get(VISITOR)));
					}
					else {
						match.setLocal					(parseTeam (li.get(LOCAL)));
						match.setVisitor				(parseTeam (li.get(VISITOR)));
					}
			
		
						
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
									match.setMatchResult(MatchResult.TIE);
							}
					}
					else {
						match.setCompleted(false);
					}

					if (getState()==CLASI)
						getSchedule().addMatchClasificacion(match);
					else if (getState()==SEMI) {
						getSchedule().addMatchSemifinal(match);
						
					}
					else if (getState()==FINAL) {
						getSchedule().setMatchFinal(match);
					}
					else
						throw new IllegalArgumentException(" invalid state -> " + li.toString() + String.valueOf(getState()));
			}
		});
		
		
		String validate = validate();
		if (!validate.equals("ok")) {
			throw new IllegalStateException(validate);
		}
		else
			logger.debug("validate -> ok");
		
		
		
		
		schedule.getMatchesClasificacion().sort(new Comparator<Match>() {

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
		
		
		
		boolean first = true;
		String currDate = null;
		
		for (Match ma:schedule.getMatchesClasificacion()) {
			if (!first) {
				if ( (currDate!=null) && (ma.getMatchDateStr()!=null) ) {
					if (!currDate.equals(ma.getMatchDateStr())) {
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
			currDate = ma.getMatchDateStr();
		}
		
		//for (Match ma:schedule.getMatches()) {
		//	logger.debug(ma.getMatchDateStr() + " " + String.valueOf(ma.daybreak)); 
		//}
		
		return schedule;
	}
	
	
	
	private Team parseTeam(String str) {
		
		String s = str.toLowerCase().trim();
		for (TournamentGroup zone: getTorneo().getTournamentGroups()) {
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
		for (TournamentGroup z: getTorneo().getTournamentGroups()) {
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
	
	
	private String validate() {
		
		if (this.schedule==null)
			return "schedule is null";
		
		Map<Team, Integer> teams = new  HashMap<Team, Integer>();
		
		for (Match ma: getSchedule().getMatchesClasificacion()) {

			if (ma.local==null || ma.visitor==null)
				return "local or visitor is null";
			
			if (!teams.containsKey(ma.local)) 
				teams.put(ma.local, Integer.valueOf(0));
					
			if (!teams.containsKey(ma.visitor)) 
				teams.put(ma.visitor, Integer.valueOf(0));
			
			
			teams.put( ma.local, Integer.valueOf(teams.get(ma.local)+1));
			teams.put( ma.visitor, Integer.valueOf(teams.get(ma.visitor)+1));

		}
		
		Integer total = null;
		
		for (Team t: teams.keySet()) {
			if (total==null)
				total=Integer.valueOf(teams.get(t));
			
			//logger.debug(t.getName() + " -> " + String.valueOf( teams.get(t)));
			
			if (!total.equals(teams.get(t))) {
				return "Error | " + t.getName() + " has " + String.valueOf( teams.get(t)) + " and total is " + total.toString();
			}
		}
		
		return "ok";
	}




}
