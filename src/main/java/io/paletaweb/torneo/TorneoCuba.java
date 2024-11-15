package io.paletaweb.torneo;




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
import io.paleta.model.Schedule;
import io.paleta.model.Team;
import io.paleta.model.TournamentGroup;
import io.paleta.model.TournamentGroupTable;
import io.paletaweb.exporter.IndexExporter;
import io.paletaweb.exporter.PlayersExporter;
import io.paletaweb.exporter.ScheduleExporter;
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
					return o1.getGroup().getName().compareToIgnoreCase(o2.getGroup().getName());
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
	
	public void execute() {
		
		/** Import */
		importMeta();
		//importInfo();
		importZones();
		importSchedule();
		importContacts();
		importAlert();
		
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


	private void exportPlayers() {
		
		String destFileName = "jugadores.html";
		String templateFileName = "jugadores.ftl";
		
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
		
		/**
		ZonaImporter za = createZonaImporter("zona_A.csv", "Zona A");
		try {
			TournamentGroup zona_a = za.execute();
			zonas.add(zona_a);
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		
		ZonaImporter zb = createZonaImporter("zona_B.csv", "Zona B");
		try {
			TournamentGroup zona_b = zb.execute();
			zonas.add(zona_b);
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		
		logger.debug("import zones ok");
		**/
		
	}
	
	
	
	private void importSchedule() {
	
		ScheduleImporter si = getApplicationContext().getBean(ScheduleImporter.class, "schedule.csv");
		
		try {
			
			this.schedule = si.execute();
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		
		logger.debug("import schedule ok");
		
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

	
	private void setState(int state) {
		this.state=state;
		
	}

	private void setWinner(Team team) {
		this.winner=team;
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
	
	


	
}
