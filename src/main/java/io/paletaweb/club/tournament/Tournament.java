package io.paletaweb.club.tournament;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fasterxml.jackson.annotation.JsonIgnore;

import freemarker.template.TemplateException;
import io.paleta.logging.Logger;
import io.paleta.model.Alert;
import io.paleta.model.Contact;
import io.paleta.model.Match;
import io.paleta.model.MatchResult;
import io.paleta.model.Meta;
import io.paleta.model.Team;
import io.paleta.model.TournamentGroup;
import io.paleta.model.TournamentGroupTable;
import io.paleta.model.club.ClubTournament;
import io.paleta.model.club.TournamentBuildStatus;
import io.paleta.model.club.TournamentStatus;
import io.paleta.model.schedule.RoundRobinGenerator;
import io.paleta.model.schedule.Schedule;
import io.paleta.model.schedule.ScheduleMatchDate;
import io.paleta.model.schedule.SchedulePlanner;
import io.paletaweb.exporter.IndexExporter;
import io.paletaweb.exporter.ScheduleCSVExporter;
import io.paletaweb.exporter.ScheduleResultsExporter;
import io.paletaweb.exporter.TeamsExporter;
import io.paletaweb.importer.AlertImporter;
import io.paletaweb.importer.ContactImporter;
import io.paletaweb.importer.MetaImporter;
import io.paletaweb.importer.ResultadoImporter;
import io.paletaweb.importer.ScheduleImporter;
import io.paletaweb.importer.ZonasImporter;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;

public abstract class Tournament implements ApplicationContextAware  {
			
