package com.pack.Employee;//package com.pack.Employee;

import com.example.kms.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Iterator;
import java.util.List;

public class StoreData {
	
	 public static void main(String[] args) {
	        // loads configuration and creates a session factory
	        Configuration con = new Configuration();
	        con.configure().addAnnotatedClass(Employee.class);
	        SessionFactory sf= con.buildSessionFactory();
	        Session session= sf.openSession();
	        Transaction trans= session.beginTransaction();


	        Address add= new Address();
	        add.setAddress_id(6);
	        add.setCity("Islambad");
	        add.setStreet("street 61");
	        //session.save(add);
	        
	        Employee emp=new Employee("John","Carter","John12","tiger","Kitchen Manager");
	        //emp.("Principal");
	        //emp.setCompany("FAST");
	        //emp.setEmail("john@gmail");
	        //emp.setEmpAddress(add);
	        //session.save(emp);

		 List employees = session.createQuery("FROM Employee").list();
		 for(Iterator iterator = employees.iterator(); iterator.hasNext();)
		 {
			 Employee employee = (Employee)iterator.next();
			 System.out.println(employee.getFirstName()+" "+employee.getLastName());
		 }
	        
	        
	        trans.commit();
	 }
}
	        

