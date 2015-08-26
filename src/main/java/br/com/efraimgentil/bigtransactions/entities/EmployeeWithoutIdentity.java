package br.com.efraimgentil.bigtransactions.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="employee")
public class EmployeeWithoutIdentity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "employee_id_seq")
	@SequenceGenerator(name="employee_id_seq" , sequenceName="employee_id_seq" , allocationSize = 500 )
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
	
	public EmployeeWithoutIdentity() {
		// TODO Auto-generated constructor stub
	}
	
	public EmployeeWithoutIdentity(long i) {
		name = "Generic Employee " + i;
		sex = "M";
		fatherName = "Generic Employee Father " + i;
		motherName = "Generic Employee MOther " + i;
		birthDate = new Date();
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
}
