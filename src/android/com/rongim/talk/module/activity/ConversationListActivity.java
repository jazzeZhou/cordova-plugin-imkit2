package com.rongim.talk.module.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

/**
 *
 * 会话列表
 * Created by jazzeZhou on 16/11/17.
 */
public class ConversationListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("conversationlist","layout",getPackageName()));

    }




}
