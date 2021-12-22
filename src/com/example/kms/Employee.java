package com.example.kms;


import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="kms_emp5")
public class Employee implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int id;

	protected String firstName;
	protected String lastName;

	@Column(unique=true)
	protected String username;
	protected String password;

	@Column(name="EmployeeType")
	protected String employeeType;

	public String getEmployeeType() {
		return employeeType;
	}

	//@OneToOne(cascade = CascadeType.ALL)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="emp_id")
	@OrderColumn(name="type")
	private Set<Task> tasks = new HashSet<>();
	//private Address empAddresses;



	public List<Task> getTasks()
	{
		List<Task> taskList = new ArrayList<Task>();

		Iterator<Task> it = this.tasks.iterator();
		while(it.hasNext()){
			taskList.add(it.next());
			//System.out.println(it.next().getTaskDetails());
		}
		return taskList;
	}

	public void addTask(Task task)
	{
		tasks.add(task);
	}

	public void setTasksCollection(Set<Task> tasks)
	{
		this.tasks = tasks;
	}

	/*public void showAddresses()
	{
		Iterator<Address> iterator = empAddresses.iterator();
		// while loop
		while (iterator.hasNext()) {
			System.out.println("Address = " + iterator.next().getAddress());
		}
	}
*/
	//To hide a column @Transient

	public Employee()
	{

	};

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
		
		
		