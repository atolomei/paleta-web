package io.paletaweb.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
@Scope("prototype")
public class TeamsExporter extends BaseExporter {

	
	public TeamsExporter(String tournamentDir, String dest_file, String html_template_file) {
		super(tournamentDir, dest_file, html_template_file);
	}
	
	
	
	public void export() throws IOException, TemplateException  {
	
		Configuration cfg = getHtmlExportService().getConfiguration();
		
		Map<String, Object> root = new HashMap<>();

		OffsetDateTime now = OffsetDateTime.now();
		
		root.put("exportdir", getSettings().getIndexExportDir());
		root.put("teams", getTournament().getTeams());
		root.put("dateexported", full_spa.format(now));
		
		
		root.put("torneo", getTournament());

		root.put("meta", getTournament().getMeta());
		root.put("alert", getTournament().getAlert());
		root.put("banner", getTournament().getBanner());

		root.put("groups", getTournament().getTournamentGroups());
		root.put("schedule", getTournament().getSchedule());
		root.put("grouptables", getTournament().getGroupTableList());
		root.put("contacts", getTournament().getContacts());

		
		
		String templateExportFile = getTournamentDirectory()+"-"+getTemplateFile();
		Template template = cfg.getTemplate(templateExportFile);
		
		Writer html = new FileWriter(new File(getSettings().getTournamentIndexExportDir( getTournamentDirectory() ), getDestFile()));
        template.process(root, html);

		html.flush();
        html.close();

	    //Writer out = new OutputStreamWriter(System.out);
    	//template.process(root, out);
    	//out.flush();
    	//out.close();

	}



}
