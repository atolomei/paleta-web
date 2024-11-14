package io.paletaweb.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.paleta.model.Alert;
import io.paleta.util.Check;

@Component
@Scope("prototype")
public class AlertImporter extends BaseImporter {
		
	protected String src;
	public  AlertImporter(String src) {
		Check.requireNonNullStringArgument(src, "src is null");
		this.src=src;
	}
	
	public String getSource() {
		return src;
	}
	
	Alert alert = null;
	
	public Alert execute() throws IOException {
		
		
		File f = new File( getSettings().getDataDir() + File.separator + getSource());
		
		if (!f.exists())
			return null;
		
		List<List<String>> records;
		
		try (Stream<String> lines = Files.lines(Paths.get(getSettings().getDataDir() + File.separator + getSource()))) {
			records = lines.filter(line -> (!line.startsWith("#")) && (!line.isBlank()))
						   .map(line -> Arrays.asList(line.split("=")))
					       .collect(Collectors.toList());
		}
		
		if (records.size()==0)
			return null;
		
		
		this.alert = new Alert();
		
		records.forEach( li -> {
			
			if (li.size()>1) {
				if (li.get(0).toLowerCase().trim().startsWith("alert.startdate")) {
					this.alert.setStartDate( getStartDate(li.get(1)));
				}
				else if (li.get(0).toLowerCase().trim().startsWith("alert.enddate")) {
					this.alert.setStartDate( getEndDate(li.get(1)));
				}
				else if (li.get(0).toLowerCase().trim().startsWith("alert.class")) {
					this.alert.setAlertClass(li.get(1).trim().toLowerCase());
				}
				
				else if (li.get(0).toLowerCase().trim().startsWith("alert.title")) {
					this.alert.setTitle(li.get(1).trim().toLowerCase());
				}
				
				else if (li.get(0).toLowerCase().trim().startsWith("alert.text")) {
					// TODO -> si hay = en el texto
					this.alert.setText(li.get(1).trim());
				}
			}
		});
		
		if ((this.alert.getTitle()==null || this.alert.getTitle().isBlank()) && 
			(this.alert.getText()==null || this.alert.getText().isBlank()) )
			return null;
		
		return this.alert;
		
	}

	private OffsetDateTime getEndDate(String string) {
		return null;
	}

	private OffsetDateTime getStartDate(String string) {
		return null;
	}
	

}
