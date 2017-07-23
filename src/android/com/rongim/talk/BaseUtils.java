package com.rongim.talk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.rongim.talk.http.HttpUtils;
import com.rongim.talk.module.activity.RongTabsActivity;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import org.apache.cordova.*;


/**
 * Created by jazzeZhou on 16/11/17.
 */
public class BaseUtils {
    
    public static CallbackContext chatCallbackContext = null;
    public static CallbackContext chatsCallbackContext = null;
    

  public static void init(Context context, final CallbackContext callbackContext) {
    RongIM.init(context);
    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

      @Override
      public UserInfo getUserInfo(String userId) {
        return HttpUtils.getInstance().getUserInfo(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
      }

    }, true);
    RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
      @Override
      public boolean onReceived(Message message, int i) {
        ImMessage im = new ImMessage();
        im.id = message.getSenderUserId();
        im.type = message.getObjectName();
        im.conversationType = message.getConversationType().getValue();
        if (im.type.equals("RC:TxtMsg")) {
          TextMessage text = (TextMessage) message.getContent();
          im.content = text.getContent();
        }
        im.lastTime = message.getReceivedTime();
        String content = JSON.toJSONString(im);
        Log.i("BaseUtils","content:"+content);
        PluginResult pr = new PluginResult(PluginResult.Status.OK, content);
        pr.setKeepCallback(true);
        callbackContext.sendPluginResult(pr);
        return false;
      }
    });
  }

  public static void getUserInfo(String userId, final CallbackContext callbackContext) {
    String userInfo = HttpUtils.getInstance().getHUserInfo(userId);
    callbackContext.success(userInfo);
  }

  public static void connect(final Context context, String token, String h_token, String url, final CallbackContext callbackContext) {
    RongIM.getInstance().logout();
    HttpUtils.setBaseurl(url);
    HttpUtils.getInstance().setToken(h_token);
    RongIM.connect(token, new RongIMClient.ConnectCallback() {
      @Override
      public void onTokenIncorrect() {
        Log.i("connect", "onTokenIncorrect");
      }

      @Override
      public void onSuccess(String s) {
        Log.i("connect", "onSuccess:" + s);
        final String userId = s;
        new Thread(new Runnable() {
          @Override
          public void run() {
            UserInfo userInfo = HttpUtils.getUserInfo(userId);
            RongIM.getInstance().refreshUserInfoCache(userInfo);
          }
        }).start();
        callbackContext.success("");
      }

      @Override
      public void onError(RongIMClient.ErrorCode errorCode) {
        callbackContext.error("");
      }
    });

  }

  public static void getConversationList(final CallbackContext callbackContext) {
    RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
      @Override
      public void onSuccess(List<Conversation> conversations) {
        List<ImMessage> list = new ArrayList<ImMessage>();
        if (conversations != null) {
          for (Conversation item: conversations) {
            ImMessage im = new ImMessage();
            im.id = item.getTargetId();
            im.conversationType = item.getConversationType().getValue();
            im.type = item.getObjectName();
            im.unreadMessageCount = item.getUnreadMessageCount();
            if (im.type.equals("RC:TxtMsg")) {
              TextMessage text = (TextMessage) item.getLatestMessage();
              im.content = text.getContent();
            }
            long sendTime = item.getSentTime();
            long recvTime = item.getReceivedTime();
            im.lastTime = sendTime>recvTime ? sendTime : recvTime;
            list.add(im);
          }
        }
        String content = JSON.toJSONString(list);
        callbackContext.success(content);
      }

      @Override
      public void onError(RongIMClient.ErrorCode errorCode) {
        callbackContext.error("");
      }
    });
  }

  public static void exit(Context context) {
    RongIM.getInstance().logout();

  }

  public static void launchChats(Activity context,final CallbackContext callbackContext) {
      chatsCallbackContext = callbackContext;
    Intent intent = new Intent(context, RongTabsActivity.class);
    context.startActivity(intent);
  }

  public static void launchChat(Activity context, String user, String title,final CallbackContext callbackContext) {
      chatCallbackContext = callbackContext;
    RongIM.getInstance().startPrivateChat(context, user, title);
  }
    
    public static void launchSystem(Activity context, String user, String title,final CallbackContext callbackContext) {
        chatCallbackContext = callbackContext;
        RongIM.getInstance().startConversation(context, Conversation.ConversationType.SYSTEM, user, title);
    }

	public static void removeConversation(Activity context, String user, int type,final CallbackContext callbackContext) {
	    RongIM.getInstance().removeConversation(Conversation.ConversationType.setValue(type), user, null);
	}

}
