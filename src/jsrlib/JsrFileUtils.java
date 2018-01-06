package jsrlib;

import java.io.File;
import java.io.IOException;

public class JsrFileUtils {
    public static boolean createFile(String deskFileName){
        File file = new File(deskFileName);
        if(file.exists()){
            JsrLog.Logout("创建文件："+ deskFileName + "失败，文件已存在");
            return false;
        }

        if(deskFileName.endsWith(File.separator)){
            JsrLog.Logout("创建文件：" + deskFileName + "失败，目标文件不能是文件夹");
            return false;
       }

       if(!file.getParentFile().exists()){
            JsrLog.Logout("父目录不存在");
            if(!file.getParentFile().mkdirs()){
                JsrLog.Logout("创建目标文件目录失败");
                return false;
            }
       }

       try{
           if(file.createNewFile()){
               JsrLog.Logout("创建文件成功：" +  deskFileName);
               return true;
           }else{
               JsrLog.Logout("创建文件失败：" +  deskFileName);
               return false;
           }
       }catch (IOException e){
           e.printStackTrace();
           JsrLog.Logout("创建文件失败：" + deskFileName + " " + e.getMessage());
           return false;
       }
    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        if (dirName == null) {
            try{
                //在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                //返回临时文件的路径
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建临时文件失败！" + e.getMessage());
                return null;
            }
        } else {
            File dir = new File(dirName);
            //如果临时文件所在目录不存在，首先创建
            if (!dir.exists()) {
                if (!JsrFileUtils.createDir(dirName)) {
                    System.out.println("创建临时文件失败，不能创建临时文件所在的目录！");
                    return null;
                }
            }
            try {
                //在指定目录下创建临时文件
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建临时文件失败！" + e.getMessage());
                return null;
            }
        }
    }
}
