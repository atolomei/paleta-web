package io.paletaweb.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.paleta.logging.Logger;
import io.paleta.model.schedule.Schedule;

@Component
@Scope("prototype")
public class ScheduleCSVExporter extends BaseExporter {
				
static private Logger logger = Logger.getLogger(ScheduleCSVExporter.class.getName());
	
										
	public ScheduleCSVExporter(String key, String dest_file, String html_template_file) {
		super(key, dest_file, html_template_file);
	}
	
	
	public void export() throws IOException, TemplateException {
		
		
		Schedule schedule=getTournament().getSchedule();
		
		Configuration cfg = getHtmlExportService().getConfiguration();
		
		OffsetDateTime now = OffsetDateTime.now();
		
		Map<String, Object> root = new HashMap<>();

		root.put("exportdir", getSettings().getTournamentDataDir( getTournamentDirectory() ));
		root.put("dateexported", full_spa.format(now));

		root.put("torneo", getTournament());

		root.put("meta", getTournament().getMeta());
		
		root.put("banner", getTournament().getBanner());
		root.put("alert", getTournament().getAlert());

		root.put("groups", getTournament().getTournamentGroups());
		root.put("schedule", getTournament().getSchedule());
		root.put("grouptables", getTournament().getGroupTableList());
		
		root.put("contacts", getTournament().getContacts());

		
		root.put("matches", schedule.getMatchesClasificacion());
		
		root.put("matchesSemifinal", schedule.getMatchesSemifinal());
		root.put("matchFinal", schedule.getMatchFinal());		
		
		String templateExportFile =getTemplateFile();
		Template template = cfg.getTemplate(templateExportFile);
	     
		String exportDir = getSettings().getTournamentDataDir( getTournamentDirectory() );  
		
		Writer html = new FileWriter(new File(exportDir, getDestFile()));
        template.process(root, html);

		html.flush();
        html.close();
		
	}
	
}
