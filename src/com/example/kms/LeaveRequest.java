package com.example.kms;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="kms_leave_requests")
public class LeaveRequest implements Serializable
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int leaveId;

    String startDate;
    String endDate;

    int empId;
    String employeeName;
    String employeeType;

    public LeaveRequest(){};

    public LeaveRequest(String startDate, String endDate, int empId, String employeeName, String employeeType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.empId = empId;
        this.employeeName = employeeName;
        this.employeeType = employeeType;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
}
