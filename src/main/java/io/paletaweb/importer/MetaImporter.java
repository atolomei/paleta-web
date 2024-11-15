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
import io.paleta.model.Meta;
import io.paleta.util.Check;

@Component
@Scope("prototype")
public class MetaImporter extends BaseImporter {

	private String src;
	private Meta meta = null;
	
	public  MetaImporter(String src) {
		Check.requireNonNullStringArgument(src, "src is null");
		this.src=src;
	}
	
	public String getSource() {
		return src;
	}
	
	public Meta execute() throws IOException {
		
		
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
		
		
		this.meta = new Meta();
		
		records.forEach( li -> {
			
			if ((li.size()>1)) {  
			
				if (li.get(0).toLowerCase().trim().equals("title")) {
					this.meta.setTitle(li.get(1).trim());
				}
				else if (li.get(0).toLowerCase().trim().equals("description")) {
					this.meta.setDescription(li.get(1).trim());
				}
				else if (li.get(0).toLowerCase().trim().equals("language")) {
					this.meta.setLanguage(li.get(1).trim());
				}
				
				else if (li.get(0).toLowerCase().trim().equals("keywords")) {
					this.meta.setKeywords(li.get(1).trim());
				}
				
				/**
				else if (li.get(0).toLowerCase().trim().startsWith("alert.text")) {
					StringBuilder str = new StringBuilder();
					str.append(li.get(1));
					if (li.size()>2) {
						for (int n=2; n<li.size();n++) {
							str.append("=");
							str.append(li.get(n));
						}
					}
					this.alert.setText(str.toString().trim());
				}
				**/
			}
			
		});
		
		return this.meta;
		
	}

	private OffsetDateTime getEndDate(String string) {
		return null;
	}

	private OffsetDateTime getStartDate(String string) {
		return null;
	}
	
}
