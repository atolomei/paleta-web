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

@Component
@Scope("prototype")
public class PlayersExporter extends BaseExporter {

	private String dest_file;
	private String html_template_file;
	
	
	public PlayersExporter(String dest_file, String html_template_file) {
		this.dest_file=dest_file;
		this.html_template_file=html_template_file;
	}
	
	
	public void export() throws IOException, TemplateException  {
	
		Configuration cfg = getHtmlExportService().getConfiguration();
		
		Map<String, Object> root = new HashMap<>();

		root.put("exportdir", getSettings().getIndexExportDir());
		root.put("teams", getTorneo().getTeams());
		
		
		Template template = cfg.getTemplate(getTemplateFile());
	     
		Writer html = new FileWriter(new File(getSettings().getIndexExportDir(), getDestFile()));
        template.process(root, html);

		html.flush();
        html.close();

	    Writer out = new OutputStreamWriter(System.out);
    	template.process(root, out);
    	out.flush();
    	out.close();

	}


	private String getTemplateFile() {
		return this.html_template_file;
	}


	private String getDestFile() {
		return this.dest_file;
	}

}
