package com.shekharkg.bhsms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shekharkg.bhsms.async.RetrieveSMS;
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
    if (storageHelper.getFromPreference(this, "IS_FIRST_RUN") == 0 || storageHelper.getAllMessages().size() == 0) {
      new RetrieveSMS(this, this).execute();
      storageHelper.saveInPreference(this, "IS_FIRST_RUN", 123);
    } else {
      logSms(storageHelper.getAllMessages());
    }
  }

  private void logSms(List<SmsModel> allMessages) {
    for (SmsModel model : allMessages) {
      Log.e("SMS", model.get_id() + ", From : " + model.getAddress() + ", Message : " + model.getMessage()
          + ", Status : " + model.getReadState() + ", Time : " + model.getTimeStamp() + ", IsInbox : " + model.getIsInbox());
    }
    Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void OnInboxSuccessfullyRead(List<SmsModel> smsModels) {
    logSms(smsModels);
  }
}