	static private Logger logger = Logger.getLogger(Tournament.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	static final String f_scheduleCSV  = "schedule.csv";
	static final String f_scheduleINFO = "schedule.info";
	
	static final String f_torneo_properties = "torneo.properties";
	static final String f_contacts = "contacts.csv";
	static final String f_alert = "alert.txt";
	static final String f_zones = "zonas.csv";

	private Boolean printCalendarSchedule = Boolean.valueOf(true);
	private Boolean printRawSchedule = Boolean.valueOf(false);
	
	private  ClubTournament clubTournament;
	
	private  TournamentBuildStatus tournamentBuildStatus = TournamentBuildStatus.STARTING;
	
	
	@Autowired
	@JsonIgnore
	SettingsService settings;
	
	@Autowired
	@JsonIgnore
	HTMLExportService htmlExportService;
	
	@Autowired
	@JsonIgnore
	private ApplicationContext applicationContext;


	public Tournament() {
		clubTournament = new ClubTournament();
	}
	
	public Tournament(String key, String name) {
		clubTournament = new ClubTournament(name, key);
	}

	
	
	
	public void execute() {
		
		/** Import */
		importMeta();
		importAlert();
		importContacts();
		importZones();
		
		if (isScheduleResults())
			setTournamentBuildStatus(TournamentBuildStatus.LOADING_RESULTS);
		else if (!isScheduleCSV()) 
			setTournamentBuildStatus(TournamentBuildStatus.GENERATING_SCHEDULE);
		else
			setTournamentBuildStatus(TournamentBuildStatus.STARTING);
		
		
		if (this.getTournamentBuildStatus()==TournamentBuildStatus.STARTING || 
		    this.getTournamentBuildStatus()==TournamentBuildStatus.GENERATING_SCHEDULE) {
			
			if (isScheduleCSV()) {
				logger.debug("Importing -> schedule.csv");
				importSchedule();
				List<ScheduleMatchDate> list = new ArrayList<ScheduleMatchDate>();
				this.getSchedule().getMatchesClasificacion().forEach( m -> {
					list.add(new ScheduleMatchDate( m.getId(), m.getDate(), m)); }
				);
				setTournamentBuildStatus(TournamentBuildStatus.GENERATING_SCHEDULE);
				
				logger.debug("Generating Schedule results to -> schedule-results.info");
				exportScheduleResults(list);
				setTournamentBuildStatus(TournamentBuildStatus.LOADING_RESULTS);
			}
			else {
				
				logger.debug("schedule.csv not found - Generating Round Robin");
				
				RoundRobinGenerator roundRobin = new RoundRobinGenerator(this.getTournamentGroups());
				List<ScheduleMatchDate> dates = roundRobin.execute();
				List<Match> matches = new ArrayList<Match>();
				dates.forEach( r -> matches.add(r.getMacth()));
				
				setSchedule(new Schedule("schedule.csv", matches));
				exportScheduleCSV();
				setTournamentBuildStatus(TournamentBuildStatus.LOADING_RESULTS);
				
				
				if (this.isPrintCalendarSchedule()) {
					SchedulePlanner planner = new SchedulePlanner(dates, getSchedule().getMatchesClasificacion()); 
					List<ScheduleMatchDate> list = planner.execute();
					exportScheduleResults(list);	
				}
				else {
					
				}
			}
		}

		if (this.getTournamentBuildStatus()==TournamentBuildStatus.LOADING_RESULTS) {
			importSchedule();
			importResultados("resultados.csv");	
		}
				
				
		/** now import results from schedule.info */
		if (this.isPrintCalendarSchedule()) {
			importSchedule(f_scheduleINFO);
		}
		
		
		
		for (TournamentGroup group: getTournamentGroups()) {
			group.setMatches(getSchedule().getMatchesClasificacion().stream().filter( m -> m.getTournamentGroup().equals(group)).toList());
		}
		
		
		
		
		// --------------------------------------------------------
		
		/** calculate */
		this.getClubTournament().calculateTables();
		
		
		/** export */
		exportIndex("index.ftl", "index.html");
		exportTeams("teams.ftl", "equipos.html");
		
		startupLogger.debug("---------------------------------------------------------");
		startupLogger.debug("Index -> exported");
		startupLogger.debug("---------------------------------------------------------");
		
		startupLogger.info("done");
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public ClubTournament getClubTournament() {
		return clubTournament;
	}

	public void setTournament(ClubTournament tournament) {
		this.clubTournament = tournament;
	}


	public void setName( String name) {
		this.getClubTournament().setName(name);
	}

	
	public void setTournamentGroups(List<TournamentGroup> list) {
		this.getClubTournament().setTournamentGroups(list);
	}
	
	public List<Match> getMatches() {
		return this.getClubTournament().getMatches();
	}

	public void setMatches(List<Match> matches) {
		this.getClubTournament().setMatches(matches);
	}

	public void setSchedule(Schedule s) {
		this.getClubTournament().setSchedule(s);	
	}
	
	public String getKey() {
		return getClubTournament().getKey();
	}

	public void setKey(String key) {
		getClubTournament().setKey(key);
	}

	public List<Team> getTeams() {
		return getClubTournament().getTeams();
	}

	
	public List<TournamentGroupTable> getGroupTableList() {
		return getClubTournament(). getGroupTableList();
	}
	public String getName() {
		return this.getClubTournament().getName();
	}
	
	public List<TournamentGroup> getTournamentGroups() {
		return this.getClubTournament().getTournamentGroups();
	}
	
	public TournamentStatus getState() {
		return this.getClubTournament().getTournamentStatus();
	}
	
	public Team getWinner() {
		return this.getClubTournament().getWinner();
	}

	public Map<TournamentGroup, TournamentGroupTable> getGroupTables() {
		return this.getClubTournament().getGroupTables();
	}

	public void setGroupTables(Map<TournamentGroup, TournamentGroupTable> groupTables) {
		this.getClubTournament().setGroupTables(groupTables);
	}

	public boolean isPrintRawSchedule() {
		return printRawSchedule;
	}

	public void setPrintRawSchedule(boolean printRawSchedule) {
		this.printRawSchedule = printRawSchedule;
	}

	public Boolean isPrintCalendarSchedule() {
		return printCalendarSchedule;
	}

	public void setPrintCalendarSchedule(Boolean printCalendarSchedule) {
		this.printCalendarSchedule = printCalendarSchedule;
	}
	public SettingsService getSettings() {
		return settings;
	}

	public void setSettings(SettingsService settings) {
		this.settings = settings;
	}

	
	public ApplicationContext getApplicationContext()  {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		 this.applicationContext = applicationContext;
	}


	public HTMLExportService getHtmlExportService() {
		return htmlExportService;
	}

	public void setHtmlExportService(HTMLExportService htmlExportService) {
		this.htmlExportService = htmlExportService;
	}

	public OffsetDateTime getStartDate() {
		return this.getClubTournament().getStartDate();
	}

	public void setStartDate(OffsetDateTime startDate) {
		this.getClubTournament().setStartDate(startDate);
	}

	public OffsetDateTime getEndDate() {
		return this.getClubTournament().getEndDate();
	}

	public void setEndDate(OffsetDateTime endDate) {
		this.getClubTournament().setEndDate(endDate);
	}

	public Alert getAlert() {
		return this.getClubTournament().getAlert();
	}

	public void setAlert(Alert alert) {
		this.getClubTournament().setAlert(alert);
	}

	public List<Contact> getContacts() {
		return this.getClubTournament().getContacts();
	}

	public void setContacts(List<Contact> contacts) {
		this.getClubTournament().setContacts(contacts);
	}

	public String getBanner() {
		return this.getClubTournament().getBanner();
	}

	public void setBanner(String banner) {
		this.getClubTournament().setBanner(banner);
	}

	public Meta getMeta() {
		return this.getClubTournament().getMeta();
	}

	public void setMeta(Meta meta) {
		this.getClubTournament().setMeta(meta);
	}

	
	public Schedule getSchedule() {
		return this.getClubTournament().getSchedule();
	}
	
	public void setWinner(Team winner) {
		this.getClubTournament().setWinner(winner);
	}

	
	public void setState(TournamentStatus state) {
		this.getClubTournament().setState(state);
	}
	

	public TournamentBuildStatus getTournamentBuildStatus() {
		return tournamentBuildStatus;
	}

	public void setTournamentBuildStatus(TournamentBuildStatus tournamentBuildStatus) {
		this.tournamentBuildStatus = tournamentBuildStatus;
	}
	
	
	protected void importResultados(String fileName) {
		ResultadoImporter ai = getApplicationContext().getBean(ResultadoImporter.class, getKey(), fileName);
		
		
		try {
			
			ai.execute();
		
			/**
			getSchedule().getMatchesClasificacion().forEach( m -> {
				
				for (Match t: matches) {
					if (t.getLocal().equals(m.getLocal()) && t.getVisitor().equals(m.getVisitor()))  
						 {
							m.setResult(t.getMatchResult());
							m.setSets(t.getSets());
						 }
					else if (t.getLocal().equals(m.getVisitor()) && t.getVisitor().equals(m.getLocal())) {
						m.setLocal(t.getLocal());
						m.setVisitor(t.getVisitor());
						m.setResult(t.getMatchResult());
						m.setSets(t.getSets());
					}
				}
			});
			**/
			
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}
	
	
	//((t.getVisitor().equals(m.getLocal())) && (t.getLocal().equals(m.getVisitor()))) )
	//		&&
	//		(t.getTournamentGroup().equals(m.getTournamentGroup()))
	
			
	protected void importAlert() {
		importAlert(f_alert);
		
	}
	
	protected void importAlert(String fileName) {
		AlertImporter ai = getApplicationContext().getBean(AlertImporter.class, getKey(), fileName);
		try {
			Alert a = ai.execute();
			setAlert(a);
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}
	
	protected void importMeta() {
		importMeta(f_torneo_properties);
	}
	
	protected void importMeta(String fileName) {
		MetaImporter ai = getApplicationContext().getBean(MetaImporter.class, getKey(), fileName);
		try {
			Meta meta = ai.execute();
			setMeta(meta);
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}
	
	protected void exportScheduleResults(List<ScheduleMatchDate> list) {
		logger.debug("Exporting schedule to results file -> " +  f_scheduleINFO);
		exportScheduleResults(getKey()+"-schedule-result.ftl", f_scheduleINFO, list);
	}
	
	
	protected void exportScheduleCSV() {
		logger.debug("Exporting schedule t file -> " +  f_scheduleCSV);
		
		ScheduleCSVExporter exporter = getApplicationContext().getBean(	ScheduleCSVExporter.class, 
				getKey(),
				"schedule.csv", 
				"schedule-csv.ftl" );
		try {
		
		exporter.export();
		
		} catch (IOException e) {
		logger.error(e);
		System.exit(1);
		}
		catch (TemplateException e1) {
		logger.error(e1);
		System.exit(1);
		}
		
		
		
	}
	
	
	protected void exportScheduleResults(String templateFileName, String fileName, List<ScheduleMatchDate> list) {
		
		String destFileName = fileName;
		
		ScheduleResultsExporter exporter = getApplicationContext().getBean(	ScheduleResultsExporter.class, 
																			getKey(),
																			destFileName, 
																			templateFileName );
		try {
			
			exporter.export();
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		 catch (TemplateException e1) {
			logger.error(e1);
			System.exit(1);
		}
	}
	
	
	protected void importContacts() {
		importContacts(f_contacts);
	}
	
	protected void importContacts(String fileName) {
		
		ContactImporter ai = getApplicationContext().getBean(ContactImporter.class, getKey(), fileName);
		
		try {
			List<Contact> a = ai.execute();
			setContacts(a);
							
				
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}


	protected void importZones() {
		importZones(f_zones);	
	}
	
	
	protected void importZones( String fileName ) {

		this.setTournamentGroups(new ArrayList<TournamentGroup>());
		
		ZonasImporter za = getApplicationContext().getBean(ZonasImporter.class, getKey(), fileName);
		try {
			this.setTournamentGroups(za.execute());
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}

	
	protected void importSchedule() {
		logger.debug("Importing Schedule from -> " + f_scheduleCSV);
		importSchedule("schedule.csv");
	}
	
	protected void importSchedule(String fileName) {
		
		ScheduleImporter si = getApplicationContext().getBean(ScheduleImporter.class, getKey(), fileName);

		try {
			
			setSchedule(si.execute());
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		
		if (getSchedule().getMatchFinal()!=null) {
			if (getSchedule().getMatchFinal().isCompleted()) {
				if (getSchedule().getMatchFinal().getResult()==MatchResult.LOCAL)
					setWinner(getSchedule().getMatchFinal().getLocal());
				else if (getSchedule().getMatchFinal().getResult()==MatchResult.VISITOR)
					setWinner(getSchedule().getMatchFinal().getVisitor());
				setState(TournamentStatus.FINISHED);
			}
			else {
				setState(TournamentStatus.FINAL);
			}
			
		}
		else if (getSchedule().getMatchesSemifinal()!=null) {
			
			List<Match> semis = getSchedule().getMatchesSemifinal();
			
			if (semis.size()==2) {
				if (semis.get(0).isCompleted() && semis.get(1).isCompleted()) 
					setState(TournamentStatus.FINAL);
				else
					setState(TournamentStatus.SEMIFINALS);
			}
		}
		else if (getSchedule().getMatchesClasificacion()!=null) {
			for (Match match: getSchedule().getMatchesClasificacion()) {
					if (match.isCompleted()) { 
						setState(TournamentStatus.CLASIFICATION);
						break;
					}
			}
		}
		else {
			setState(TournamentStatus.NOT_STARTED);
		}
	}

	
	



	protected void exportTeams(String templateFileName, String destFileName) {
		
		TeamsExporter exporter = getApplicationContext().getBean(	TeamsExporter.class, 
																	getKey(),													
																	destFileName, 
																	templateFileName );
		try {
			
			exporter.export();
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		 catch (TemplateException e1) {
			logger.error(e1);
			System.exit(1);
		}
		
	}
	
	
	
	/**
	 * schedule.csv
	 *
	 */
	protected boolean isScheduleCSV() {
		File schedule = new File( getSettings().getTournamentDataDir( getKey()) + File.separator + f_scheduleCSV);
		return schedule.exists() && schedule.isFile();
		
	}
	
	
	/**
	 * schedule-results.info
	 * 
	 * @return
	 */
	protected boolean isScheduleResults() {
		File scheduleResults =  new File(getSettings().getTournamentDataDir( getKey()) + File.separator + f_scheduleINFO);
		return (scheduleResults.exists() && scheduleResults.isFile());
	}

	
	
	protected boolean isResultadosCSV() {
		File results =  new File(getSettings().getTournamentDataDir( getKey()) + File.separator + "resultados.csv");
		return (results .exists() && results .isFile());
	}

	
	protected void exportIndex(String templateFileName, String destFileName) {
		
		IndexExporter exporter = getApplicationContext().getBean(IndexExporter.class, 
				getKey(),											
				destFileName, 
				templateFileName);
		
		try {
			exporter.export();
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		 catch (TemplateException e1) {
			logger.error(e1);
			System.exit(1);
		}
	}

	



	
	
}
