package chatAppWithGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class ClientGUI extends JFrame {
    JTextArea messageArea;
    PrintWriter writer;
    DataInputStream input;
    Socket socket;
     String name;
    public ClientGUI() {
        name = JOptionPane.showInputDialog(this, "Enter your name: ", "Name Entry",PLAIN_MESSAGE);
        try{
        socket = new Socket("192.168.107.165", 5000);
        input = new DataInputStream(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException ioE){
            JOptionPane.showMessageDialog(this, "Failed to connect!", "Fail Message", WARNING_MESSAGE, null);
        }
        
        // creating components of chat app frame
        messageArea = new JTextArea();
        JButton sendButton = new JButton("Send");
        JTextArea messageField = new JTextArea();

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridConstraint = new GridBagConstraints(0, 0, REMAINDER,
                RELATIVE, 1.0, 1.0, NORTHEAST, BOTH, new Insets(4, 4, 4, 4), 0, 0);

        Dimension messageAreaSize = new Dimension(1100, 450);
        Dimension messageFieldSize = new Dimension(1000, 100);
        Dimension sendButtonSize = new Dimension(30, 20);

        // adding bounds to components
        messageArea.setMinimumSize(messageAreaSize);
        messageArea.setPreferredSize(messageAreaSize);
        messageArea.setEditable(false);

        gridBag.setConstraints(messageArea, gridConstraint);

        messageField.setPreferredSize(messageFieldSize);
        // messageField.setMinimumSize(messageFieldSize);

        gridConstraint.gridy += 1.0;
        gridConstraint.gridx += 1.0;
        gridConstraint.gridwidth = RELATIVE;
        gridBag.setConstraints(messageField, gridConstraint);

        sendButton.setMinimumSize(sendButtonSize);
        // sendButton.setPreferredSize(sendButtonSize);
        gridConstraint.gridx += 1.0;
        gridConstraint.gridwidth = REMAINDER;
        gridConstraint.weighty = 0;
        gridConstraint.weightx = 0;
        gridBag.setConstraints(sendButton, gridConstraint);

        messageArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        messageField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                writeMessage(name + ": " + messageField.getText() + "\n");
                messageField.setText("");
            }
        });

        // adding components to JFrame
        add(messageArea);
        add(messageField);
        add(sendButton);

        // setContentPane(scrollPane);
        setTitle("Chat App - " + name);
        setPreferredSize(new Dimension(1400, 650));
        setLayout(gridBag);
        setBackground(new Color(250, 250, 250));
        pack();
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.readMessage();
    }


    public void readMessage() {
        try {
            while(true){
                String message = input.readUTF();
                String receivedMessage = "\t" + (new SimpleDateFormat("HH: MM")).format(new Date()) + 
                " " + message;
                messageArea.append(receivedMessage + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"something went wrong while receiving message");
        }
    }

    public void writeMessage(String message){
            if(!message.isBlank()){
                writer.println(message);
                writer.flush();
            }
               
    }

    
}
