package br.com.efraimgentil.bigtransactions;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Config {
	
	private long max;
	private long breakAt;
	private boolean showLog;
	private boolean usesBatch;
	private EntityManagerFactory emf;
	
	public Config(long max, boolean showLog) {
		super();
		this.max = max;
		this.breakAt = max + 1;
		this.showLog = showLog;
	}

	public Config(long max, long breakAt, boolean showLog) {
		super();
		this.max = max;
		this.breakAt = breakAt;
		this.showLog = showLog;
	}
	
	public long getMax() {
		return max;
	}
	public long getBreakAt() {
		return breakAt;
	}
	public boolean isShowLog() {
		return showLog;
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public boolean isUsesBatch() {
		return usesBatch;
	}

	public void setUsesBatch(boolean usesBatch) {
		this.usesBatch = usesBatch;
	}
	
	
	
}
