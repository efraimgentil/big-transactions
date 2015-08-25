package br.com.efraimgentil.bigtransactions;

public interface EntityGenerator<T> {
	
	T generate(Long i);
	
}
