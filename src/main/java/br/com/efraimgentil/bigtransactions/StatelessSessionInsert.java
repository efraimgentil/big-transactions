package br.com.efraimgentil.bigtransactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionImpl;

import br.com.efraimgentil.bigtransactions.entities.Employee;

public class StatelessSessionInsert implements Insert{
	
	
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
		Config config = new Config(1000000l, true);
		config.setEmf(emf);
		new StatelessSessionInsert().executeInsert( config );
		emf.close();
		System.exit(0);
	}
	
	
	
	public void executeInsert(Config config) {
		EntityManagerFactory emf = config.getEmf();
		EntityManager em = emf.createEntityManager();
		
		//Gets the sessionImpl from hibernate
		SessionImpl session = (SessionImpl) em.getDelegate(); 
		//Create a statelessSession from hibernate
		StatelessSession oss = session.getSessionFactory().openStatelessSession();
		Transaction transaction = oss.beginTransaction();
		try {
			long time = System.currentTimeMillis();
			for (long i = 0l; i < config.getMax() ; i++) {
				if(i >= config.getBreakAt() )
					throw new Exception("SHOULD ROLL BACK");
				if( i % 1000l == 0l){
					if(config.isShowLog())
						System.out.println( i + " - " +  ( System.currentTimeMillis() - time ) / 1000 + " seconds "  );
				}
				oss.insert(  new Employee(i) );
			}
			transaction.commit();
			System.out.println(getClass().getSimpleName()  + ": "+ config.getMax() + "  Inserts, Total time: " +  ( System.currentTimeMillis() - time ) / 1000 + " seconds "  );
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}finally{
			oss.close();
			em.close();
		}
	}
}
