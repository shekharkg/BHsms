package com.shekharkg.bhsms.utils;

import com.shekharkg.bhsms.bean.SmsModel;

import java.util.List;

/**
 * Created by ShekharKG on 4/29/2016.
 */
public interface CallBack {

  void OnInboxSuccessfullyRead(List<SmsModel> smsModels);

}
