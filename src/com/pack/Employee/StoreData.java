package com.pack.Employee;//package com.pack.Employee;

import com.example.kms.Employee;
import com.example.kms.Ingredients;
import com.example.kms.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class StoreData {
	
	 public static void main(String[] args) {
	        // loads configuration and creates a session factory
	        Configuration con = new Configuration();
	        con.configure().addAnnotatedClass(Employee.class);
	        SessionFactory sf= con.buildSessionFactory();
	        Session session= sf.openSession();
	        Transaction trans= session.beginTransaction();


		 	String sDate1="31/12/2021";
		 	Date date = new Date();
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}

		 //Employee emp1=new Employee("-","-","System","tiger","System");
			//session.save(emp1);

	   /*   Employee emp1=new Employee("John","Khan","John12","tiger","Kitchen Manager");
		 	Employee emp2=new Employee("Ibrahim","Aamer","Ibrahim12","tiger","Junior Chef");
		 	Employee emp3=new Employee("Musa","Aamer","Musa12","tiger","Sous Chef");
		 	Employee emp4=new Employee("Ahmed","Tanveer","Ahmed12","tiger","Head Chef");


		 	Task task1 = new Task("Pina Colada Requirements","Use 3 coconuts, blend and add sugar","31/12/2021");
		 	Task task2 = new Task("Dinner time","Make Mango eat milshake go sleep","31/12/2021");
		 	Task task3 = new Task("Lets go","Make Drinks and get drunk","31/12/2021");

		 session.save(task1);
		 session.save(task2);
		 session.save(task3);

			 emp2.addTask(task1);
			 emp2.addTask(task2);
			 emp3.addTask(task3);
	 		session.save(emp1);
	 		session.save(emp2);
	 		session.save(emp3);
	 		session.save(emp4);
			 session.save(task1);
			 session.save(task2);
			 session.save(task3);*/


		 //Ingredients ing = new Ingredients(2,"strawberry");
		 /*Ingredients ing1 = new Ingredients(2,"Coconut");
		 Ingredients ing2 = new Ingredients(2,"Mango");
		 Ingredients ing3 = new Ingredients(2,"Illaichi");
		 Ingredients ing4 = new Ingredients(2,"Karele");

		 session.save(ing1);
		 session.save(ing2);
		 session.save(ing3);
		 session.save(ing4);*/



		/* Employee emp = session.get(Employee.class,11);

		 System.out.println("Full name : "+emp.getFullName());

		 emp.addTask(task1);
		 emp.addTask(task2);

		 System.out.println(emp.getTasks().size());
		 for(int c=0 ; c<emp.getTasks().size();c++)
		 {
			 System.out.println(emp.getTasks().get(c).getTaskDetails());
		 }*/

		 /*session.saveOrUpdate(emp1);
		 session.saveOrUpdate(emp2);
		 session.saveOrUpdate(emp3);
		 session.saveOrUpdate(emp4);*/
		 //emp.addTask(task1);
		 //emp.addTask(task2);
		 //session.update(emp);
		 //emp2.addTask(task1);

		 /*Employee emp = session.get(Employee.class,4);
		 //System.out.println(emp.getFirstName()+" "+emp.getLastName());
		 List<Task> tasks = emp.getTasks();
		 for(int c=0 ; c<tasks.size(); c++)
		 {
			 System.out.println(emp.getFirstName()+" "+emp.getLastName()+tasks.get(c).getTaskName());
		 }*/


				 //Employee employee = session.get(Employee.class,emp1.getID());

			//employee.addTask();


		 //emp.addAddress(add);
			//emp.addAddress(add2);
			//emp.("Principal");
	        //emp.setCompany("FAST");
	        //emp.setEmail("john@gmail");
	        //emp.setEmpAddress(add);

	        /*session.saveOrUpdate(emp1);
	        session.saveOrUpdate(emp2);
	        session.saveOrUpdate(emp3);
	        session.saveOrUpdate(emp4);*/

		 /*List employees = session.createQuery("FROM Employee").list();
		 for(Iterator iterator = employees.iterator(); iterator.hasNext();)
		 {
			 Employee employee = (Employee)iterator.next();
			 System.out.println(employee.getFirstName()+" "+employee.getLastName());
			 //Set<Task> tasks = employee.getTasks();
			 Iterator<Task> it = employee.getTasks().iterator();
			 while(it.hasNext()){
				 System.out.println(it.next().getTaskName());
			 }
			 //employee.showAddresses();
		 }*/

		 /*List tasks = session.createQuery("FROM Task").list();
		 for(Iterator iterator = tasks.iterator(); iterator.hasNext();)
		 {
			 Task task = (Task)iterator.next();
			 //taskList.add(task);
			 System.out.println(task.getTaskDetails());
			 //employee.showAddresses();
		 }*/
	        
	        
	        trans.commit();
	 }
}
	        

