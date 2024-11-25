package io.paletaweb.club.tournament;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import freemarker.template.TemplateException;
import io.paleta.logging.Logger;
import io.paleta.model.Alert;
import io.paleta.model.Contact;
import io.paleta.model.Match;
import io.paleta.model.MatchResult;
import io.paleta.model.Meta;
import io.paleta.model.Team;
import io.paleta.model.TournamentGroup;
import io.paleta.model.TournamentGroupTable;
import io.paleta.model.schedule.RoundRobinGenerator;
import io.paleta.model.schedule.Schedule;
import io.paleta.model.schedule.ScheduleMatchDate;
import io.paleta.model.schedule.SchedulePlanner;

@Service
public class TorneoCuba extends Tournament implements ApplicationContextAware {
			
	static private Logger logger = Logger.getLogger(TorneoCuba.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");
	
		
	public TorneoCuba() {
		super("viamonte2024", "Viamonte 2024");
	}
	
	
	public TorneoCuba(String directory, String name) {
		super(directory, name);
	}
	
	
	/**
	 * if schedule.info not exists -> 
	 *     
	 *     if ("schedule.csv" exists)
	 *     			importa de"schedule.csv", "genera schedule.info"
	 *     else
	 *     			genera schedule por algoritmo, genera "schedule.info"
	 *      
	 *      
	 *      
	 * 
	 *    carga el schedule y resultados de "schedule.info"
	 *      
	 *      
	 * 
	 * @return
	 */
	

	
	
	
	
}
