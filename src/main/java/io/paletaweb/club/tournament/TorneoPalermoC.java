package io.paletaweb.club.tournament;

import org.springframework.stereotype.Service;

@Service
public class TorneoPalermoC extends Tournament {

	
	public TorneoPalermoC() {
		super("cubac2024", "CUBA Categoría C 2024");
	}
	
	
	public TorneoPalermoC(String directory, String name) {
		super(directory, name);
	}
}
