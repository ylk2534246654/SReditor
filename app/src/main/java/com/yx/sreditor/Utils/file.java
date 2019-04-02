package com.yx.sreditor.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yx on 2019/3/25.
 */
/*
需要请求权限及配置权限
AndroidManifest.xml
<!-- SDCard中创建与删除文件权限 -->
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
<!--读取SDCard数据权限-->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<!-- 向SDCard写入数据权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
---------------------------
Main
if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
}
ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
 */
public class file {
    /**
     *
     * @param context
     * @param filename 文件名称
     * @return
     * @throws IOException
     */
    public static String read(Context context, String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.jundroo.simplerockets/files/"+filename;
            //filename = context.getExternalCacheDir().getAbsolutePath() + java.io.File.separator + path;
            //打开文件输入流
            FileInputStream inputStream = new FileInputStream(path);

            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            //读取文件内容
            while(len > 0){
                sb.append(new String(buffer,0,len));
                //继续将数据放到buffer中
                len = inputStream.read(buffer);
            }
            //关闭输入流
            inputStream.close();
        }
        return sb.toString();
    }

    /**
     *
     * @param filename 文件名称
     * @param data
     */
    public static void write(String filename,String data) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.jundroo.simplerockets/files/"+filename;
            //新建一个File对象，把我们要建的文件路径传进去。
            java.io.File file = new java.io.File(path);
            //判断文件是否存在，如果存在就删除。
            if (file.exists()) {
                file.delete();
            }
            try {
                //通过文件的对象file的createNewFile()方法来创建文件
                file.createNewFile();
                //新建一个FileOutputStream()，把文件的路径传进去
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                //给定一个字符串，将其转换成字节数组
                byte[] bytes = data.getBytes();
                //通过输出流对象写入字节数组
                fileOutputStream.write(bytes);
                //关流
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
