package com.practice.noyet.chatroom;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.practice.noyet.chatroom.socket.ChatClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /** 带清除标志的客户端A的数据输入框 */
    private ClearEditText aContent;
    /** 客户端A的数据提交按钮 */
    private TextView aSend;
    /** 带清除标志的客户端B的数据输入框 */
    private ClearEditText bContent;
    /** 客户端B的数据提交按钮 */
    private TextView bSend;
    /** 显示从服务器端返回的数据 */
    private ListView mListView;

    /** 用户A的客户端 */
    private ChatClient clientA;
    /** 用户B的客户端 */
    private ChatClient clientB;

    private ArrayAdapter<String> mAdapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        new MyAsyncTask().execute(1);
        aContent = (ClearEditText) findViewById(R.id.a_client_cet);
        aSend = (TextView) findViewById(R.id.a_client_send);
        bContent = (ClearEditText) findViewById(R.id.b_client_cet);
        bSend = (TextView) findViewById(R.id.b_client_send);
        mListView = (ListView) findViewById(R.id.chat_content_listview);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        aSend.setOnClickListener(this);
        bSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.a_client_send:
                //向服务器发送消息
                clientA.sendChatMsg(aContent.getText().toString());
                //获取从服务器返回的数据
                new MyAsyncTask().execute(2);
                break;
            case R.id.b_client_send:
                //向服务器发送消息
                clientB.sendChatMsg(bContent.getText().toString());
                //获取从服务器返回的数据
                new MyAsyncTask().execute(3);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clientA.destroySocket();
        clientB.destroySocket();
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            if (integers[0] == 1) {
                clientA = new ChatClient();
                clientB = new ChatClient();
            } else if (integers[0] == 2) {
                list.add("Client A: " + clientA.getChatMsg());
            } else if (integers[0] == 3) {
                list.add("Client B: " + clientA.getChatMsg());
            }
            return integers[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 2 || integer == 3) {
                mAdapter.notifyDataSetChanged();
            }
        }

    }
}
