package test.clientserver;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ClientGUITest {
    private String address = "lochalhost";
    private int port = 49999;
    private int port2 = 49998;
    private Socket s = null;
    private OutputStreamWriter out;
    private BufferedReader in;
    private ServerSocket ss;

    @Test
    void testClientSend() throws IOException, InterruptedException {
        String response;
        s = new Socket("localhost",port);
        OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
        out.write("a message");
        out.close();
        s.close();

        ServerSocket ss = new ServerSocket(port2);
        s = ss.accept();
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        response = in.readLine();
        assertEquals("OK",response);
    }
}