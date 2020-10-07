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
    private Socket s;
    private JTextField receiverField;
    private JTextField ccField;
    private JTextField subjectField;
    private JComboBox priorityField;
    private JButton sendButton;
    private JTextArea messageField;
    private JPanel mainPanel;
    final static Logger logger = Logger.getLogger(String.valueOf(ClientGUI.class));

    public ClientGUI()  {

        final Socket s;
        try {
            s = new Socket("localhost",port);
        } catch (IOException e) {
            e.printStackTrace();
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
             * @param selectedIndex this is the selected index of priority box
             * @return the priority
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
             * this method clears the fields after sending a message to server
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
             * this method checks the field, if it finds necessary fields empty it returns false, else it returns ture
             */
            private boolean fieldChecker() {
                return senderField.getText() != null && receiverField.getText() != null && (priorityField.getSelectedIndex() != 0);
            }

            /**
             * @return it returns the message as JSONObject
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
     * this method sets the frame for the program
     */
    public static void main(String[] args) throws IOException {
        frame = new JFrame("App");
        frame.setContentPane(new ClientGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
