package com.shekharkg.bhsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shekharkg.bhsms.async.GetConversationList;
import com.shekharkg.bhsms.async.RetrieveSMS;
import com.shekharkg.bhsms.bean.ConversationModel;
import com.shekharkg.bhsms.bean.SmsModel;
import com.shekharkg.bhsms.storage.StorageHelper;
import com.shekharkg.bhsms.utils.CallBack;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements CallBack {

  private StorageHelper storageHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    storageHelper = StorageHelper.singleInstance(this);
    if (storageHelper.getMessageCount() == 0) {
      new RetrieveSMS(this, this).execute();
    } else {
//      new GetConversationList(this, this);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    registerReceiver(broadcastReceiver, new IntentFilter("newMessageReceived"));
  }

  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle b = intent.getExtras();
      String smsModel = b.getString("smsModel");
      Log.e("smsModel", smsModel);
    }
  };

  @Override
  protected void onStop() {
    super.onStop();
    unregisterReceiver(broadcastReceiver);
  }

  private void logSms(List<SmsModel> allMessages) {
    for (SmsModel model : allMessages) {
      Log.i("SMS", model.get_id() + ", From : " + model.getAddress() + ", Message : " + model.getMessage()
          + ", Status : " + model.getReadState() + ", Time : " + model.getTimeStamp() + ", IsInbox : " + model.getIsInbox());
    }
  }

  @Override
  public void OnInboxSuccessfullyRead(List<SmsModel> smsModels) {

  }

  @Override
  public void OnConversationListCreated(List<ConversationModel> conversationModels) {
    if (conversationModels != null && conversationModels.size() > 0) {
      //Populate list
    }
  }
}
