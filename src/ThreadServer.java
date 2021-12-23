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
                       || receivedMessage.getQuery().equals("Sous Chef-AddMealPrep"))
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