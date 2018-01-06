package remoteview;

import jdk.internal.util.xml.impl.Input;
import jsrlib.JsrLog;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.io.ObjectInputStream;

//消息时间进程

public class MsgThread implements Runnable{
    private ObjectInputStream ois;
    public MsgThread(ObjectInputStream ois){
        this.ois = ois;
    }
    @Override
    public void run() {
        try{
            action();
        }catch (ClassNotFoundException | AWTException |IOException e){
            JsrLog.Logout("start msg failed!");
        }
    }

    public void action() throws AWTException, ClassNotFoundException, IOException{
        Robot robot = new Robot();
        while(true){
            InputEvent e = (InputEvent)ois.readObject();
            if(e != null){
                handleEvents(robot, e);
            }
        }
    }

    public static void handleEvents(Robot action, InputEvent event){
        MouseEvent mouseEvent = null;
        MouseWheelEvent mouseWheelEvent = null;
        KeyEvent keyEvent = null;
        int mbm = -100;
        switch (event.getID()){
            case MouseEvent.MOUSE_MOVED:
                mouseEvent = (MouseEvent)event;
                action.mouseMove(mouseEvent.getX(), mouseEvent.getY());
                break;
            case MouseEvent.MOUSE_PRESSED:
                mouseEvent = (MouseEvent)event;
                action.mouseMove(mouseEvent.getX(), mouseEvent.getY());
                mbm = getMouseClick(mouseEvent.getButton());
                if(mbm != -100)
                    action.mousePress(mbm);
                break;
            case MouseEvent.MOUSE_RELEASED :              //鼠标键松开
                mouseEvent = ( MouseEvent ) event;
                action.mouseMove( mouseEvent.getX() , mouseEvent.getY() );
                mbm = getMouseClick( mouseEvent.getButton() );//取得鼠标按键
                if(mbm != -100)
                    action.mouseRelease( mbm );
                break ;
            case MouseEvent.MOUSE_WHEEL :                  //鼠标滚动
                mouseWheelEvent = ( MouseWheelEvent ) event ;
                action.mouseWheel(mouseWheelEvent.getWheelRotation());
                break ;
            case MouseEvent.MOUSE_DRAGGED :                      //鼠标拖拽
                mouseEvent = ( MouseEvent ) event ;
                action.mouseMove( mouseEvent.getX(), mouseEvent.getY() );
                break ;
            case KeyEvent.KEY_PRESSED :                     //按键
                keyEvent = ( KeyEvent ) event;
                action.keyPress( keyEvent.getKeyCode() );
                break ;
            case KeyEvent.KEY_RELEASED :                    //松键
                keyEvent = ( KeyEvent ) event ;
                action.keyRelease( keyEvent.getKeyCode() );
                break ;
            default: break ;
        }
    }

    private static int getMouseClick(int button){
        if(button == MouseEvent.BUTTON1)    //左键
            return InputEvent.BUTTON1_MASK;
        if(button == MouseEvent.BUTTON3)    //右
            return InputEvent.BUTTON3_MASK;
        if(button == MouseEvent.BUTTON2)
            return InputEvent.BUTTON2_MASK;  //中

        return -100;
    }
}
