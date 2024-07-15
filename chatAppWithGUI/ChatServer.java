package chatAppWithGUI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    List<ClientHandler> clients = new ArrayList<>();
    
    ServerSocket server;
     public void run(){
        try {
            server = new ServerSocket(5000);
            while(true){
                Socket clientSocket = server.accept();
                System.out.println("client accepted: " + clientSocket);
                ClientHandler clientThread = new ClientHandler(clientSocket, clients);
                clients.add(clientThread);
                new Thread(clientThread).start();
            }
        } catch (IOException e) {
            System.out.println("Error occured: " + e.getMessage());
        }
    }
    public static void main(String... args) {
        JavaNetTrial jt = new JavaNetTrial();
        jt.run();
    }
}
class ClientHandler implements Runnable{
    List<ClientHandler> clients;
    Socket socket;
    BufferedReader input;
    DataOutputStream output;
    public ClientHandler(Socket client, List<ClientHandler> clients){
        this.clients = clients;
        this.socket = client;
        try{
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new DataOutputStream(socket.getOutputStream());
    
        } catch(IOException ioe){
            System.out.println("error from client Handler: " + ioe.getMessage());
        }
    }

    @Override
    public void run(){
        try{
            while (true) {
            String message = input.readLine(); 
                if(!message.isBlank()){
                for(ClientHandler client : clients){
                    client.output.writeUTF(message);
                }
            }
            }
        }catch(IOException io){
            System.out.println(io.getMessage());
        }finally{
            try{
               input.close();
            //    output.close();
               socket.close();
            }catch(IOException ioE){
                System.out.println("Not able to close" + ioE.getMessage());
            }
        }
    }
}
