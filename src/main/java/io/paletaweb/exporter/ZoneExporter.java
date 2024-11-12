package io.paletaweb.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.paleta.logging.Logger;
import io.paleta.model.TournamentGroup;
import io.paletaweb.importer.ZonaImporter;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;

/**
 * 
 * genera HTML de las zonas 
 * 
 * 
 */
@Component
@Scope("prototype")
public class ZoneExporter extends BaseExporter {

	static private Logger logger = Logger.getLogger(ZonaImporter.class.getName());
	
	private String template_file;
	private String dest_file;
	private TournamentGroup zona;
	

	public ZoneExporter(String dest_file, 
						String template_file, 
						TournamentGroup zona) {
		
		this.template_file=template_file;
		this.dest_file=dest_file;
		this.zona=zona;
	}
	
	
	/**
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void export() throws IOException, TemplateException {
		
		Configuration cfg = getHtmlExportService().getConfiguration();
		
		Map<String, Object> root = new HashMap<>();

		root.put("name", zona.getName());
		root.put("teams", zona.getTeams());

		Template template = cfg.getTemplate(getTemplateFile());
		
		Writer html = new FileWriter(new File(getSettings().getExportDir(), getDestFile()));
		
        template.process(root, html);

		html.flush();
        html.close();

	    Writer out = new OutputStreamWriter(System.out);
    	template.process(root, out);
    	out.flush();
    	out.close();
    	
	}

	private String getTemplateFile() {
		return this.template_file;
	}
	
	private String getDestFile() {
		return this.dest_file;
	}
	
	
}












/**
Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);

//cfg.setClassForTemplateLoading(this.getClass(), "/");
cfg.setDirectoryForTemplateLoading(new File( getSettings().getTemplatesDir()));

cfg.setDefaultEncoding("UTF-8");
cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
cfg.setLogTemplateExceptions(false);
cfg.setWrapUncheckedExceptions(true);
cfg.setFallbackOnNullLoopVariable(false);
cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault()); 	

//this.templateChartDescriptorFile = cfg.getTemplate("templates/chartDescriptorFile.ftl");
*/
