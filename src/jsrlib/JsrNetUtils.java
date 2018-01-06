package jsrlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class JsrNetUtils {

    public static void ServerTest(){
        ServerSocket ss = null;
        Socket socket = null;
        InputStream is = null;
        try {
            ss = new ServerSocket(8090);
            socket = ss.accept();
            is = socket.getInputStream();
            byte[] b = new byte[20];
            int len = 20;
            while((len = is.read(b)) != -1)
            {
                JsrLog.Logout(new String(b, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null) {
                    is.close();
                }
                if(socket != null){
                    socket.close();
                }
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void ClientTest(){
        OutputStream os = null;
        Socket socket = null;
        try {
            socket= new Socket("192.168.100.19",8090);
            os = socket.getOutputStream();
            os.write("hello net".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os != null)
            {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket != null)
            {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
