package com.yx.sreditor;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yx.sreditor.Utils.StringToSixthUtils;
import com.yx.sreditor.Utils.file;
import com.yx.sreditor.Utils.utils;
import com.yx.sreditor.View.Design;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private LinearLayout Layout_Design;
    private boolean show = false,Text_Color = false;
    private EditText EditText_Code;
    private int Mcursor=0;
    private String shuruqiandezhi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //视图界面
        Layout_Design = findViewById(R.id.Layout_Design);
        //代码编辑界面
        EditText_Code = findViewById(R.id.EditText_Code);
        //监听代码EditText输入变化
        EditText_Code.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
                Mcursor = EditText_Code.getSelectionStart();
                shuruqiandezhi = EditText_Code.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if(EditText_Code.getSelectionStart()!=0){
                    if(Mcursor > EditText_Code.getSelectionStart()){
                        //Log.e("输入值",Mcursor+"/"+EditText_Code.getSelectionStart());
                        String delete =shuruqiandezhi.substring(EditText_Code.getSelectionStart(),Mcursor);
                        Log.e("删除值",delete);
                        if(delete.indexOf("\"")!=-1 | delete.indexOf("=")!=-1 | delete.indexOf("<")!=-1 | delete.indexOf(">")!=-1| delete.length()>2){
                            code_color();
                        }
                    }else if(Mcursor < EditText_Code.getSelectionStart()){
                        String input_text = EditText_Code.getText().toString().substring(Mcursor,EditText_Code.getSelectionStart());
                        Log.e("输入值",input_text);
                        if(input_text.indexOf("\"")!=-1 | input_text.indexOf("=")!=-1 | input_text.indexOf("<")!=-1 | input_text.indexOf(">")!=-1| input_text.length()>2){
                            code_color();
                        }
                    }
                }


                if(!Text_Color){//判断是否是非是手动输入
                    try {
                        //初始化代码颜色
                        //

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //每次监听完确认非手动输入关闭
                    Text_Color=false;
                }
            }
        });
        //初始化界面
        if(show){//如果视图界面显示
            //显示视图界面
            Layout_Design.setVisibility(View.VISIBLE);
            //隐藏代码界面
            EditText_Code.setVisibility(View.INVISIBLE);
        }else {
            //隐藏视图界面
            Layout_Design.setVisibility(View.INVISIBLE);
            //显示代码界面
            EditText_Code.setVisibility(View.VISIBLE);
        }
        try {
            //读取本地文件
            EditText_Code.setText(file.read(this,"SmolarSystem.xml"));
            code_color();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //FloatingActionButton监听
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show){//如果视图界面不显示
                    if(EditText_Code.getText().toString()!=null && !EditText_Code.getText().toString().equals("")){
                        //向视图传输代码
                        Design.setMessage(EditText_Code.getText().toString());

                        //显示视图界面
                        Layout_Design.setVisibility(View.VISIBLE);
                        //隐藏代码界面
                        EditText_Code.setVisibility(View.INVISIBLE);
                        //设置视图界面显示
                        show = true;
                    }else {
                        Snackbar.make(view, "代码错误", Snackbar.LENGTH_LONG)
                                .setAction("确定", null).show();
                    }
                }else {
                    //隐藏视图界面
                    Layout_Design.setVisibility(View.INVISIBLE);
                    //显示代码界面
                    EditText_Code.setVisibility(View.VISIBLE);
                    //设置视图界面不显示
                    show = false;
                }
            }
        });
    }
    /**
     * 初始化代码颜色
     */

    private void code_color() {
        try{
            String message = EditText_Code.getText().toString();
            //获取光标位置
            int cursor = EditText_Code.getSelectionStart();
            //设置非手动输入
            Text_Color = true;

            SpannableStringBuilder style=new SpannableStringBuilder(message);
            int int_1;
            int int_2;
            int int_color_0=0;;
            //注释颜色
            while (message.indexOf("<!--")!=-1 && message.indexOf("-->")!=-1){
                style.setSpan(new ForegroundColorSpan(Color.rgb(180,180,180 )),message.indexOf("<!--"),message.indexOf("-->")+3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                String message2 = utils.substring(message,"<!--","-->");
                message2 = "<!--" + message2 + "-->";
                message = StringToSixthUtils.decode(StringToSixthUtils.encode(message).replaceFirst(StringToSixthUtils.encode(message2), StringToSixthUtils.encode(utils.text_null(message2.length()))));
            }
            //代码颜色
            while (message.indexOf("<")<message.indexOf(">")){
                String message2 = utils.substring(message,"<",">");
                message2 = "<" + message2 + ">";
                int_color_0 = message.indexOf("<");
                if(message2.indexOf(" ")!=-1){
                    //<开头颜色
                    style.setSpan(new ForegroundColorSpan(Color.rgb(0,0,158)),int_color_0,int_color_0+message2.indexOf(" "),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    //项的颜色
                    if(message2.indexOf(" ")<message2.indexOf("=")){
                        int_1 = 0;
                        int_2 = message2.indexOf("=");
                        for (int i=0;i<utils.appearNumber(message2,"=");i++){
                            if(int_2>int_1 && int_1!=-1){
                                int_1 = message2.indexOf(" ",int_1)+1;
                                int_2 = message2.indexOf("=",int_2)+1;
                                style.setSpan(new ForegroundColorSpan(Color.rgb(11,11,205)),int_color_0+int_1,int_color_0+int_2,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            }
                        }
                    }
                    //值的颜色
                    if(message2.indexOf("\"")<message2.indexOf("\"",message2.indexOf("\"")+1)){
                        int_1 = 0;
                        int_2 = 1;
                        for (int i=0;i<utils.appearNumber(message2,"\"");i++){
                            if(int_2>int_1 && int_1!=-1){
                                if(i==0){//第一次数据初始化
                                    int_1 = message2.indexOf("\"",0);
                                }else {
                                    int_1 = message2.indexOf("\"",int_2+1);
                                }
                                int_2 = message2.indexOf("\"",int_1+1)+1;
                                if(int_2>int_1 && int_1!=-1){
                                    style.setSpan(new ForegroundColorSpan(Color.rgb(0,128 ,0)),int_color_0+int_1,int_color_0+int_2,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                }
                            }
                        }
                    }
                }else {
                    //>结尾颜色
                    style.setSpan(new ForegroundColorSpan(Color.rgb(	0,0,158 )),int_color_0,int_color_0+message2.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                try {
                    //替换
                    message = StringToSixthUtils.decode(StringToSixthUtils.encode(message).replaceFirst(StringToSixthUtils.encode(message2), StringToSixthUtils.encode(utils.text_null(message2.length()))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            EditText_Code.setText(style);
            //置光标位置
            EditText_Code.setSelection(cursor);
            Text_Color=false;
        }catch (Exception e){

        }
    }
}
