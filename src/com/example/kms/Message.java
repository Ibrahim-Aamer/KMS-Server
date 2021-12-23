package com.example.kms;

import java.io.Serializable;
import java.util.ArrayList;

// must implement Serializable in order to be sent
public class Message implements Serializable{

    private String query;
    private String username;
    private String password;

    private EmployeeKMS empKms;

    private ArrayList<LeaveRequest> AllLeaveRequests;

    private LeaveRequest newLeaveRequest;

    private Product newProduct;

    private ArrayList<Product> ProductsList;

    private EmployeeKMS newMember;

    //private Employee employeeObject;


    //Variables to be used by kitchen Manager
    private EmployeeKMS empAssignedto;
    private Task assignedTask;

    private ArrayList<EmployeeKMS> EmployeeKMSList = new ArrayList<EmployeeKMS>();

    private ArrayList<Task> TasksList = new ArrayList<Task>();

    private LeaveRequest acknowledgeLeaveRequest;

    //Array list for ingredients
    private ArrayList<Ingredients> ingredientsList = new ArrayList<Ingredients>();


    public LeaveRequest getAcknowledgeLeaveRequest() {
        return acknowledgeLeaveRequest;
    }

    public void setAcknowledgeLeaveRequest(LeaveRequest acknowledgeLeaveRequest) {
        this.acknowledgeLeaveRequest = acknowledgeLeaveRequest;
    }

    public Task getNewTask() {
        return newTask;
    }

    public LeaveRequest getNewLeaveRequest() {
        return newLeaveRequest;
    }

    public ArrayList<LeaveRequest> getAllLeaveRequests() {
        return AllLeaveRequests;
    }

    public void setAllLeaveRequests(ArrayList<LeaveRequest> allLeaveRequests) {
        AllLeaveRequests = allLeaveRequests;
    }


    public void setNewLeaveRequest(LeaveRequest newLeaveRequest) {
        this.newLeaveRequest = newLeaveRequest;
    }

    public ArrayList<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }

    private Task newTask;


    public Message() {

    }


    public Message(String query) {

        this.query = query;
    }

    public EmployeeKMS getEmpAssignedto() {
        return empAssignedto;
    }

    public void setEmpAssignedto(EmployeeKMS empAssignedto) {
        this.empAssignedto = empAssignedto;
    }

    public Task getAssignedTask() {
        return assignedTask;
    }

    public void setAssignedTask(Task assignedTask) {
        this.assignedTask = assignedTask;
    }


    public ArrayList<Task> getTasksList() {
        return TasksList;
    }

    public void setTasksList(ArrayList<Task> tasksList) {
        TasksList = tasksList;
    }


    //Returning employee object
    public EmployeeKMS getEmployeeObject()
    {
        return this.empKms;
    }

    public void setEmployeeObject(EmployeeKMS emp)
    {
        this.empKms = emp;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<EmployeeKMS> getEmployeeList() {
        return EmployeeKMSList;
    }

    public void setEmployeeList(ArrayList<EmployeeKMS> employeeList) {
        EmployeeKMSList = employeeList;
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }

    public ArrayList<Product> getProductsList() {
        return ProductsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        ProductsList = productsList;
    }

    public EmployeeKMS getNewMember() {
        return newMember;
    }

    public void setNewMember(EmployeeKMS newMember) {
        this.newMember = newMember;
    }
}