package com.example.kms;

import com.example.kms.Employee;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="kms_tasks5")
public class Task implements Serializable
{
    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "employee_id")
    //Employee mEmployee;//points to the employee to which this task belongs



    String taskName;

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    String taskDetails;

    String taskDate;

    public Task()
    {

    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Task(String taskName,String taskDet, String tDate)
    {
        //this.mEmployee = emp;
        this.taskName = taskName;
        this.taskDetails = taskDet;
        this.taskDate = tDate;
    }

    public int getId() {
        return id;
    }

    // public Employee getEmployee() {
    // return mEmployee;
    //}

    public String getTaskDetails() {
        return taskDetails;
    }

    public String getTaskDate() {
        return taskDate;
    }
}
