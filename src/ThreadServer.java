import com.example.kms.Employee;
import com.example.kms.EmployeeKMS;
import com.example.kms.Message;
import com.example.kms.Task;
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
                else if(receivedMessage.getQuery().equals("KitchenManger-AddTask"))
                {
                    //Calling add new task handler for this query
                    replyMessage = this.AddNewTaskHandler(receivedMessage,session);
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

        //Function to assign task to an employee , used by kitchen manager
        public Message KitchenManagerAssignTask(Message receivedMessage,Session session)
        {
            Message replyMessage = new Message();

            //Getting emp and task from db
            Employee emp = session.get(Employee.class,receivedMessage.getEmpAssignedto().getID());
            Task atask = session.get(Task.class,receivedMessage.getAssignedTask().getId());

            emp.addTask(atask);//addding task to employee

            //updating tables
            session.update(emp);
            session.update(atask);

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

            replyMessage.setQuery("Task-Added");

            //Now adding new task list in message
            replyMessage.setTasksList(this.getTasksList(session));

            return replyMessage;
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