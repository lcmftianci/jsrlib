package remoteview;

import jsrlib.JsrLog;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {

    private static Socket socket;
    private static ObjectOutputStream oos ;
    private static ObjectInputStream ois;
    private static Robot robot;

    public static void main(String[] args) throws UnknownHostException, IOException, AWTException{
      try {
          StartConnection("s", 1);
          MsgThread robot = new MsgThread(ois);
          Thread t = new Thread(robot, "robot");
          t.start();
          while(true){
              CapturePic();
          }
      }catch (Exception e){
          oos.close();
          socket.close();
      }
    }

    public static void StartConnection(String IP,int port) throws UnknownHostException, IOException, AWTException {
       Socket socket = new Socket("192.168.0.106",7777);
        if(socket.isConnected()){
            JsrLog.Logout("socket connected..."+socket);
        }
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public static void CapturePic() throws AWTException, IOException{
        robot = new Robot();
        Message msg = null;
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dm = tk.getScreenSize();
        Robot robot = new Robot();

        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();

        String desktopPath = desktopDir.getAbsolutePath();

        desktopPath += "ScreenCut\\";

        for(int i = 0; i < 50; i++){
            Rectangle rec = new Rectangle(0, 0, (int)dm.getWidth(), (int)dm.getHeight());
            BufferedImage bImg = robot.createScreenCapture(rec);
            String filePath = desktopPath + "Screen" + i + ".jpeg";
            FileOutputStream fops = new FileOutputStream(filePath);
            ImageIO.write(bImg, "jpeg", fops);
            fops.flush();
            fops.close();
            msg = new Message(filePath);
            JsrLog.Logout(msg.getFileName());
            JsrLog.Logout("send");
            oos.writeObject(msg);
            oos.flush();
        }
    }

    public static void Close() throws IOException{
        oos.flush();
        oos.close();
        socket.close();
    }
}
