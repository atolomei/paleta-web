package io.paletaweb.club.tournament;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.paleta.logging.Logger;
import jakarta.annotation.PostConstruct;

@Service
public class TournamentManager implements ApplicationContextAware  {
			
	static private Logger logger = Logger.getLogger(TorneoCuba.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	@Autowired
	@JsonIgnore
	private ApplicationContext applicationContext;
	
	@Autowired
	@JsonIgnore
	TorneoCuba torneoViamonte;
	

	@Autowired
	@JsonIgnore
	TorneoPalermo torneoPalermo;
	

	
	Map<String, Tournament> map = new HashMap<String, Tournament>();
	
	public TournamentManager() {
	}
	
	@PostConstruct
	public void init() {
		
		torneoViamonte.setName("Torneo CUBA Viamonte");
		torneoViamonte.setKey("viamonte2024");
		torneoViamonte.setPrintRawSchedule(Boolean.valueOf(false));
		torneoViamonte.setPrintCalendarSchedule(Boolean.valueOf(true));
		
		map.put(torneoViamonte.getKey(), torneoViamonte);
		
		torneoPalermo.setName("Torneo Clausura Categoría B");
		torneoPalermo.setKey("cubab2024");
		torneoPalermo.setBanner("Torneo Clausura Categoría B");
		torneoPalermo.setPrintRawSchedule(Boolean.valueOf(true));
		torneoPalermo.setPrintCalendarSchedule(Boolean.valueOf(false));
		
		map.put(torneoPalermo.getKey(),torneoPalermo);
		
		
		
		
		
		
	}
	
	public ApplicationContext getApplicationContext()  {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		 this.applicationContext = applicationContext;
	}

	
	
	public Tournament getTourmanent(String key) {
		return map.get(key);
	}
	
	
}
