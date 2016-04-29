package com.shekharkg.bhsms.async;

import android.content.Context;
import android.os.AsyncTask;

import com.shekharkg.bhsms.bean.ConversationModel;
import com.shekharkg.bhsms.storage.StorageHelper;
import com.shekharkg.bhsms.utils.CallBack;

import java.util.List;

/**
 * Created by ShekharKG on 4/29/2016.
 */
public class GetConversationList extends AsyncTask<Void, Void, List<ConversationModel>> {

  private Context context;
  private CallBack callBack;
  private StorageHelper storageHelper;

  public GetConversationList(Context context, CallBack callBack) {
    this.context = context;
    this.callBack = callBack;
    storageHelper = StorageHelper.singleInstance(context);
  }

  @Override
  protected List<ConversationModel> doInBackground(Void... params) {
    return storageHelper.getConversationList();
  }

  @Override
  protected void onPostExecute(List<ConversationModel> conversationModels) {
    callBack.OnConversationListCreated(conversationModels);
  }
}
