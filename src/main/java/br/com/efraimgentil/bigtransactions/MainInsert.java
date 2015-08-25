package br.com.efraimgentil.bigtransactions;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.efraimgentil.bigtransactions.entities.Employee;
import br.com.efraimgentil.bigtransactions.entities.EmployeeWithoutIdentity;

public class MainInsert {
	
	
	public static void main(String[] args) {
		
		//This persistence unit have the most basic configuration to access the database
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
		Config c = new Config( 100000, false  );
		c.setEmf( emf );
		
		EntityGenerator<Employee> employeeGenerator = new EntityGenerator<Employee>() {
			public Employee generate(Long i) {
				return new Employee(i);
			}
		};
		
		new EntityManagerInsert().executeInsert( c , employeeGenerator );
		new SessionInsert().executeInsert( c , employeeGenerator );
		new StatelessSessionInsert().executeInsert( c , employeeGenerator );
		
		EntityGenerator<EmployeeWithoutIdentity> employeeWIGenerator = new EntityGenerator<EmployeeWithoutIdentity>() {
			public EmployeeWithoutIdentity generate(Long i) {
				return new EmployeeWithoutIdentity(i);
			}
		};
		System.out.println( "Same generation without identity generatedValue");
		//The employeeWI uses the sequence generator, and allocates 1000 keys to be used
		new EntityManagerInsert().executeInsert( c , employeeWIGenerator );
		new SessionInsert().executeInsert( c , employeeWIGenerator );
		new StatelessSessionInsert().executeInsert( c , employeeWIGenerator );
		
		System.out.println( "Same generation without identity generatedValue and using batch inserts");
		//Note that to the batch inserts work, we need to use a generatedValue with strategy deferent from identity
		c.setUsesBatch(true);
		EntityManagerFactory emfBatch = Persistence.createEntityManagerFactory("examplePUBatch");
		c.setEmf(emfBatch);
		
		new EntityManagerInsert().executeInsert( c , employeeWIGenerator );
		new SessionInsert().executeInsert( c , employeeWIGenerator );
		new StatelessSessionInsert().executeInsert( c , employeeWIGenerator );
		// With batch we have a big diferente in the time to proccess all the inserts
		
		
		emf.close();
		emfBatch.close();
		
		System.exit(0);
		
	}
	
	
}
