# big-transactions

Example project showing how to handle big transactions using jpa with hibernate


### What you need to run the project

- Java 1.7
- Maven
- Tomcat 7 +
- PostgresSQL running in your machine

### Create database

- Run the content of the file script.sql to configure the database


### The Problem

When working with a lot of data to be inserted or updated, jpa show a slow performance,  and with more data you work more slow it becomes, that happens because JPA/Hibernate caches the data that we are inserting, updating or selecting

### Inserting with batch

We can work this problem in many ways, mostly it depends on how much data we are handling, using JPA directly is the slowest way, using something from the vendor ( Hibernate in our case ) is faster, but when using the batch processing that difference is not so big

How we start using the batch processing ? first we will configure our persistence layer

```xml
<persistence-unit name="examplePUBatch" transaction-type="RESOURCE_LOCAL">
  	<provider>org.hibernate.ejb.HibernatePersistence</provider>
  	<properties>
	   	<property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect" />
	  	<property name="hibernate.connection.username" value="postgres" />
	   <property name="hibernate.connection.password" value="root" />
	   <property name="hibernate.connection.driver_class" value="org.postgresql.Driver" />
	   <property name="hibernate.connection.url" value="jdbc:postgresql://localhost:5432/test" />
	   	<property name="hibernate.jdbc.batch_size" value="1000" />
	</properties>
</persistence-unit>
```

The property "hibernate.jdbc.batch_size" configures our batch size, so we will be working with 1000 instructions per time, but note if we want to use the batch our entities should not have the @GeneratedValue(strategy = GenerationType.IDENTITY) annotation in their ID attribute, because this annotation disables the batch processing transparently, and you will never see any difference, with this in mind we are using a sequence to our id attribute, and we are allocating 1000 keys, because that is the size of our "batch_size"

```java
@Entity
@Table(name="employee")
public class EmployeeWithoutIdentity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO , generator = "employee_id_seq")
	@SequenceGenerator(name="employee_id_seq" , sequenceName="employee_id_seq" , allocationSize = 1000 )
	@Column(name="id")
	private Long id;
	@Column(name="name")
	private String name;
	@Column(name="sex")
	private String sex;
	@Column(name="father_name")
	private String fatherName;
	@Column(name="mother_name")
	private String motherName;
	@Temporal(TemporalType.DATE)
	@Column(name="birth_date")
	private Date birthDate;
	
	//	...	
}
```