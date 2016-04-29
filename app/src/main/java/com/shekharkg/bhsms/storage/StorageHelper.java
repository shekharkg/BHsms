package com.shekharkg.bhsms.storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shekharkg.bhsms.bean.ConversationModel;
import com.shekharkg.bhsms.bean.SmsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShekharKG on 4/29/2016.
 */
public class StorageHelper extends SQLiteOpenHelper {

  private static final String DB_NAME = "dbBuyHutke";
  private static final int DB_VERSION = 1;

  private static final String tblMessage = "tblMessage";
  private static final String colID = "id";
  private static final String colMessageID = "messageId";
  private static final String colAddress = "address";
  private static final String colMessage = "message";
  private static final String colReadState = "readState";
  private static final String colTimeStamp = "timeStamp";
  private static final String colIsInbox = "isInbox";

  private SQLiteDatabase db;

  /**
   * Query to create table
   */
  private final static String createTable = "Create TABLE " + tblMessage + " (" + colID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colMessageID + " TEXT, " + colAddress + " TEXT, "
      + colMessage + " TEXT, " + colReadState + " INTEGER, " + colTimeStamp + " TEXT, "
      + colIsInbox + " INTEGER);";

  /**
   * Drop table query used when DB version updates
   */
  private final static String dropTable = "DROP TABLE IF EXISTS " + tblMessage;


  /**
   * Private constructor to make this class singleton
   */
  private StorageHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
    db = getWritableDatabase();
  }


  private static StorageHelper storageHelper;

  /**
   * Static method returns single reference
   */
  public static StorageHelper singleInstance(Context context) {
    if (storageHelper == null)
      storageHelper = new StorageHelper(context);
    return storageHelper;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(createTable);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(dropTable);
    db.execSQL(createTable);
  }


  /**
   * Get sms count
   */
  public long getMessageCount() {
    return DatabaseUtils.queryNumEntries(db, tblMessage);
  }


  /**
   * Select messages list from table
   */
  public List<SmsModel> getAllMessages() {
    Cursor c = db.query(tblMessage, new String[]{colMessageID, colAddress,
            colMessage, colReadState, colTimeStamp, colIsInbox},
        null, null, null, null, null);
    List<SmsModel> values = new ArrayList<>();
    while (c.moveToNext()) {
      SmsModel smsModel = new SmsModel();
      smsModel.set_id(c.getString(0));
      smsModel.setAddress(c.getString(1));
      smsModel.setMessage(c.getString(2));
      smsModel.setReadState(c.getInt(3));
      smsModel.setTimeStamp(c.getLong(4));
      smsModel.setIsInbox(c.getInt(5));
      values.add(smsModel);
    }
    c.close();
    return values;
  }

  /**
   * Get list of conversations
   */
  public List<ConversationModel> getConversationList() {
    Cursor c = db.query(tblMessage, new String[]{colAddress, colMessage, colReadState, colIsInbox, colTimeStamp},
        colIsInbox + " =?", new String[]{"1"},
        null, null, colTimeStamp + " DESC");
    List<ConversationModel> values = new ArrayList<>();
    Map<String, ConversationModel> modelMap = new HashMap<>();
    while (c.moveToNext()) {
      ConversationModel conversationModel = new ConversationModel();
      conversationModel.setAddress(c.getString(0));
      conversationModel.setLastMessage(c.getString(1));
      conversationModel.setReadStatus(c.getInt(2));
      conversationModel.setIsInbox(c.getInt(3));
      conversationModel.setTimeStamp(c.getLong(4));
      if (!modelMap.containsKey(conversationModel.getAddress())) {
        modelMap.put(conversationModel.getAddress(), conversationModel);
        values.add(conversationModel);
      }
    }
    modelMap = null;
    c.close();
    return values;
  }

  /**
   * Insert a message to table
   */
  public long addMessage(SmsModel smsModel) {
    Cursor c = db.query(tblMessage, new String[]{colMessage, colAddress, colTimeStamp},
        colAddress + " =? AND " + colTimeStamp + " =?",
        new String[]{smsModel.getAddress(), String.valueOf(smsModel.getTimeStamp())},
        null, null, null);

    if (c.getCount() > 0) {
      c.close();
      return 0;
    }
    c.close();

    ContentValues contentValues = new ContentValues();
    contentValues.put(colMessageID, smsModel.get_id());
    contentValues.put(colAddress, smsModel.getAddress());
    contentValues.put(colMessage, smsModel.getMessage());
    contentValues.put(colReadState, smsModel.getReadState());
    contentValues.put(colTimeStamp, smsModel.getTimeStamp());
    contentValues.put(colIsInbox, smsModel.getIsInbox());
    return db.insert(tblMessage, null, contentValues);
  }

  /**
   * Change status of unread message
   */
  public int updateReadStatus(String address) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(colReadState, 1);
    return db.update(tblMessage, contentValues, colAddress + " =? AND " + colReadState + " =?",
        new String[]{address, "0"});
  }

  /**
   * Save the timestamp of last message received which is read by app
   */
  public void saveInPreference(Context context, String key, long value) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),
        Context.MODE_PRIVATE);
    sharedPreferences.edit().putLong(key, value).apply();
  }

  /**
   * Get the last saved timestamp
   */
  public long getFromPreference(Context context, String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),
        Context.MODE_PRIVATE);
    return sharedPreferences.getLong(key, 0);
  }

}

