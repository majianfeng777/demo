package com.example.sqlconnect_test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class testmain extends AppCompatActivity {
    private TextView response;
    PrintWriter outputStream;
    private EditText ip,port,text_post;
    private Button post,get,btn_conn;
    private Connect coonect;
    private Socket socket;
    BufferedReader inputStream;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        response=(TextView) findViewById(R.id.text_response);
        ip=(EditText) findViewById(R.id.text_ip);
        text_post=(EditText) findViewById(R.id.text_post);
        port=(EditText) findViewById(R.id.text_port);
        post=(Button) findViewById(R.id.btn_post);
        get=(Button) findViewById(R.id.btn_get);
        btn_conn=(Button) findViewById(R.id.btn_conn);

        btn_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            coonect=new Connect(ip.getText().toString(),Integer.valueOf(port.getText().toString()));
                            if (coonect.socket.isConnected()){
                                response.setText("连接成功");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            coonect.post(text_post.getText().toString());
                            response.setText("发送成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    StringBuilder sb;
                    @Override
                    public void run() {
                        InputStream inputStream = null;
                        try {
//                            inputStream = coonect.socket.getInputStream();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//                            String reply = null;
//                             sb = new StringBuilder();
//                            while (!((reply = br.readLine()) == null)) {
//                                sb.append(reply);
//                            }
                            Message message=new Message();
                            message.what=1;
                            String[] mdata = new String[]{};
                            String[] data=coonect.get().split(",");
                            for (int i=0;i<data.length;i++){
                                 mdata=data[i].split("_");
                                 message.obj=mdata[0];
                            }
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what==1){
                response.setText(msg.obj.toString());
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            coonect.relase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
