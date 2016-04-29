package com.shekharkg.bhsms.bean;

/**
 * Created by ShekharKG on 4/29/2016.
 */
public class ConversationModel {

  private String address;
  private String lastMessage;
  private int readStatus;
  private int isInbox;
  private long timeStamp;

  public ConversationModel() {
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }

  public int getReadStatus() {
    return readStatus;
  }

  public void setReadStatus(int readStatus) {
    this.readStatus = readStatus;
  }

  public int getIsInbox() {
    return isInbox;
  }

  public void setIsInbox(int isInbox) {
    this.isInbox = isInbox;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }
}
