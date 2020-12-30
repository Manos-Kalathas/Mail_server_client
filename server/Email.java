package server;

public class Email {
    private int ID;
    private String sender;
    private String receiver;
    private String subject;
    private String mainbody;
    private boolean isNew;
    public Email(int ID,String s,String r,String subj,String m){
        this.ID = ID;
        sender = s;
        receiver = r;
        subject = subj;
        mainbody = m;
        isNew = true;
    }
    public String getSender(){return sender;}
    public int getID(){return ID;}
    public String getReceiver(){return receiver;}
    public String getSubject(){return subject;}
    public String getBody(){return mainbody;}
    public String getIsNew(){
        if (isNew)
            return "[NEW]";
        else
            return "     ";
    }
    public void setIsNew(boolean b){isNew = b;} 
}
