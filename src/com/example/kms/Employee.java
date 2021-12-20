package com.example.kms;



import com.pack.Employee.Address;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name="KMS_Employees")
public class Employee implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int id;

	protected String firstName;
	protected String lastName;

	protected String username;
	protected String password;

	@Column(name="EmployeeType")
	protected String employeeType;

	@Column(name="Email")
	protected String email;

	//@OneToOne(cascade = CascadeType.ALL)
	//private Address empAddress;

	//To hide a column @Transient

	public Employee(){};

	public Employee(String fname, String lname, String username, String password, String empType)
	{
		this.firstName = fname;
		this.lastName = lname;
		this.username = username;
		this.password = password;
		this.employeeType = empType;
	}

	public int getID() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName(){
		return (this.firstName+" "+this.lastName);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
		
		
		