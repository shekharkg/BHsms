package com.shekharkg.bhsms.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.gson.Gson;
import com.shekharkg.bhsms.bean.SmsModel;
import com.shekharkg.bhsms.storage.StorageHelper;

/**
 * Created by ShekharKG on 4/29/2016.
 */
public class IncomingSms extends BroadcastReceiver {

  public void onReceive(Context context, Intent intent) {

    SmsModel newSmsModel = null;

    Log.e("smsModel", "New SMS received");

    final Bundle bundle = intent.getExtras();
    try {
      if (bundle != null) {
        final Object[] pdusObj = (Object[]) bundle.get("pdus");
        for (Object aPdusObj : pdusObj) {

          SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
          SmsModel smsModel = new SmsModel();
          smsModel.set_id(String.valueOf(currentMessage.getStatusOnIcc()));
          smsModel.setAddress(currentMessage.getDisplayOriginatingAddress());
          smsModel.setMessage(currentMessage.getDisplayMessageBody());
          smsModel.setReadState(0);
          smsModel.setTimeStamp(currentMessage.getTimestampMillis());
          smsModel.setIsInbox(1);

          StorageHelper.singleInstance(context).addMessage(smsModel);
          newSmsModel = smsModel;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    if (newSmsModel != null) {
      Intent newSmsIntent = new Intent("newMessageReceived");
      newSmsIntent.putExtra("smsModel", new Gson().toJson(newSmsModel));
      context.sendBroadcast(newSmsIntent);
    }
  }


}
