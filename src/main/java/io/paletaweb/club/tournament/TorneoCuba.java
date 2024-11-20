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
import org.springframework.stereotype.Service;

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
import io.paleta.model.schedule.RoundRobinGenerator;
import io.paleta.model.schedule.Schedule;
import io.paleta.model.schedule.ScheduleMatchDate;
import io.paleta.model.schedule.SchedulePlanner;
import io.paletaweb.exporter.IndexExporter;
import io.paletaweb.exporter.PlayersExporter;
import io.paletaweb.exporter.ScheduleExporter;
import io.paletaweb.exporter.ScheduleResultsExporter;
import io.paletaweb.exporter.TableExporter;
import io.paletaweb.exporter.ZoneExporter;
import io.paletaweb.importer.AlertImporter;
import io.paletaweb.importer.ContactImporter;
import io.paletaweb.importer.MetaImporter;
import io.paletaweb.importer.ResultadoImporter;
import io.paletaweb.importer.ScheduleImporter;
import io.paletaweb.importer.ZonaImporter;
import io.paletaweb.importer.ZonasImporter;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;

@Service
public class TorneoCuba implements ApplicationContextAware {
			
	static private Logger logger = Logger.getLogger(TorneoCuba.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	
	static final String scheduleCSV="schedule.csv";
	static final String scheduleINFO="schedule.info";

	static final int NOT_STARTED     	= 0;
	static final int CLASIFICATION	 	= 1;
	static final int SEMIFINALS    	  	= 2;
	static final int FINAL			   	= 3;
	static final int FINISHED		   	= 4;
	
	private String name;
	
	@JsonIgnore
	private Schedule schedule;
	
	@JsonIgnore
	private List<TournamentGroup> zonas;
	
	@JsonIgnore
	private List<Match> matches;

	@JsonIgnore
	private Map<TournamentGroup, TournamentGroupTable> groupTables = new HashMap<TournamentGroup, TournamentGroupTable>();

	@Autowired
	@JsonIgnore
	SettingsService settings;
	
	@Autowired
	@JsonIgnore
	HTMLExportService htmlExportService;
	
	@Autowired
	@JsonIgnore
	private ApplicationContext applicationContext;

	@JsonIgnore
	private Team winner;
	
	@JsonIgnore
	private int state = NOT_STARTED;
	
	@JsonIgnore
	private OffsetDateTime startDate;
	
	@JsonIgnore
	private OffsetDateTime endDate;
	
	@JsonIgnore
	private Alert alert;
	
	@JsonIgnore
	private List<Contact> contacts;
	
	@JsonIgnore
	private String banner="Torneo de Paleta CUBA Viamonte";
	
	@JsonIgnore
	private Meta meta;
	
	public TorneoCuba() {
	}

	public TorneoCuba(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
	public List<TournamentGroup> getTournamentGroups() {
		return this.zonas;
	}
	
	public int getState() {
		return state;
	}
	
	public Team getWinner() {
		return winner;
	}

	public SettingsService getSettings() {
		return settings;
	}

	public void setSettings(SettingsService settings) {
		this.settings = settings;
	}

	public List<TournamentGroupTable> getGroupTableList() {
		List<TournamentGroupTable> list = new ArrayList<TournamentGroupTable>();

		list.addAll(getGroupTables().values());
		list.sort(new Comparator<TournamentGroupTable>() {
			@Override
			public int compare(TournamentGroupTable o1, TournamentGroupTable o2) {
				try {
					return o1.getTournamentGroup().getName().compareToIgnoreCase(o2.getTournamentGroup().getName());
				} catch (Exception e) {
					logger.error(e);
					return 0;
				}
			}
		});
		
		return list;
	}
	
	public Map<TournamentGroup, TournamentGroupTable> getGroupTables() {
		return groupTables;
	}

	public void setGroupTables(Map<TournamentGroup, TournamentGroupTable> groupTables) {
		this.groupTables = groupTables;
	}

	
	public Schedule getSchedule() {
		return this.schedule;
	}

	public List<Team> getTeams() {

		List<Team> list = new ArrayList<Team>();
		
		if (getTournamentGroups()==null)
			return list;
		
		for (TournamentGroup g: getTournamentGroups()) {
			list.addAll(g.getTeams());
		}
		
		list.sort(new Comparator<Team>() {
			@Override
			public int compare(Team o1, Team o2) {
				try {
					return o1.getName().compareToIgnoreCase(o2.getName());
				} catch (Exception e) {
					logger.error(e);
				}
				return 0;
			}
		});
		return list;
	}
	
	
	/**
	 *   
	 * 
	 * if schedule.info not exists -> 
	 *     
	 *     if ("schedule.csv" exists)
	 *     			importa de"schedule.csv", "genera schedule.info"
	 *     else
	 *     			genera schedule por algoritmo, genera "schedule.info"
	 *      
	 *      
	 *      
	 * 
	 *    carga el schedule y resultados de "schedule.info"
	 *      
	 *      
	 * 
	 * @return
	 */
	
	
	private boolean isScheduleCSV() {
		File schedule = new File(getSettings().getDataDir() + File.separator + scheduleCSV);
		return schedule.exists() && schedule.isFile();
		
	}
	private boolean isScheduleResults() {
		File scheduleResults =  new File(getSettings().getDataDir() + File.separator + "schedule.info");
		return (scheduleResults.exists() && scheduleResults.isFile());
	}
	
	
	
	public void execute() {
		
		/** Import */
		importMeta();
		importContacts();
		importAlert();
		importZones();
		//importInfo();
		
		boolean force = false;
		
		if (force) {
			RoundRobinGenerator roundRobin = new RoundRobinGenerator(this.getTournamentGroups());
			List<ScheduleMatchDate> dates = roundRobin.execute();
			List<Match> matches = new ArrayList<Match>();
			dates.forEach( r -> { 
				r.getMacth().setDate(r.getDate());
				matches.add(r.getMacth()); 
			});
			
			setSchedule(new Schedule("nofile", matches));
			
			SchedulePlanner planner = new SchedulePlanner( dates, getSchedule().getMatchesClasificacion()); 
			List<ScheduleMatchDate> list = planner.execute();
			exportScheduleResults(scheduleINFO, list);
		}
		
		if (!isScheduleResults()) {
			if (isScheduleCSV()) {
				logger.debug("Importing Schedule from -> " + scheduleCSV);
				importSchedule("schedule.csv");
				List<ScheduleMatchDate> list = new ArrayList<ScheduleMatchDate>();
				this.getSchedule().getMatchesClasificacion().forEach( m -> {
					list.add(new ScheduleMatchDate( m.getId(), m.getDate(), m)); }
				);
				logger.debug("Exporting schedule to results file -> " +  scheduleINFO);
				exportScheduleResults(scheduleINFO, list);	
			}
			else {
				RoundRobinGenerator roundRobin = new RoundRobinGenerator(this.getTournamentGroups());
				List<ScheduleMatchDate> dates = roundRobin.execute();
				List<Match> matches = new ArrayList<Match>();
				dates.forEach( r -> matches.add(r.getMacth()));
				setSchedule( new Schedule("nofile", matches));
				
				SchedulePlanner planner = new SchedulePlanner( dates, getSchedule().getMatchesClasificacion()); 
				List<ScheduleMatchDate> list = planner.execute();
				exportScheduleResults(scheduleINFO, list);	
			}
		}
		
		/** now import results from schedule.info */
		
		logger.debug("Importing Schedule and Results -> " + scheduleINFO);
		importSchedule(scheduleINFO);
		
		//System.exit(0);
		
		/** calculate */
		calculateTables();
		
		/** export */
		exportIndex();
		exportPlayers();
		
		startupLogger.debug("---------------------------------------------------------");
		startupLogger.debug("Index -> exported");
		startupLogger.debug("---------------------------------------------------------");
		
		startupLogger.info("done");
	}


	private void calculateTables() {
		
		this.groupTables = new HashMap<TournamentGroup, TournamentGroupTable>();
				
		for (TournamentGroup group: this.getTournamentGroups()) {
			 TournamentGroupTable table = new TournamentGroupTable(group, getSchedule()); 
			 table.calculate();
			  this.groupTables.put(group, table);
		}
	}
	
	
	
	private void exportIndex() {
		
		String destFileName = "index.html";
		String templateFileName = "index.ftl";
		
		IndexExporter exporter = getApplicationContext().getBean(IndexExporter.class, 
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


	
	private void exportScheduleResults(String fileName, List<ScheduleMatchDate> list) {
		
		String destFileName = fileName;
		String templateFileName = "schedule-result.ftl";
		
		ScheduleResultsExporter exporter = getApplicationContext().getBean(ScheduleResultsExporter.class, 
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

	
	private void exportPlayers() {
		
		String destFileName = "equipos.html";
		String templateFileName = "equipos.ftl";
		
		PlayersExporter exporter = getApplicationContext().getBean(PlayersExporter.class, 
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

	/**
	private void exportTable(TournamentGroup z) {
		String destFileName = z.getName().toLowerCase().replace(" ", "_")+"_table.html";
		String templateFileName = "table.ftl";
		TournamentGroupTable gtable=groupTables.get(z); 
		TableExporter exporter = getApplicationContext().getBean(   TableExporter.class, 
																	gtable,
																	destFileName, 
																	templateFileName
																);
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
	

	private void exportSchedule() {
		
		String destFileName = "schedule.html";
		String templateFileName = "schedule.ftl";
		
		ScheduleExporter exporter = getApplicationContext().getBean(ScheduleExporter.class, destFileName, templateFileName);
		
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
*/
	
	/**
	 * 
	
	private void exportGroup(TournamentGroup zone) {
		
		String destFileName = zone.getName().toLowerCase().replace(" ", "_")+".html";
		String templateFileName = "zona.ftl";
		
		ZoneExporter exporter = getApplicationContext().getBean(ZoneExporter.class, destFileName, templateFileName, zone);
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
 */
	
	
	
	
	
	protected ResultadoImporter createResultadoImporter(String src) {
		return getApplicationContext().getBean(ResultadoImporter.class, src);
	}

	
	public ApplicationContext getApplicationContext()  {
		return applicationContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		if (applicationContext==null) 
			applicationContext=appContext;
	}
	
	private void importAlert() {
		AlertImporter ai = getApplicationContext().getBean(AlertImporter.class, "alert.txt");
		try {
			Alert a = ai.execute();
			setAlert(a);
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}
	
	private void importMeta() {
		MetaImporter ai = getApplicationContext().getBean(MetaImporter.class, "torneo.properties");
		try {
			Meta meta = ai.execute();
			setMeta(meta);
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}
	
	


	private void importContacts() {
		
		ContactImporter ai = getApplicationContext().getBean(ContactImporter.class, "contacts.csv");
		
		try {
			List<Contact> a = ai.execute();
			setContacts(a);
							
				
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}

	/**
	 * 
	 */
	private void importZones() {

		
		this.zonas = new ArrayList<TournamentGroup>();
		
		ZonasImporter za = getApplicationContext().getBean(ZonasImporter.class, "zonas.csv");
		
		try {
			this.zonas = za.execute();
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}
	
	
	private void importSchedule( String fileName) {
	
		ScheduleImporter si = getApplicationContext().getBean(ScheduleImporter.class,  fileName);
		
		try {
			
			setSchedule(si.execute());
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		
		if (schedule.getMatchFinal()!=null) {
			if (schedule.getMatchFinal().isCompleted()) {
				if (schedule.getMatchFinal().getResult()==MatchResult.LOCAL)
					setWinner(schedule.getMatchFinal().getLocal());
				else if (schedule.getMatchFinal().getResult()==MatchResult.VISITOR)
					setWinner(schedule.getMatchFinal().getVisitor());
				setState(FINISHED);
			}
			else {
				setState(FINAL);
			}
			
		}
		else if (schedule.getMatchesSemifinal()!=null) {
			
			List<Match> semis = schedule.getMatchesSemifinal();
			
			if (semis.size()==2) {
				if (semis.get(0).isCompleted() && semis.get(1).isCompleted()) 
					setState(FINAL);
				else
					setState(SEMIFINALS);
			}
		}
		else if (schedule.getMatchesClasificacion()!=null) {
			for (Match match: schedule.getMatchesClasificacion()) {
					if (match.isCompleted()) { 
						setState(CLASIFICATION);
						break;
					}
			}
		}
		else {
			setState(NOT_STARTED);
		}
	}

	public void setMeta(Meta meta) {
		this.meta=meta;
		
	}
	
	public String getBanner() {
		return banner; 
	}
	
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	public void setAlert(Alert alert) {
		this.alert=alert;
		
	}
	public Alert getAlert() {
		return this.alert;
	}

	public Meta getMeta() {
		return meta;
	}
	
	
	private void setSchedule(Schedule schedule) {
			this.schedule=schedule;
	}

	private void setState(int state) {
		this.state=state;
		
	}

	private void setWinner(Team team) {
		this.winner=team;
	}

	
	
	


	
}
