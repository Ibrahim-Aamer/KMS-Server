import com.example.kms.Employee;
import com.example.kms.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.List;

// Server class
class Server {
    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(4444);
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

                Message m = (Message) is.readObject();
                System.out.println(m.getText());
                m.setText(2," Wow");

                boolean userMatch = false;//boolean flag

                List employees = session.createQuery("FROM Employee").list();
                for(Iterator iterator = employees.iterator(); iterator.hasNext();)
                {
                    Employee employee = (Employee)iterator.next();

                    if(employee.getUsername().equals(m.getEmployee().getUsername())
                            && employee.getPassword().equals(m.getEmployee().getPassword()))
                    {
                        m.setText(1,"Username and Password Matched");
                        userMatch = true;
                    }
                    //System.out.println(employee.getFirstName()+" "+employee.getLastName());
                }

                if(userMatch == false)
                {
                    m.setText(1,"Credentials Invalid");
                }

                System.out.println(m.getEmployee().getFirstName()+" "+m.getEmployee().getLastName());

                ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());

                os.writeObject(m);

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
    }
}
