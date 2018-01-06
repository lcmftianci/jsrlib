package remoteview;

import jsrlib.JsrLog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerView {
    private static Socket sockClient;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private static JLabel imgamsphere;
    private static ImageIcon iIcon;

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        try {
            OpenServer();
            ShowUI();
            while(true){
                ReveivePic();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ois.close();
            sockClient.close();
        }
    }

    private static void ReveivePic() throws ClassNotFoundException, IOException{
        Message msg = (Message)ois.readObject();
        FileOutputStream fos = new FileOutputStream("E:\\output\\" + msg.getFileName());
        fos.write(msg.getFileContent(), 0, (int)msg.getFileLength());
        fos.flush();
        FileInputStream fis = new FileInputStream("E:\\output\\" + msg.getFileName());
        BufferedImage bi = ImageIO.read(fis);
        iIcon = new ImageIcon(bi);
        Image img = iIcon.getImage();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension ds = tk.getScreenSize();
        int w = ds.width;
        int h = ds.height;
        BufferedImage bi1 = resize(img, 800, 600);
        imgamsphere.setIcon(new ImageIcon(bi1));
        imgamsphere.repaint();
    }

    private static BufferedImage resize(Image img, int newW, int newH) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage dimg = new BufferedImage(newW, newH,BufferedImage.TYPE_INT_BGR);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    public static void setListener( JFrame frame){
        //panel设置监听器
        frame.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                sendEventObject(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                sendEventObject(e);
            }
            @Override
            public void keyTyped(KeyEvent e) {

            }
        });
        frame.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e) {
                sendEventObject(e);
            }
        });
        frame.addMouseMotionListener(new MouseMotionListener(){
            public void mouseDragged(MouseEvent e) {
                sendEventObject(e);
            }
            public void mouseMoved(MouseEvent e) {
                sendEventObject(e);

            }
        });
        frame.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e) {
                sendEventObject(e);
            }
            public void mouseEntered(MouseEvent e) {
                sendEventObject(e);
            }
            public void mouseExited(MouseEvent e) {
                sendEventObject(e);
            }
            public void mousePressed(MouseEvent e) {
                sendEventObject(e);
            }
            public void mouseReleased(MouseEvent e) {
                sendEventObject(e);
            }
        });
    }

    private static void sendEventObject(InputEvent event){
        try{
            System.out.println("send");
            oos.writeObject(event);
            oos.flush();
        }catch(Exception ef){
            ef.printStackTrace();
        }
    }

    private static void ShowUI() {
        //控制台标题
        JFrame jf = new JFrame("控制台");
        setListener(jf);
        //控制台大小
        jf.setSize(500, 400);
        //imag_lab用于存放画面
        imgamsphere = new JLabel();
        jf.add(imgamsphere);
        //设置控制台可见
        jf.setVisible(true);
        //控制台置顶
        jf.setAlwaysOnTop(true);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void OpenServer() throws IOException, ClassNotFoundException {
        JsrLog.Logout("Server Start!");
        ServerSocket server = new ServerSocket(6002);
        sockClient = server.accept();
        JsrLog.Logout("access success!");
        ois = new ObjectInputStream(sockClient.getInputStream());
        oos = new ObjectOutputStream(sockClient.getOutputStream());
    }
}
