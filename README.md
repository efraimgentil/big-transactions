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

The property "hibernate.jdbc.batch_size" configures our batch size, so we will be working with 1000 instructions per time, but note if we want to use the batch our entities should not have the @GeneratedValue(strategy = GenerationType.IDENTITY) annotation in their ID attribute, because this annotation disables the batch processing transparently, and you will never see any difference, with this in mind we are using a sequence to our id attribute, and we are alloemcating 1000 keys, because that is the size of our "batch_size"

```java
@Entity
@Table(name="employee")
public class EmployeeWithoutIdentity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "employee_id_seq")
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

Note that you will lose some keys for your pk and will have some "jumps" in your sequence count, there is a trick to change that, unfortunately  this is a vendor specific trick, you will use the @GenericGenerator(name="increment" , strategy="increment"), but be aware of what this is actually doing, this config will do a select for your max (id) in your table and increment from there, this is ok but your sequence will be forgotten, and if any developer try to manually insert something will get a biThis is your call, you dicide what to use, if will don't mind losing some keys, preffer to use the sequence generatorg error!

```java
@Entity
@Table(name="employee")
public class EmployeeWithoutIdentity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "increment")
	@GenericGenerator(name="increment" , strategy="increment")
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
	//...
}
```

This is your call, you will decide what to use, if you don't mind losing some keys in the sequence, preffer to use the sequence generator.

Then we will do our persistence normaly, but we will count each operation, and flush our entityManager/session when we reash our batch_size see:

```java
\\...
	\\ LOOP
	if( i % 1000l == 0l){
		em.flush();
		em.clear();
	}
	entityManager.persist( employee );
	i++
\\...
```