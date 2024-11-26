package io.paletaweb.club.tournament;

import org.springframework.stereotype.Service;

@Service
public class TorneoPalermoB extends Tournament {

	public TorneoPalermoB() {
		super("cubab2024", "CUBA Categoría B 2024");
	}
	
	
	public TorneoPalermoB(String directory, String name) {
		super(directory, name);
	}
	

}
