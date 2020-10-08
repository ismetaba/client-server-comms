package test.clientserver;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    private int port = 49999;
    private int port2 = 49998;
    private Socket s = null;
    private OutputStreamWriter out;
    private BufferedReader in;
    private ServerSocket ss;


    @Test
    void testReceiveMessage() throws IOException {

        ServerSocket ss = new ServerSocket(port);
        String receivedMessage;
        s = ss.accept();
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        receivedMessage = in.readLine();
        s.close();
        ss.close();

        s = new Socket("localhost",port2);
        out = new OutputStreamWriter(s.getOutputStream());
        out.write("OK");
        out.close();
        assertEquals("a message",receivedMessage);
        }


}