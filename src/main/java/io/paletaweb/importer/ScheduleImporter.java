package io.paletaweb.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
import io.paleta.model.Team;
import io.paleta.model.TournamentGroup;
import io.paleta.model.schedule.Schedule;
import io.paleta.util.Check;
import io.paletaweb.torneo.TorneoCuba;

@Component
@Scope("prototype")
public class ScheduleImporter extends BaseImporter {
			
	static private Logger logger = Logger.getLogger(ScheduleImporter.class.getName());
	
	static int ID	 	= 0;
	static int DATE 	= 1;
	static int HOUR 	= 2;
	static int GROUP 	= 3;
	static int LOCAL 	= 4;
	static int VISITOR 	= 5;
	
	static int RESULT 	= 6;
	static int SET_1	= 7;
	static int SET_2 	= 8;
	static int SET_3 	= 9;
	static int SET_4 	= 10;
	static int SET_5 	= 11;
	
	static  final int NA = -1;
	static  final int CLASI = 0;
	static  final int SEMI = 1;
	static  final int FINAL = 2;
	

	
	private int state = CLASI;

	private long matchId = 0;
	
	private Schedule schedule;
	
	public ScheduleImporter(String src) {
		super(src);
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
		
		
		File f = new File(getSettings().getDataDir() + File.separator + getSourceFile());
		
		if (!f.exists())
			return null;
		
		List<List<String>> records;
		
		
		this.schedule = new Schedule();
		
		this.schedule.setFileName(getSourceFile());

		
		try (Stream<String> lines = Files.lines(Paths.get(getSettings().getDataDir() + File.separator + getSourceFile()))) {
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
					
					Match match = new Match(matchId++);
					
					OffsetDateTime date = parseDate(li.get(DATE), li.get(HOUR)); 
					match.setDate(date);
					
					if (getState()==CLASI) {
						match.setTournamentGroup		(parseGroup(li.get(GROUP)));
						match.setLocal					(parseTeam (match.getTournamentGroup(), li.get(LOCAL)));
						match.setVisitor				(parseTeam (match.getTournamentGroup(), li.get(VISITOR)));
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
	}
	
	
	
	private OffsetDateTime parseDate(String dayMonthYear, String hourMin) {
		
		String t_date = dayMonthYear;
		String t_hour = hourMin;
		
		String s_year, s_month, s_day;
		Integer year, month, day;
		
		{
			String arr[] = t_date.trim().replaceAll("/", "-").split("-");
			
			if (arr.length<3)
				s_year = "2024";
			else
				s_year = arr[2].trim();
			
			if (s_year.length()<4)
				s_year="20"+s_year;
			
			
			if (arr.length<2)
				s_month = "12";
			else
				s_month=arr[1].trim();
			
			if (s_month.length()<2)
				s_month="0"+s_month;
			
			if (arr.length<1)
				s_day = "1";
			else
				s_day=arr[0].trim();
			
			if (s_day.length()<2)
				s_day="0"+s_day;
		
			
			try {
				year = Integer.valueOf(s_year.trim());
			} catch (Exception e) {
				logger.error(e);
				year=2024;
			}
			
			try {
				month = Integer.valueOf(s_month.trim());
			} catch (Exception e) {
				logger.error(e);
				month=12;
			}
			
			try {
				day = Integer.valueOf(s_day.trim());
			} catch (Exception e) {
				logger.error(e);
				day=1;
			}
		
		}
		

		String s_hour, s_min, s_sec;
		Integer hour, min, sec;

		String arr[] = t_hour.replaceAll("-", ":").split(":");
		
		if (arr.length<3)
			s_sec = "00";
		else
			s_sec = arr[2];
		
		if (s_sec.length()<2)
			s_sec="0"+s_sec.trim();
		
		
		
		if (arr.length<2)
			s_min = "00";
		else
			s_min=arr[1];
		
		if (s_min.length()<2)
			s_min="0"+s_min.trim();
		
		
		if (arr.length<1)
			s_hour = "19";
		else
			s_hour=arr[0].trim();;
		
		if (s_hour.length()<2)
			s_hour="0"+s_hour.trim();

		
		{
		
			try {
				hour = Integer.valueOf(s_hour.trim());
			} catch (Exception e) {
				logger.error(e);
				hour=19;
			}
			
			try {
				min = Integer.valueOf(s_min.trim());
			} catch (Exception e) {
				logger.error(e);
				min=0;
			}
			
			try {
				sec = Integer.valueOf(s_sec.trim());
			} catch (Exception e) {
				logger.error(e);
				sec=0;
			}		
		}
		

		
		String str_date = s_year +"-" + s_month + "-" + s_day + "T" + s_hour+":" + s_min + ":" + s_sec + "-03:00";
		
		//logger.debug(str_date);
	
		try {
			OffsetDateTime date = OffsetDateTime.parse(str_date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			return date;
		} catch (Exception e) {
			logger.error(e, " | Tried timestamp without GMT. " + str_date);
			return null;
		}
		
		
		
		
		
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
