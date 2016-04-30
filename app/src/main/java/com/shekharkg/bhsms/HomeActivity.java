package com.shekharkg.bhsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.shekharkg.bhsms.adapter.ConversationAdapter;
import com.shekharkg.bhsms.async.RetrieveSMS;
import com.shekharkg.bhsms.bean.ConversationModel;
import com.shekharkg.bhsms.bean.SmsModel;
import com.shekharkg.bhsms.storage.StorageHelper;
import com.shekharkg.bhsms.utils.CallBack;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements CallBack {

  private StorageHelper storageHelper;
  private ListView conversationLV;
  private ConversationAdapter conversationAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    conversationLV = (ListView) findViewById(R.id.listView);

    storageHelper = StorageHelper.singleInstance(this);
    if (storageHelper.getMessageCount() == 0)
      new RetrieveSMS(this, this).execute();
    else
      populateWithConversationList(storageHelper.getConversationList());

  }


  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(broadcastReceiver, new IntentFilter("newMessageReceived"));
  }

  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle b = intent.getExtras();
      String smsDetails = b.getString("smsModel");
      Log.e("smsModel", smsDetails);
      SmsModel smsModel = new Gson().fromJson(smsDetails, SmsModel.class);
      if (smsModel == null || smsModel.getAddress() == null)
        return;

      boolean isSenderAlreadyExists = false;

      if (conversationAdapter != null) {
        for (ConversationModel model : conversationAdapter.conversationModels) {
          if (model.getAddress().equals(smsModel.getAddress())) {
            model.setLastMessage(smsModel.getMessage());
            model.setTimeStamp(smsModel.getTimeStamp());
            model.setReadStatus(0);
            model.setIsInbox(1);
            conversationAdapter.conversationModels.remove(model);
            conversationAdapter.conversationModels.add(0, model);
            isSenderAlreadyExists = true;
            break;
          }
        }
        if (!isSenderAlreadyExists) {
          ConversationModel conversationModel = new ConversationModel();
          conversationModel.setAddress(smsModel.getAddress());
          conversationModel.setLastMessage(smsModel.getMessage());
          conversationModel.setTimeStamp(smsModel.getTimeStamp());
          conversationModel.setIsInbox(1);
          conversationModel.setReadStatus(0);
          conversationAdapter.conversationModels.add(0, conversationModel);
        }

        conversationAdapter.notifyDataSetChanged();
      } else
        populateWithConversationList(storageHelper.getConversationList());
    }
  };

  @Override
  protected void onPause() {
    super.onPause();
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
    populateWithConversationList(storageHelper.getConversationList());
  }

  public void populateWithConversationList(List<ConversationModel> conversationModels) {
    findViewById(R.id.loader).setVisibility(View.GONE);
    if (conversationModels != null && conversationModels.size() > 0) {
      conversationAdapter = new ConversationAdapter(this, conversationModels);
      conversationLV.setAdapter(conversationAdapter);

      conversationLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          storageHelper.updateReadStatus(conversationAdapter.conversationModels.get(position).getAddress());
          conversationAdapter.conversationModels.get(position).setReadStatus(1);
          conversationAdapter.notifyDataSetChanged();

          Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
          intent.putExtra("address", conversationAdapter.conversationModels.get(position).getAddress());
          startActivity(intent);
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
      });
    } else
      findViewById(R.id.noSmsFoundTV).setVisibility(View.VISIBLE);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
  }
}
