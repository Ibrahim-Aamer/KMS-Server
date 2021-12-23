import com.example.kms.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Server class
class Server {
    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(4470);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());

                // create a new thread object
                ClientHandler clientSock
                        = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {

            //---HIBERNATE INITIAL CONFIGURATION CODE-------------
            // loads configuration and creates a session factory
            Configuration con = new Configuration();
            con.configure().addAnnotatedClass(Employee.class);
            SessionFactory sf= con.buildSessionFactory();
            Session session= sf.openSession();
            Transaction trans= session.beginTransaction();
            //-----------------------------------------------------


            PrintWriter out = null;
            BufferedReader in = null;
            try {

                ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

                Message receivedMessage = (Message) is.readObject();
                System.out.println(receivedMessage.getQuery());

                //For replying
                Message replyMessage = new Message();

                //IF ELSE STATEMENTS TO CATER DIFFERENT INCOMING QUERIES

                //Login Query
                if(receivedMessage.getQuery().equals("LoginQuery"))
                {
                    //calling appropriate caterer for query
                    replyMessage = this.LoginQueryHandler(receivedMessage, session);
                }
                else if(receivedMessage.getQuery().equals("KitchenManger-AssignTask"))
                {
                    //calling appropriate caterer for query
                    replyMessage = this.KitchenManagerAssignTask(receivedMessage,session);
                }
                else if(receivedMessage.getQuery().equals("KitchenManger-AddTask")
                       || receivedMessage.getQuery().equals("Sous Chef-AddMealPrep")
                        || receivedMessage.getQuery().equals("Junior Chef-AddSupplyOrder"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.AddNewTaskHandler(receivedMessage,session);
                }
                else if(receivedMessage.getQuery().equals("Leave-Request"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.LeaveRequestHandler(receivedMessage,session);
                }
                else if(receivedMessage.getQuery().equals("KitchenManger-Acknowledge Leave Request"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.KitchenManagerAcknowledgeLeaveRequest(receivedMessage,session);
                }
                else if(receivedMessage.getQuery().equals("Head Chef-Create Menu"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.HeadChefCreateMenu(receivedMessage,session);
                }
                else if(receivedMessage.getQuery().equals("Head Chef-Delete Menu Item"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.HeadChefDeleteMenu(receivedMessage,session);
                }
                else if(receivedMessage.getQuery().equals("Head Chef-Add New Member"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.HeadChefAddNewMember(receivedMessage,session);
                }
                else if(receivedMessage.getQuery().equals("Junior Chef-Add New Ingredient"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.JuniorChefAddIngredientsQuery(receivedMessage,session);
                }
                else
                {
                    replyMessage.setQuery("Invalid-Query");
                }

                trans.commit();//committing hibernate transaction

                //Sending reply message back to client
                ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
                os.writeObject(replyMessage);

                clientSocket.close();

            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private Message JuniorChefAddIngredientsQuery(Message receivedMessage, Session session)
        {
            Message replyMessage = new Message();

            //Getting new Ingredient from Message
            Ingredients newIngredient = receivedMessage.getNewIngredient();

            //Adding into table
            session.save(newIngredient);

            replyMessage.setQuery("Ingredients-Added");

            return replyMessage;

        }

        private Message HeadChefAddNewMember(Message receivedMessage, Session session)
        {
            Message replyMessage = new Message();

            EmployeeKMS tempEmp = receivedMessage.getNewMember();//getting employeeKMS

            //Creating new Employee
            Employee newEmp = new Employee(tempEmp.getFirstName(),tempEmp.getLastName()
                    ,tempEmp.getUsername(),tempEmp.getPassword(), tempEmp.getEmployeeType());

            //Adding into table
            session.save(newEmp);


            replyMessage.setQuery("Member-Added");

            //Now adding new task list in message
            replyMessage.setEmployeeList(this.getEmployeeList(session));

            return replyMessage;
        }

        private Message HeadChefDeleteMenu(Message receivedMessage, Session session)
        {
            Message replyMessage = new Message();

            //getting new leave request
            Product delProd = session.get(Product.class ,receivedMessage.getNewProduct().getProductID());//getting ack leave request
            //deleting from db
            session.delete(delProd);

            //session.saveOrUpdate(newLeaveRequest);

            System.out.println("Product deleted : " + delProd.getName());

            replyMessage.setQuery("Product Deleted");

            return replyMessage;


        }

        private Message HeadChefCreateMenu(Message receivedMessage, Session session)
        {

            //getting new received product
            Product receivedProduct = receivedMessage.getNewProduct();

            //Searching in db
            Product newProduct = session.get(Product.class,receivedProduct.getProductID());

            if(newProduct != null)
            {
                newProduct.setDescription(receivedProduct.getDescription());
                newProduct.setImgPath(receivedProduct.getImgPath());
                newProduct.setPrice(receivedProduct.getPrice());
                newProduct.setName(receivedProduct.getName());
                session.update(newProduct);//saving edited product
            }
            else
            {
                session.save(receivedProduct);//saving new product
            }

            Message replyMessage = new Message();
            replyMessage.setQuery("Product-Added");

            //Now adding new task list in message
            replyMessage.setTasksList(this.getTasksList(session));

            return replyMessage;
        }

        private Message KitchenManagerAcknowledgeLeaveRequest(Message receivedMessage, Session session)
        {
            Message replyMessage = new Message();

            //getting new leave request
            LeaveRequest ackLeaveRequest = session.get(LeaveRequest.class ,receivedMessage.getAcknowledgeLeaveRequest().getLeaveId());//getting ack leave request
            //deleting from db
            session.delete(ackLeaveRequest);



            //session.saveOrUpdate(newLeaveRequest);

            System.out.println("Leave deleted : " + ackLeaveRequest.getEmployeeName());

            replyMessage.setQuery("Leave-Request Deleted");

            return replyMessage;

        }

        private Message LeaveRequestHandler(Message receivedMessage, Session session)
        {
            Message replyMessage = new Message();

            //getting new leave request
            LeaveRequest newLeaveRequest = receivedMessage.getNewLeaveRequest();

            session.saveOrUpdate(newLeaveRequest);

            System.out.println("New Leave from : " + newLeaveRequest.getEmployeeName());

            replyMessage.setQuery("Leave-Request Added");

            return replyMessage;

        }

        //Function to assign task to an employee , used by kitchen manager
        public Message KitchenManagerAssignTask(Message receivedMessage,Session session)
        {
            Message replyMessage = new Message();

            //Getting emp and task from db
            Employee emp = session.get(Employee.class,receivedMessage.getEmpAssignedto().getID());
            Task atask = session.get(Task.class,receivedMessage.getAssignedTask().getId());

            atask.setTaskDate(receivedMessage.getAssignedTask().getTaskDate());
            session.saveOrUpdate(atask);

            emp.addTask(atask);//addding task to employee

            //updating tables

            session.saveOrUpdate(emp);

            System.out.println("EMP : " + emp.getFirstName());
            System.out.println("Task date: "+atask.getTaskDate());


            replyMessage.setQuery("Task-Assigned");

            System.out.println("Assigned to : " + emp.getFullName());
            System.out.println("Assigned to : " + atask.getTaskName());

            return replyMessage;
        }

        //Function to assign task to an employee , used by kitchen manager
        public Message AddNewTaskHandler(Message receivedMessage,Session session)
        {
            Message replyMessage = new Message();

            //Getting new Task from Message
            Task newTask = receivedMessage.getNewTask();

            //Adding into table
            session.save(newTask);

            if(receivedMessage.getQuery().equals("Sous Chef-AddMealPrep"))
            {
                this.UpdateIngredientsList(receivedMessage.getIngredientsList(),session);
            }

            replyMessage.setQuery("Task-Added");

            //Now adding new task list in message
            replyMessage.setTasksList(this.getTasksList(session));

            return replyMessage;
        }

        private void UpdateIngredientsList(ArrayList<Ingredients> ingredientsList, Session session)
        {
            //List updating
            for(int c=0 ; c<ingredientsList.size(); c++)
            {
                Ingredients ing = session.get(Ingredients.class,ingredientsList.get(c).getId());

                ing.setQuantity(ingredientsList.get(c).getQuantity());

                session.update(ing);
            }
        }

        public Message LoginQueryHandler(Message receivedMessage, Session session)
        {
            Message replyMessage = new Message();

            boolean userMatch = false;//boolean flag

            List employees = session.createQuery("FROM Employee").list();
            for(Iterator iterator = employees.iterator(); iterator.hasNext();)
            {
                Employee employee = (Employee)iterator.next();

                if(employee.getUsername().equals(receivedMessage.getUsername())
                        && employee.getPassword().equals(receivedMessage.getPassword()))
                {
                    replyMessage.setQuery("Username and Password Matched");
                    EmployeeKMS empkms = new EmployeeKMS(employee.getID(), employee.getFirstName(), employee.getLastName(),
                            employee.getUsername(), employee.getPassword(), employee.getEmployeeType(),employee.getTasks());
                    replyMessage.setEmployeeObject(empkms);//Putting found employee in message

                    //Putting employee list and tasks list in message
                    replyMessage.setTasksList(this.getTasksList(session));
                    replyMessage.setEmployeeList(this.getEmployeeList(session));
                    replyMessage.setAllLeaveRequests(this.getAllLeaveRequests(session));
                    replyMessage.setIngredientsList(this.getAllIngredientsList(session));
                    replyMessage.setProductsList(this.getProductList(session));

                    System.out.println(employee.getEmployeeType());
                    userMatch = true;
                    break;
                }
                //System.out.println(employee.getFirstName()+" "+employee.getLastName());
            }

            if(userMatch == false)
            {
                replyMessage.setQuery("Credentials Invalid");
            }

            return replyMessage;
        }

        private ArrayList<Ingredients> getAllIngredientsList(Session session)
        {
            ArrayList<Ingredients> AllIngredients = new ArrayList<Ingredients>();

            List ingredients = session.createQuery("FROM Ingredients").list();
            for(Iterator iterator = ingredients.iterator(); iterator.hasNext();)
            {
                Ingredients ing = (Ingredients) iterator.next();
                AllIngredients.add(ing);
                //System.out.println(employee.getFirstName()+" "+employee.getLastName());
                //employee.showAddresses();
            }

            return AllIngredients;
        }

        public ArrayList<LeaveRequest> getAllLeaveRequests(Session session)
        {
            ArrayList<LeaveRequest> AllLeaveRequests = new ArrayList<LeaveRequest>();

            List tasks = session.createQuery("FROM LeaveRequest").list();
            for(Iterator iterator = tasks.iterator(); iterator.hasNext();)
            {
                LeaveRequest leaveRequest = (LeaveRequest)iterator.next();
                AllLeaveRequests.add(leaveRequest);
                //System.out.println(employee.getFirstName()+" "+employee.getLastName());
                //employee.showAddresses();
            }

            return AllLeaveRequests;
        }

        public ArrayList<Task> getTasksList(Session session)
        {
            ArrayList<Task> taskList = new ArrayList<Task>();

            List tasks = session.createQuery("FROM Task").list();
            for(Iterator iterator = tasks.iterator(); iterator.hasNext();)
            {
                Task task = (Task)iterator.next();
                taskList.add(task);
                //System.out.println(employee.getFirstName()+" "+employee.getLastName());
                //employee.showAddresses();
            }

            return taskList;
        }

        public ArrayList<Product> getProductList(Session session)
        {
            ArrayList<Product> productList = new ArrayList<Product>();

            List employees = session.createQuery("FROM Product").list();
            for(Iterator iterator = employees.iterator(); iterator.hasNext();)
            {
                Product product = (Product)iterator.next();
                Product pd = new Product(product.getProductID(),product.getName(),product.getImgPath(),product.getPrice(), product.getDescription());
                productList.add(pd);
                //System.out.println(employee.getFirstName()+" "+employee.getLastName());
                //employee.showAddresses();
            }

            return productList;
        }


        public ArrayList<EmployeeKMS> getEmployeeList(Session session)
        {
            ArrayList<EmployeeKMS> employeeList = new ArrayList<EmployeeKMS>();

            List employees = session.createQuery("FROM Employee").list();
            for(Iterator iterator = employees.iterator(); iterator.hasNext();)
            {
                Employee employee = (Employee)iterator.next();
                EmployeeKMS empkms = new EmployeeKMS(employee.getID(), employee.getFirstName(), employee.getLastName(),
                        employee.getUsername(), employee.getPassword(), employee.getEmployeeType(),employee.getTasks());
                employeeList.add(empkms);
                //System.out.println(employee.getFirstName()+" "+employee.getLastName());
                //employee.showAddresses();
            }

            return employeeList;
        }
    }
}