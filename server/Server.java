package server;

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
public class Server {
        private List<Account> AccountList;
        private final Account acc1;
        private final Account acc2;
        private final Account acc3;
        private final Email mail1,mail2,mail3,mail4,mail5,mail6;
        public Server(){
            AccountList = new ArrayList(2);
            acc1 = new Account("Geon@csd.auth.gr","sabd");
            acc2 = new Account("Dean@csd.auth.gr","098f");
            acc3 = new Account("papado@gmail.com","am!2js");
            AccountList.add(acc1);
            AccountList.add(acc2);
            AccountList.add(acc3);
            mail1 = new Email(0,"Geon@csd.auth.gr","Dean@csd.auth.gr","Meeting","Hey, what place would you like to meet at?");
            mail2 = new Email(1,"Geon@csd.auth.gr","Dean@csd.auth.gr","Meeting","Okay, see you there.");
            mail3 = new Email(2,"papado@gmail.com","Dean@csd.auth.gr","Book","Do you have the book we use in databases,i want to check something.");
            mail4 = new Email(0,"Dean@csd.auth.gr","Geon@csd.auth.gr","Meeting","Lets meet at the centre, at 8 pm.");
            mail5 = new Email(1,"papado@gmail.com","Geon@csd.auth.gr","Database Book","Dean wants the book of databases do you have it by chance because i don't.");
            mail6 = new Email(2,"Dean@csd.auth.gr","Geon@csd.auth.gr","Basketball","Basketball at 12 tomorrow in the usual place.");
            acc1.addEmail(mail4);
            acc1.addEmail(mail5);
            acc1.addEmail(mail6);
            acc2.addEmail(mail1);
            acc2.addEmail(mail2);
            acc2.addEmail(mail3);
        }
	public static void main (String args[]) {
            int serverPort = Integer.parseInt(args[0]); // the server port
            try(
                    ServerSocket listenSocket = new ServerSocket(serverPort);
                ){
                    Server s = new Server();
                    while(true) {
                                Socket clientSocket = listenSocket.accept();		
                                Connection c = new Connection(clientSocket,s);
                    }
                } catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());
            }  
        }
        /**
         * 
         * @param username the username of the account
         * @param password the password of the account
         * @return true if login successful and false if not
         */
        public boolean LogIn(String username,String password){
            for (Account acc : AccountList){
                if( acc.getUsername().equals(username) && acc.getPassword().equals(password) )
                    return true;
            }
            return false;
        }
        /**
         * 
         * @param username username of the account
         * @param password password of the account
         * @return false if the username already exists true if the username is free and the account was created succesfully
         */
        public boolean addAccount(String username,String password){
            Account a;
            for (Account acc : AccountList){
                if( acc.getUsername().equals(username) )
                    return false;
            }
            a = new Account(username,password);
            AccountList.add(a);
            return true;
        }
        /**
         * 
         * @param username username of the account
         * @param password password of the account
         * @return the instance of the account with the given username/password combination
         */
        public Account getAccount(String username,String password){
            for (Account acc : AccountList){
                if( acc.getUsername().equals(username) && acc.getPassword().equals(password) )
                    return acc;
            }
            return null;
        }
        /**
         * 
         * @param username the username of the account you want to find
         * @return the account if the username exists null otherwise
         */
        public Account findAccount(String username){
            for (Account acc : AccountList){
                if (acc.getUsername().equals(username))
                    return acc;
            }
            return null;
        }
}
class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    String message;
    Server s;
    public Connection (Socket aClientSocket,Server s) {
        message = "";
        this.s = s;
        try {
                clientSocket = aClientSocket;
                in = new DataInputStream( clientSocket.getInputStream());
                out = new DataOutputStream( clientSocket.getOutputStream());
                this.start();
        } catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
    }
    public void run(){
            mainMenu();
    }
    private void mainMenu(){
        try{
            out.writeUTF('\n' + message + '\n' + '\n' + "Hello,please sign up or login." + '\n' + "---------------" + '\n' + ">SignUp" + '\n' + ">LogIn" + '\n' + ">Exit" + '\n' + "----------------");
            message = "";
            String command = in.readUTF();
            String u,p;
            if (command.equalsIgnoreCase("SignUp")){
                out.writeUTF("Type your username :");
                u = in.readUTF();
                out.writeUTF("Type your password :");
                p = in.readUTF();
                if (!s.addAccount(u,p))
                    message="Username already exists";
                else
                    message="Account succesfully created";   
            }
            else if (command.equalsIgnoreCase("LogIn")){
                while (true){
                    out.writeUTF("Type your username :");
                    u = in.readUTF();
                    out.writeUTF("Type your password :");
                    p = in.readUTF();   
                    if (s.LogIn(u,p)){
                        message="LogIn successful";
                        Account current = s.getAccount(u,p);
                        accountMenu(current);
                    }
                    else{
                        message="Invalid username or password,please try again";
                            mainMenu();
                    }
                }
            }
            else if (command.equalsIgnoreCase("Exit")){
               try{clientSocket.close();}catch(IOException e){System.out.println(e.getMessage());}
            }
            else
            {
                message = "That is not a valid command.";
            }
            mainMenu();
        }catch(EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch(IOException e){System.out.println(e.getMessage());}    
    }
    private void accountMenu(Account a){
        ArrayList<Email> E = a.getMailbox();
        int receiverMailID,emailID;
        String s_emailID,r,subj,b;
        boolean flag;
        try{
            out.writeUTF('\n' + message + '\n' + "---------------" + '\n' + ">NewEmail" + '\n' + ">ShowEmails" 
            + '\n' + ">ReadEmail" + '\n' + ">DeleteEmail" + '\n' + ">LogOut" + '\n' + ">Exit" + '\n'+  "---------------");
            message="";
            String command = in.readUTF();
            if (command.equalsIgnoreCase("NewEmail")){
                out.writeUTF("Type the username of the receiver :");
                r = in.readUTF();
                Account receiver = s.findAccount(r);
                if (receiver != null){ //the receiver exists
                    out.writeUTF("Type the subject of the email :");
                    subj = in.readUTF();
                    out.writeUTF("Type the body of your email :");
                    b = in.readUTF();
                    receiverMailID = receiver.getID();
                    Email email = new Email(receiverMailID,a.getUsername(),r,subj,b);
                    receiver.addEmail(email);
                    message="Email Succesfully sent";
                }
                else
                {
                    message="The username of the receiver does not exist";
                } 
            }
            else if (command.equalsIgnoreCase("ShowEmails")){
                message="ID       From                    Subject" + '\n';
                for (Email e: E)
                    message = message.concat(e.getID() + "  " + e.getIsNew() + " " + e.getSender() + "        "+ e.getSubject() + '\n');
            }
            else if (command.equalsIgnoreCase("LogOut")){
                message = "";
                mainMenu();
            }
            else if (command.equalsIgnoreCase("ReadEmail")){
                out.writeUTF("Type the ID of the email you want to read :");
                s_emailID = in.readUTF();
                emailID = Integer.parseInt(s_emailID);
                flag = false;
                for (Email e: E){
                    if (e.getID() == emailID){
                        flag = true;
                        e.setIsNew(false);
                        message = e.getBody();
                    }
                }
                if (flag == false){
                    message = "There is not an email with the ID you entered";
                }
            }
            else if (command.equalsIgnoreCase("DeleteEmail")){
                out.writeUTF("Type the ID of the email you want to delete :");
                s_emailID = in.readUTF();
                emailID = Integer.parseInt(s_emailID);
                flag = false;
                for (Email e: E){
                    if (e.getID() == emailID){
                        flag = true;
                        E.remove(e);
                        message = "Email succesfully deleted";
                        break;
                    }
                }
                if (flag == false){
                    message = "There is not an email with the ID you entered";
                }
            }
            else if (command.equalsIgnoreCase("Exit")){
                try{clientSocket.close();}catch(IOException e){System.out.println(e.getMessage());}
            }
            else{
                message = "That is not a valid command";
            }
        accountMenu(a);
        }catch(EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch(IOException e){System.out.println(e.getMessage());}
    }
}