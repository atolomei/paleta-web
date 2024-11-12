package io.paletaweb.torneo;



import java.io.IOException;
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
import io.paleta.model.Match;
import io.paleta.model.Schedule;
import io.paleta.model.TablePosition;
import io.paleta.model.TournamentGroup;
import io.paleta.model.TournamentGroupTable;
import io.paletaweb.exporter.IndexExporter;
import io.paletaweb.exporter.ScheduleExporter;
import io.paletaweb.exporter.TableExporter;
import io.paletaweb.exporter.ZoneExporter;
import io.paletaweb.importer.ResultadoImporter;
import io.paletaweb.importer.ScheduleImporter;
import io.paletaweb.importer.ZonaImporter;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;

@Service
public class TorneoCuba implements ApplicationContextAware {
			
	static private Logger logger = Logger.getLogger(TorneoCuba.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");
						
	private String name;
	
	@JsonIgnore
	private Schedule schedule;
	
	@JsonIgnore
	private List<TournamentGroup> zonas;
	
	@JsonIgnore
	private List<Match> matches;
	
	//@JsonIgnore
	//private Map<TournamentGroup, TablePosition> tables;
	
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
	private Map<TournamentGroup, TournamentGroupTable> groupTables = new HashMap<TournamentGroup, TournamentGroupTable>();
	
	
	
	
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
	
	
	
	
	protected void importData() {
		importZones();
		importSchedule();
	}
	
	
			
	private void calculateTables() {
		
		for (TournamentGroup group: this.getTournamentGroups()) {

			 TournamentGroupTable table = new TournamentGroupTable(group, getSchedule()); 
			 table.calculate();
			  this.groupTables.put(group, table);
			  
			  startupLogger.info(table.toString());
			  startupLogger.info("");
			  
			  
		}
		
		startupLogger.info("done");
	}
	
	
	private void exportData() {
		
		//for (TournamentGroup group: this.getTournamentGroups())
		//	exportGroup(group);
		
		//startupLogger.debug("---------------------------------------------------------");
		//startupLogger.debug("Groups -> exported");
		//startupLogger.debug("---------------------------------------------------------");
		
				
		
		//exportSchedule();
		
		//startupLogger.debug("---------------------------------------------------------");
		//startupLogger.debug("Schedule -> exported");
		//startupLogger.debug("---------------------------------------------------------");
		
		
		//for (TournamentGroup z: this.getTournamentGroups()) {
		//	exportTable(z);	
		//}
		//startupLogger.info("");
		
		
		exportIndex();
		
		startupLogger.debug("---------------------------------------------------------");
		startupLogger.debug("Index -> exported");
		startupLogger.debug("---------------------------------------------------------");
		
		startupLogger.info("done");
		
	}
	
	
	public void execute() {
	
		importData();
		
		calculateTables();
		
		exportData();
		
		
		
		
	}
	

	private void exportIndex() {
		
		String destFileName = "index.html";
		String templateFileName = "index.ftl";
		
 
		
		IndexExporter exporter = getApplicationContext().getBean(   IndexExporter.class, 
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

	/**
	 * 
	 */
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

	
	/**
	 * 
	private void exportTable(TournamentGroup zone) {
		
		String destFileName = zone.getName().toLowerCase().replace(" ", "_")+"_table"+".html";
		String templateFileName = "table.ftl";
		
		 TableExporter exporter = getApplicationContext().getBean( TableExporter.class, destFileName, templateFileName, zone);
		
		try {

			
			
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
	}
	 */
	
	
	
	protected ZonaImporter createZonaImporter(String src, String name) {
		return getApplicationContext().getBean(ZonaImporter.class, src, name);
	}
	
	
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
	
	/**
	 * 
	 */
	private void importZones() {

		zonas = new ArrayList<TournamentGroup>();
		
		ZonaImporter za = createZonaImporter("zona_A.csv", "Zona A");
		try {
			TournamentGroup zona_a = za.execute();
			zonas.add(zona_a);
			startupLogger.info( zona_a.toString());
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		

		ZonaImporter zb= createZonaImporter("zona_B.csv", "Zona B");
		try {
			TournamentGroup zona_b = zb.execute();
			zonas.add(zona_b);
			startupLogger.info(zona_b.toString());
		} catch (IOException e) {
			logger.error(e);
			System.exit(1);
		}
		
		logger.debug("import zones ok");
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



	
}
