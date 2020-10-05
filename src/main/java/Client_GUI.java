// GG:
// Paket yok. Default paket kullanımı yerine belirgin bir paket altında olmalı tüm kod.
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author ismet abacı
 */

// GG:
// Sınıf isimlerinde _ gibi karakterler genellikle C# stilidir ve Java konvensiyonunda kullanılmazlar.
public class Client_GUI extends JFrame{
    private JTextField senderField;
    private JTextField receiverField;
    private JTextField ccField;
    private JTextField subjectField;
    private JComboBox priorityField;
    private JButton sendButton;
    private JTextArea messageField;
    private JPanel mainPanel;
    final static Logger logger = Logger.getLogger(String.valueOf(Client_GUI.class));

    public Client_GUI() {
        sendButton.addActionListener(new ActionListener() {

            /**
             * This method listens the button. When pressed, it sends a message to server or it asks user to fill empty fields
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                if(fieldChecker()){
                    // GG:
                    // Mesaj nesnesinin oluşturulması bir başka metotta olsa 
                    // okunabilirlik ve cohesion açısından daha iyi olur.
                    JSONObject msg = new JSONObject();
                    msg.put("sender",senderField.getText());
                    msg.put("receiver",receiverField.getText());
                    msg.put("cc",ccField.getText());
                    msg.put("subject",subjectField.getText());
                    msg.put("message",messageField.getText());
                    msg.put("priority",getPriority(priorityField.getSelectedIndex()));

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
                // GG:
                // Her seferinde bağlantı açıp server'ın yanıtını beklemeden kapatıyorsun. Bunun yerine bağlantıyı
                // bir kere açmalı ve server ile iletişim kapsamında devamlı açık tutmalısın.
                int port = 49999;
                // GG:
                // try, catch, for vs keywordler öncesi boşluk olmalı
                try{
                    Socket s = new Socket("localhost",port);
                    OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
                    out.write(msg.toString());
                    out.close();
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
             * this method checks the field, if it finds necessery fields empty it returns false, else it returns ture
             */
            private boolean fieldChecker() {
                return senderField.getText() != null && receiverField.getText() != null && (priorityField.getSelectedIndex() != 0);
            }
        });
    }

    /**
     * this method sets the frame for the program
     */
    public static void main(String[] args) {
        // GG:
        // Frame burada değil, Client_GUI içinde oluşturulmalı.
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Client_GUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
