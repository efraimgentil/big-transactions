package br.com.efraimgentil.bigtransactions;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.ejb.TransactionImpl;
import org.hibernate.internal.SessionImpl;

import br.com.efraimgentil.bigtransactions.entities.Employee;

public class EntityManagerInsert implements Insert {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
		Config config = new Config(1000000l, true);
		config.setEmf(emf);
		new EntityManagerInsert().executeInsert( config );
		emf.close();
		System.exit(0);
	}

	public void executeInsert(Config config) {
		EntityManagerFactory emf = config.getEmf();
		EntityManager em = emf.createEntityManager();new StatelessSessionInsert().executeInsert( config );
		EntityTransaction transaction = em.getTransaction();
		
		long time = System.currentTimeMillis();
		try {
			transaction.begin();
			for (long i = 0l; i < config.getMax() ; i++) {
				if(i >= config.getBreakAt() )
					throw new Exception("SHOULD ROLL BACK");
				if( i % 1000l == 0l){
					if(config.isShowLog())
						System.out.println( i + " - " +  ( System.currentTimeMillis() - time ) / 1000 + " seconds "  );
					em.flush();
					em.clear();
				}
				em.persist(new Employee(i) );
			}
			transaction.commit();
			System.out.println(getClass().getSimpleName()  + ": "+ config.getMax() 
					+ "  Inserts, Total time: " +  ( System.currentTimeMillis() - time ) / 1000 + " seconds "  );
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction.isActive()){
				transaction.rollback();
				System.out.println("OPS, rollback");
			}
		}finally{
			em.close();
		}
	}

}
