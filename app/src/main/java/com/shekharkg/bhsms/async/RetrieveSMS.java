package com.shekharkg.bhsms.async;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.shekharkg.bhsms.bean.SmsModel;
import com.shekharkg.bhsms.storage.StorageHelper;
import com.shekharkg.bhsms.utils.CallBack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ShekharKG on 4/29/2016.
 */
public class RetrieveSMS extends AsyncTask<Void, Void, List<SmsModel>> {

  private Context context;
  private CallBack callBack;
  private StorageHelper storageHelper;
  public static final String lastMessageTime = "LAST_MESSAGE_TIMESTAMP";

  public RetrieveSMS(Context context, CallBack callBack) {
    this.context = context;
    this.callBack = callBack;
    storageHelper = StorageHelper.singleInstance(context);
  }

  private List<SmsModel> getAllSms() {
    List<SmsModel> allInBoxSms = new ArrayList<SmsModel>();
    SmsModel smsModel = new SmsModel();
    Uri message = Uri.parse("content://sms/");
    ContentResolver cr = context.getContentResolver();

    Cursor c = cr.query(message, null, null, null, null);
    int totalSMS = c.getCount();

    if (c.moveToFirst()) {
      for (int i = 0; i < totalSMS; i++) {

        long timeStamp = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")));
        smsModel = new SmsModel();
        smsModel.set_id(c.getString(c.getColumnIndexOrThrow("_id")));
        smsModel.setAddress(c.getString(c
            .getColumnIndexOrThrow("address")));
        smsModel.setMessage(c.getString(c.getColumnIndexOrThrow("body")));
        smsModel.setReadState(c.getInt(c.getColumnIndex("read")));
        smsModel.setTimeStamp(timeStamp);
        if (c.getString(c.getColumnIndexOrThrow("type")).contains("1"))
          smsModel.setIsInbox(1);
        else
          smsModel.setIsInbox(0);
        storageHelper.addMessage(smsModel);
        allInBoxSms.add(smsModel);

        c.moveToNext();
      }
    } else {
      Log.e("Retrieving SMS", "You have no SMS");
    }
    c.close();

    return allInBoxSms;
  }

  @Override
  protected List<SmsModel> doInBackground(Void... params) {
    return getAllSms();
  }

  @Override
  protected void onPostExecute(List<SmsModel> smsModels) {
    callBack.OnInboxSuccessfullyRead(smsModels);
  }
}
