package com.clientserver;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author ismet abacı
 */
public class ClientGUI extends JFrame{
    static JFrame frame;
    private JTextField senderField;
    private static int port = 49999;
    private JTextField receiverField;
    private JTextField ccField;
    private JTextField subjectField;
    private JComboBox priorityField;
    private JButton sendButton;
    private JTextArea messageField;
    private JPanel mainPanel;
    private final static Logger logger = Logger.getLogger(String.valueOf(ClientGUI.class));

    public ClientGUI()  {

        add(mainPanel);
        setSize(300,400);
        setTitle("Client");

        final Socket s;
        try {
            s = new Socket("localhost",port);
        } catch (IOException e) {
            logger.warning("Connection Problem");
            return;
        }

        sendButton.addActionListener(new ActionListener() {

            /**
             * This method listens the button. When pressed, it sends a message to server or it asks user to fill empty fields
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                if(fieldChecker()){
                    JSONObject msg = getMessageAsJSONObject();
                    sendTheMessage(msg);
                    JOptionPane.showMessageDialog(null,"Mail is sent.");
                    emptyTheFields();
                }else{
                    JOptionPane.showMessageDialog(null,"Please fill out the necessary fields.");
                }
            }

            /**
             * This method receives index of selected priority box, the it returns the priority level
             * @param selectedIndex This is the selected index of priority box
             * @return The priority of the message
             */
            private String getPriority(int selectedIndex) {
                switch (selectedIndex){
                    case 1:
                        return "low";
                    case 2:
                        return "normal";
                    case 3 :
                        return "high";
                }
                throw new IllegalArgumentException();
            }

            /**
             * This method sends a message to server.
             */
            private void sendTheMessage(JSONObject msg) {

                try{
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    out.writeUTF(msg.toString());
                    logger.info("OK");
                }catch(Exception e){
                    logger.warning("Error on client -> " + e);
                }
            }

            /**
             * This method clears the fields after sending a message to server
             */
            private void emptyTheFields() {
                senderField.setText(null);
                receiverField.setText(null);
                ccField.setText(null);
                subjectField.setText(null);
                priorityField.setSelectedIndex(0);
                messageField.setText(null);
            }

            /**
             * This method checks the field, if it finds necessary fields empty it returns false, else it returns ture
             */
            private boolean fieldChecker() {
                return senderField.getText() != null && receiverField.getText() != null && (priorityField.getSelectedIndex() != 0);
            }

            /**
             * @return It returns the message as JSONObject
             */
            private JSONObject getMessageAsJSONObject() {
                JSONObject m = new JSONObject();
                m.put("sender",senderField.getText());
                m.put("receiver",receiverField.getText());
                m.put("cc",ccField.getText());
                m.put("subject",subjectField.getText());
                m.put("message",messageField.getText());
                m.put("priority",getPriority(priorityField.getSelectedIndex()));
                return m;
            }
        });
    }

    /**
     * This method sets the frame for the program
     */
    public static void main(String[] args) throws IOException {
        ClientGUI app = new ClientGUI();
        app.setVisible(true);
    }
}
