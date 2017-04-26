package com.rongim.talk.module.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import com.rongim.talk.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;


/**
 * Created by jazzeZhou on 16/11/17.
 */
public class RongTabsActivity extends FragmentActivity {

    private FrameLayout fcontainer;

    private final int NUM_ITEMS = 1;
    private List<Fragment> fragmentList;
    private ConversationListFragment conversationListFragment;

    private Button btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("activity_main","layout",getPackageName()));
        initData();
        initView();
        initEvent();

    }

    protected void initData() {

    }

    protected void initView() {
        initConversationListFragment();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(conversationListFragment);

        fcontainer = (FrameLayout) findViewById(getResources().getIdentifier("fragment_container","id",getPackageName()));

        btn_back = (Button)findViewById(getResources().getIdentifier("title_back","id",getPackageName()));
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Fragment fragment = (Fragment)fragments.instantiateItem(fcontainer,0);
        fragments.setPrimaryItem(fcontainer,0,fragment);
        fragments.finishUpdate(fcontainer);

    }

    protected void initEvent() {

    }

    private void initConversationListFragment() {
        conversationListFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                .build();
        conversationListFragment.setUri(uri);

    }

    FragmentPagerAdapter fragments = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragmentList.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

    };

    @Override
    protected void onDestroy() {
        if (BaseUtils.chatsCallbackContext != null) {
        			BaseUtils.chatsCallbackContext.success("");
        			BaseUtils.chatsCallbackContext = null;
        }
        super.onDestroy();
    }

}
