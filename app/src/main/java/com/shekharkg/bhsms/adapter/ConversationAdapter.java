package com.shekharkg.bhsms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shekharkg.bhsms.R;
import com.shekharkg.bhsms.bean.ConversationModel;
import com.shekharkg.bhsms.utils.Utils;

import java.util.List;

/**
 * Created by ShekharKG on 4/30/2016.
 */
public class ConversationAdapter extends BaseAdapter {

  private Context context;
  public List<ConversationModel> conversationModels;

  public ConversationAdapter(Context context, List<ConversationModel> conversationModels) {
    this.context = context;
    this.conversationModels = conversationModels;
  }

  @Override
  public int getCount() {
    return conversationModels.size();
  }

  @Override
  public Object getItem(int position) {
    return conversationModels.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = LayoutInflater.from(context).inflate(R.layout.conversation_single_item, parent, false);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.addressTV = (TextView) view.findViewById(R.id.addressTV);
      viewHolder.messageTV = (TextView) view.findViewById(R.id.messageTV);
      viewHolder.timeStampTV = (TextView) view.findViewById(R.id.timeStampTV);
      viewHolder.isRead = view.findViewById(R.id.isRead);
      view.setTag(viewHolder);
    }

    ConversationModel model = conversationModels.get(position);
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.addressTV.setText(model.getAddress());
    holder.messageTV.setText(model.getLastMessage());
    holder.timeStampTV.setText(Utils.getTimeDate(model.getTimeStamp()));
    if (model.getIsInbox() == 1 && model.getReadStatus() == 0)
      holder.isRead.setVisibility(View.VISIBLE);
    else
      holder.isRead.setVisibility(View.INVISIBLE);

    return view;
  }

  private static class ViewHolder {
    private TextView addressTV, messageTV, timeStampTV;
    private View isRead;
  }
}
