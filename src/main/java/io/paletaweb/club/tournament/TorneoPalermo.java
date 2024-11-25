package io.paletaweb.club.tournament;

import org.springframework.stereotype.Service;

@Service
public class TorneoPalermo extends Tournament {

	public TorneoPalermo() {
		super("cubab2024", "CUBA Categoría B 2024");
	}
	
	
	public TorneoPalermo(String directory, String name) {
		super(directory, name);
	}
	

}
