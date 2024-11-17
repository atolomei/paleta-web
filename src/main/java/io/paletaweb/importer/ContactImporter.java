package io.paletaweb.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.paleta.model.Contact;
import io.paleta.model.schedule.Schedule;
import io.paleta.util.Check;


@Component
@Scope("prototype")
public class ContactImporter extends BaseImporter {
	
	String src;
	List<Contact> list;
	
	
	
	public ContactImporter(String src) {
		Check.requireNonNullStringArgument(src, "src is null");
		this.src=src;
	}
	
	public String getSource() {
		return src;
	}

	
	public List<Contact> execute() throws IOException {
		
		
		File f = new File( getSettings().getDataDir() + File.separator + getSource());
		
		if (!f.exists())
			return null;
		
		List<List<String>> records;
		 
		try (Stream<String> lines = Files.lines(Paths.get(getSettings().getDataDir() + File.separator + getSource()))) {
			records = lines.filter(line -> (!line.startsWith("#")) && (!line.isBlank()))
						   .map(line -> Arrays.asList(line.split(",")))
					       .collect(Collectors.toList());
		}
		
		
		if (records==null || records.size()==0)
			return null;
		
		list = new ArrayList<Contact>();
		
		records.forEach( li -> {
			if (li.size()>1) {
				list.add( new Contact(li.get(0).trim(), li.get(1).trim()));
			}
		});
		
		return list;
	}
		
	
}
