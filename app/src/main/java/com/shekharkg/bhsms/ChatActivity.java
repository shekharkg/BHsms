package com.shekharkg.bhsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.shekharkg.bhsms.adapter.ChatAdapter;
import com.shekharkg.bhsms.bean.SmsModel;
import com.shekharkg.bhsms.storage.StorageHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ShekharKG on 4/30/2016.
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText messageET;
  private ListView listView;
  private ImageButton sendAction;
  private StorageHelper storageHelper;
  private ChatAdapter chatAdapter;
  private String address;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_chat);

    messageET = (EditText) findViewById(R.id.messageET);
    listView = (ListView) findViewById(R.id.listView);
    sendAction = (ImageButton) findViewById(R.id.sendAction);

    storageHelper = StorageHelper.singleInstance(this);

    address = getIntent().getStringExtra("address");
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(address);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    sendAction.setOnClickListener(this);

    List<SmsModel> models = storageHelper.getAllMessages(address);

    chatAdapter = new ChatAdapter(this, models);
    listView.setAdapter(chatAdapter);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home)
      onBackPressed();
    return super.onOptionsItemSelected(item);
  }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }

  @Override
  public void onClick(View v) {
    if (v == sendAction) {
      String message = messageET.getText().toString().trim();
      if (!message.isEmpty()) {
        SmsModel smsModel = new SmsModel();
        smsModel.setIsInbox(0);
        smsModel.set_id("1");
        smsModel.setTimeStamp(Calendar.getInstance().getTimeInMillis());
        smsModel.setMessage(message);
        smsModel.setReadState(1);
        smsModel.setAddress(address);
        storageHelper.addMessage(smsModel);

        chatAdapter.smsModelList.add(smsModel);
        chatAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(chatAdapter.smsModelList.size() - 1);
      }
    }
  }

  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle b = intent.getExtras();
      String smsDetails = b.getString("smsModel");
      Log.e("smsModel", smsDetails);
      SmsModel smsModel = new Gson().fromJson(smsDetails, SmsModel.class);
    }
  };

  @Override
  protected void onStart() {
    super.onStart();
    registerReceiver(broadcastReceiver, new IntentFilter("newMessageReceived"));
  }

  @Override
  protected void onStop() {
    super.onStop();
    unregisterReceiver(broadcastReceiver);
  }
}
