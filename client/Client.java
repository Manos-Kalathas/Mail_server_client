package client;
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
	public static void main (String args[]) {
            // arguments supply ip and port
            int port = Integer.parseInt(args[1]);
            try(
                    Scanner input = new Scanner(System.in);
                    Socket s = new Socket(args[0], port); 
                    DataInputStream in = new DataInputStream( s.getInputStream());
                    DataOutputStream out = new DataOutputStream( s.getOutputStream());
                ){
                    while(true){
                        String data = in.readUTF();// read a line of data from the stream
                        System.out.println(data);
                        data = input.nextLine();
                        out.writeUTF(data);
                        if (data.equalsIgnoreCase("Exit")){
                            break;
                        }
                    }
                    s.close();
            }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
            }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
            }catch (IOException e){System.out.println(e.getMessage());}
     }
}