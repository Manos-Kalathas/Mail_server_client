package server;

import java.util.ArrayList;

public class Account {
    private String username;
    private String password;
    private ArrayList<Email> mailbox;
    public Account(String u,String p){
        username = u;
        password = p;
        mailbox = new ArrayList<>();
    }
    public void addEmail(Email e){
        mailbox.add(e);
    }
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public int getID(){return mailbox.size();}
    public ArrayList<Email> getMailbox(){return mailbox;}
    //public void DeleteEmail(Email e){mailbox.remove(e);}
}
