package io.paletaweb.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.paleta.logging.Logger;
import io.paleta.model.TournamentGroup;
import io.paleta.model.TournamentGroupTable;
import io.paleta.util.Check;
import io.paletaweb.importer.ZonaImporter;
import io.paletaweb.service.HTMLExportService;
import io.paletaweb.service.SettingsService;

@Component
@Scope("prototype")
public class TableExporter extends BaseExporter {

	static private Logger logger = Logger.getLogger(TableExporter.class.getName());
	
	@JsonIgnore
	private String html_template_file;

	@JsonIgnore
	private String dest_file;
	
	@JsonIgnore
	private TournamentGroupTable groupTable;

	
	public TableExporter(TournamentGroupTable groupTable, String html_dest_file, String html_template_file) {
		this.groupTable=groupTable;
		this.dest_file=html_dest_file;
		this.html_template_file=html_template_file;
	}
	
	
	public TournamentGroupTable getGroupTable() {
		return groupTable;
	}


	public void setGroupTable(TournamentGroupTable groupTable) {
		this.groupTable = groupTable;
	}


	public TableExporter (TournamentGroupTable gt) {
		this.groupTable=gt;
		
	}
	
	
	 
	public void export() throws IOException, TemplateException {

		Check.requireNonNull(getGroupTable());
		Check.requireNonNull(getGroupTable().getGroup());
		Check.requireNonNull(getGroupTable().getTable());
		
		Configuration cfg = getHtmlExportService().getConfiguration();

		Map<String, Object> root = new HashMap<>();		

		root.put("group", getGroupTable().getGroup().getName());
		root.put("tablepositions", getGroupTable().getTable());
		

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
	
	public SettingsService getSettings() {
		return settings;
	}

	public void setSettings(SettingsService settings) {
		this.settings = settings;
	}	
	
	private String getTemplateFile() {
		return this.html_template_file;
	}


	private String getDestFile() {
		return this.dest_file;
	}
			

}
