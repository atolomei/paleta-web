package io.paletaweb.exporter;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import io.paleta.logging.Logger;
import io.paleta.util.Check;

public abstract class ExportAgent implements Runnable {
					
	static final long DEFAULT_SLEEP_TIME = 1 * 60 * 1000; // 1 minute

	private AtomicBoolean exit = new AtomicBoolean(false);
	private Thread thread;
	static private Logger logger = Logger.getLogger(ExportAgent.class.getName());
	
	private final int scanFreqMillisecs;
	
	public abstract void execute();
	
	
	public ExportAgent(int scanFreqMillisecs) {
		this.scanFreqMillisecs=scanFreqMillisecs;
	}
	
	public boolean exit() {
		return this.exit.get();
	}
	
	public void sendExitSignal() {
		this.exit.set(true);
		if (this.thread!=null)
			this.thread.interrupt();
	}
	
	public long getSleepTimeMillis() { 
		return this.scanFreqMillisecs;
	}

	@Override
	public void run() {
		Check.checkTrue(getSleepTimeMillis()>100, "getSleepTimeMillis() must be > 100 milisecs -> " + String.valueOf(getSleepTimeMillis()));
		this.thread = Thread.currentThread();
		synchronized (this) {
			
			while (!exit()) {
					try {
						execute();
						Thread.sleep(getSleepTimeMillis());
					
					} catch (InterruptedException e) {
						
					} catch (Exception e) {
						logger.error(e);
					}
			}
		}
	}

}
