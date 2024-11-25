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


@Component
@Scope("prototype")
public class ContactImporter extends BaseImporter {
	
	private List<Contact> list;
	
	
	
	public ContactImporter(String dir, String sourceFile) {
		super(dir, sourceFile);
	}
	
	
	public List<Contact> execute() throws IOException {
		
		String path = getSettings().getTournamentDataDir( getTournamentDirectory() ) + File.separator + getSourceFile();
		File f = new File(path);
		
		if (!f.exists())
			return null;
		
		List<List<String>> records;
		 
		try (Stream<String> lines = Files.lines(Paths.get( path ))) {
			records = lines.filter(line -> (!line.startsWith("#")) && (!line.isBlank()))
						   .map(line -> Arrays.asList(line.split(",")))
					       .collect(Collectors.toList());
		}
		
		
		if (records==null || records.size()==0)
			return null;
		
		list = new ArrayList<Contact>();
		
		records.forEach( li -> {
			if (li.size()>0) {
				list.add( new Contact(li.get(0).trim(), 
						(li.size()>1?li.get(1).trim(): "")));
			}
		});
		
		return list;
	}
		
	
}
