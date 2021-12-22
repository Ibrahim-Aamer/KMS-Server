package com.pack.Employee;

import com.example.kms.Employee;

import javax.persistence.*;

@Entity
@Table(name="EmployeeAddress")
public class Address {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int address_id;
	private String city;
	private String street;

	@ManyToOne
	//@JoinColumn (name="employee_id",referencedColumnName="id",nullable=false,unique=true)
	@JoinColumn (name="employee_id")
	private Employee mEmployee;

	public String getAddress()
	{
		return city+" "+street;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}
}
