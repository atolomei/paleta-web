package io.paletaweb.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.paleta.logging.Logger;
import io.paleta.model.Match;
import io.paleta.model.schedule.Schedule;
import io.paleta.util.Check;
import io.paletaweb.importer.ZonaImporter;

@Component
@Scope("prototype")
public class ScheduleExporter extends BaseExporter {
				
	static private Logger logger = Logger.getLogger(ScheduleExporter.class.getName());
	
	//private String html_dest_file;
	//private String html_template_file;

	
	public ScheduleExporter (String tournamentDir, String dest_file, String html_template_file) {
		super(tournamentDir, dest_file, html_template_file);
	}
	
	
	public void export() throws IOException, TemplateException {
		
		Schedule schedule=getTournament().getSchedule();
		
		Check.requireNonNull(schedule);
		
		Configuration cfg = getHtmlExportService().getConfiguration();
		
		Map<String, Object> root = new HashMap<>();

		root.put("matches", schedule.getMatchesClasificacion());
		
		root.put("matchesSemifinal", schedule.getMatchesSemifinal());
		root.put("matchFinal", schedule.getMatchFinal());		
		

		String templateExportFile =getSettings().getTemplatesDir() + File.separator + getTournamentDirectory()+"-"+getTemplateFile();
		Template template = cfg.getTemplate(templateExportFile);
		
		Writer html = new FileWriter(new File(getSettings().getTournamentDataDir( getTournamentDirectory() ), getDestFile()));
        template.process(root, html);


		html.flush();
        html.close();

	    //Writer out = new OutputStreamWriter(System.out);
    	//template.process(root, out);
    	//out.flush();
    	//out.close();
	}


		
	
}

