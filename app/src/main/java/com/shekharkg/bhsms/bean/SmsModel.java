package com.shekharkg.bhsms.bean;

/**
 * Created by ShekharKG on 4/29/2016.
 */
public class SmsModel {

  private String _id;
  private String address;
  private String message;
  private String readState;
  private long timeStamp;
  private int isInbox;

  public SmsModel() {
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getReadState() {
    return readState;
  }

  public void setReadState(String readState) {
    this.readState = readState;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public int getIsInbox() {
    return isInbox;
  }

  public void setIsInbox(int isInbox) {
    this.isInbox = isInbox;
  }
}

