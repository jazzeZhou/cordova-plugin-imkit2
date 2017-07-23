package com.rong.imkit;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.content.Intent;
import android.content.Context;

import com.rongim.talk.BaseUtils;

public class ImKit extends CordovaPlugin {

  public static final String ACTION_INIT = "Init";
  public static final String ACTION_CONNECT = "Connect";
  public static final String ACTION_GETUSER = "GetUserInfo";
  public static final String ACTION_GET_LIST = "GetConversationList";
  public static final String ACTION_EXIT = "Exit";
  public static final String ACTION_LAUNCH_CHATS = "LaunchChats";
  public static final String ACTION_LAUNCH_CHAT = "LaunchChat";
  public static final String ACTION_LAUNCH_SYSTEM = "LaunchSystem";
  public static final String ACTION_RECIEVE_MESSAGE = "RecieveMessage";
  public static final String ACTION_RECIEVE_REMOVECONVERSATION = "RemoveConversation";

  private static CallbackContext mCallbackContext;
  private static CordovaInterface mCordova = null;
  private static CordovaPlugin mPlugin = null;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    mCordova = cordova;
    mPlugin = this;
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_INIT.equals(action)) {
      BaseUtils.init(mCordova.getActivity(), callbackContext);
    } else if (ACTION_CONNECT.equals(action)) {
      String token = args.getString(0);
      String h_token = args.getString(1);
      String user_url = args.getString(2);
      BaseUtils.connect(mCordova.getActivity(), token, h_token, user_url, callbackContext);
    } else if (ACTION_GETUSER.equals(action)) {
      String userId = args.getString(0);
      BaseUtils.getUserInfo(userId, callbackContext);
    } else if (ACTION_GET_LIST.equals(action)) {
      BaseUtils.getConversationList(callbackContext);
    } else if (ACTION_EXIT.equals(action)) {
      BaseUtils.exit(mCordova.getActivity());
    } else if (ACTION_LAUNCH_CHATS.equals(action)) {
      BaseUtils.launchChats(mCordova.getActivity(),callbackContext);
    } else if (ACTION_LAUNCH_CHAT.equals(action)) {
      String user = args.getString(0);
      String title = args.getString(1);
      BaseUtils.launchChat(mCordova.getActivity(), user, title,callbackContext);
    } else if (ACTION_LAUNCH_SYSTEM.equals(action)) {
      String user = args.getString(0);
      String title = args.getString(1);
      BaseUtils.launchSystem(mCordova.getActivity(), user, title,callbackContext);
    } else if (ACTION_RECIEVE_REMOVECONVERSATION.equals(action)) {
      String user = args.getString(0);
      int type = args.getInt(1);
      BaseUtils.removeConversation(mCordova.getActivity(), user, type,callbackContext);
    }
    return true;
  }
}
