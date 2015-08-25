package br.com.efraimgentil.bigtransactions;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MainInsert {
	
	
	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
		Config c = new Config( 100000, false  );
		c.setEmf( emf );
		
		new EntityManagerInsert().executeInsert( c );
		new SessionInsert().executeInsert( c);
		new StatelessSessionInsert().executeInsert( c);
		
	}
	
	
}
