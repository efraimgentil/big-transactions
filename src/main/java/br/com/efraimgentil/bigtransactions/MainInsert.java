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

public class MainInsert {

	public static void main(String[] args) {

		long max = 100000l;
		long breakAt = 100001l;

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("examplePU");
		EntityManager em = emf.createEntityManager();
//		EntityTransaction transaction = em.getTransaction();
		
		long time = System.currentTimeMillis();
		try {
			SessionImpl session = (SessionImpl) em.getDelegate(); 
//			transaction.begin();
			StatelessSession oss = session.getSessionFactory().openStatelessSession();
//			session.setFlushMode(FlushMode.MANUAL);
//			session.setCacheMode(CacheMode.IGNORE);
			Transaction transaction = oss.beginTransaction();
			
			for (long i = 0l; i < max; i++) {
				if(i >= breakAt)
					throw new Exception("SHOULD ROLL BACK");
				if( i % 1000l == 0l){
					System.out.println( i + " - " +  ( System.currentTimeMillis() - time ) / 1000 + " seconds "  );
//					session.flush();
//					session.clear();
//					em.flush();
//					em.clear();
				}
//				em.persist(new Employee(i) );
//				session.save(  new Employee(i) );
				oss.insert(  new Employee(i) );
			}
			transaction.commit();
			System.out.println( " DONE ");
			System.out.println( "Total in millis: " +  ( System.currentTimeMillis() - time ) / 1000 + " seconds "  );
		} catch (Exception e) {
			e.printStackTrace();
//			if (transaction.isActive()){
//				transaction.rollback();
//				System.out.println("OPS, rollback");
//			}
		}
//		List resultList = em.createQuery("FROM Employee").getResultList();

		System.exit(0);
	}

}
