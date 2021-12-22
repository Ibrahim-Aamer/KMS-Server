package com.example.kms;


import java.io.Serializable;
import java.util.*;

public class EmployeeKMS implements Serializable {

    protected int id;

    protected String firstName;
    protected String lastName;

    protected String username;
    protected String password;


    protected String employeeType;

    public String getEmployeeType() {
        return employeeType;
    }


    private List<Task> tasks;
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

    public void setTasksCollection(List<Task> tasks)
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

    public EmployeeKMS()
    {

    };

    public EmployeeKMS(int ID,String fname, String lname, String username, String password, String empType, List<Task> tasks)
    {
        this.id = ID;
        this.firstName = fname;
        this.lastName = lname;
        this.username = username;
        this.password = password;
        this.employeeType = empType;
        this.tasks = tasks;
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


