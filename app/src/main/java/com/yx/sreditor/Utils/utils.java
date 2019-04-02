package com.yx.sreditor.Utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by 雨夏 on 2019/3/25.
 */

public class utils {
    private static int mapi=0,shangyige=0,Children=0;
    private static String Color_r,Color_g,Color_b;
    public static String substring(String str, String arg0, String arg1) {
        String str2 = null;
        int int_1 = str.indexOf(arg0) + arg0.length();// 获取右边指针
        int int_2 = str.indexOf(arg1, int_1);// 获取左边指针
        str2 = str.substring(int_1, int_2);// 根据指针获取指定值
        if (str2.equals("")) {
            return null;
        }
        return str2;
    }

    public static String text_null(int str) {
        String str2 = "";
        for (int i = 0; i < str; i++) {
            str2 = str2 + "-";
        }
        return str2;
    }

    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }

    public static Map<Integer,Map<String,String>> parseXMLWithPull(String result) throws Exception {
        Map<Integer,Map<String,String>> map = new HashMap<>();
        Map<String,String> map2 = new HashMap<String,String>();
        mapi=0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));
            String name = "";
            String gravity = "";
            String radius = "";
            String e = "";
            String w = "";
            String prograde = "";
            String a = "";
            String v = "";
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG://开始解析
                        if("Children".equals(nodeName)){
                            Children++;
                            Log.e(TAG, "Children++");
                        }

                        if ("Planet".equals(nodeName)){
                            Log.e(TAG, "Planet");
                            map2 = new HashMap<String,String>();
                            name = parser.getAttributeValue(null, "name");
                            gravity = parser.getAttributeValue(null, "gravity");
                            radius = parser.getAttributeValue(null, "radius");
                            if(parser.getAttributeValue(null, "mapColor")!=null){
                                String[] str = parser.getAttributeValue(null, "mapColor").split(",");
                                Color_r = str[0];
                                Color_g = str[1];
                                Color_b = str[2];
                            }
                            map2.put("Children",Children+"");

                            map2.put("name",name);
                            map2.put("gravity",gravity);
                            map2.put("radius",radius);
                            map2.put("Color_r",Color_r);
                            map2.put("Color_g",Color_g);
                            map2.put("Color_b",Color_b);
                            if(Children==0){//sun
                                mapi++;
                                map2.put("i",mapi+"");
                                map.put(mapi,map2);
                                map2 = new HashMap<String,String>();
                            }
                        }
                        if ("Orbit".equals(nodeName)){

                            w = parser.getAttributeValue(null, "w");
                            e = parser.getAttributeValue(null, "e");
                            prograde = parser.getAttributeValue(null, "prograde");
                            a = parser.getAttributeValue(null, "a");
                            v = parser.getAttributeValue(null, "v");

                            map2.put("w",w);
                            map2.put("e",e);
                            map2.put("prograde",prograde);
                            map2.put("a",a);
                            map2.put("v",v);
                            mapi=mapi+2;
                            if(Children==1){
                                shangyige = mapi;
                            }
                            map2.put("i",shangyige+"");
                            map.put(mapi,map2);
                            map2 = new HashMap<String,String>();
                        }
                        //Log.e(TAG, "<"+nodeName +">");
                        Log.e(TAG, "<"+nodeName +">"+"name:"+parser.getAttributeValue(null, "name"));
                        break;
                    case XmlPullParser.END_TAG://完成解析
                        Log.e(TAG, "</"+nodeName +">");
                        if("Children".equals(nodeName)){
                            Children--;
                            Log.e(TAG, "Children--");
                        }

                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
