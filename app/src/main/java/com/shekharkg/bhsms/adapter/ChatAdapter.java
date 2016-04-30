package com.shekharkg.bhsms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shekharkg.bhsms.R;
import com.shekharkg.bhsms.bean.SmsModel;
import com.shekharkg.bhsms.utils.Utils;

import java.util.List;

/**
 * Created by ShekharKG on 4/30/2016.
 */
public class ChatAdapter extends BaseAdapter {

  private Context context;
  public List<SmsModel> smsModelList;

  public ChatAdapter(Context context, List<SmsModel> smsModelList) {
    this.context = context;
    this.smsModelList = smsModelList;
  }

  @Override
  public int getCount() {
    return smsModelList.size();
  }

  @Override
  public Object getItem(int position) {
    return smsModelList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = LayoutInflater.from(context).inflate(R.layout.chat_single_item, parent, false);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.receivedLL = (LinearLayout) view.findViewById(R.id.receivedLL);
      viewHolder.sentLL = (LinearLayout) view.findViewById(R.id.sentLL);
      viewHolder.rMessageTV = (TextView) view.findViewById(R.id.rMessageTV);
      viewHolder.rTimeStamp = (TextView) view.findViewById(R.id.rTimeStamp);
      viewHolder.sMessageTV = (TextView) view.findViewById(R.id.sMessageTV);
      viewHolder.sTimeStamp = (TextView) view.findViewById(R.id.sTimeStamp);
      view.setTag(viewHolder);
    }

    SmsModel model = smsModelList.get(position);
    ViewHolder holder = (ViewHolder) view.getTag();
    if (model.getIsInbox() == 1) {
      holder.sentLL.setVisibility(View.GONE);
      holder.rMessageTV.setText(model.getMessage());
      holder.rTimeStamp.setText(Utils.getTimeDate(model.getTimeStamp()));
    } else {
      holder.receivedLL.setVisibility(View.GONE);
      holder.sMessageTV.setText(model.getMessage());
      holder.sTimeStamp.setText(Utils.getTimeDate(model.getTimeStamp()));
    }

    return view;
  }

  private static class ViewHolder {
    private LinearLayout sentLL, receivedLL;
    private TextView rMessageTV, rTimeStamp, sMessageTV, sTimeStamp;
  }
}
