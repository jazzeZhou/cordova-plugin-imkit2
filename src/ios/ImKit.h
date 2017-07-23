//
//  RongCloudModule.h
//  UZApp
//
//  Created by xugang on 14/12/17.
//  Copyright (c) 2014年 APICloud. All rights reserved.
//

#import <Cordova/CDVPlugin.h>
#import <RongIMLib/RongIMLib.h>
#import <RongIMKit/RongIMKit.h>

id<CDVCommandDelegate> delegate;
CDVInvokedUrlCommand *cmd;
CDVInvokedUrlCommand *bcmd;
void recvFunc(NSString *content);
void backFunc(NSString *content);

@interface ImKit : CDVPlugin <RCIMClientReceiveMessageDelegate, RCConnectionStatusChangeDelegate, RCIMUserInfoDataSource>
    
    //@property(nonatomic, assign) id<CDVCommandDelegate> delegate;
    //@property(nonatomic, retain) CDVInvokedUrlCommand *cmd;
    
    
- (void)Init:(CDVInvokedUrlCommand *)command;
- (void)Connect:(CDVInvokedUrlCommand *)command;
- (void)LaunchChats:(CDVInvokedUrlCommand *)command;
- (void)LaunchChat:(CDVInvokedUrlCommand *)command;
- (void)LaunchSystem:(CDVInvokedUrlCommand *)command;
- (void)Exit:(CDVInvokedUrlCommand *)command;
- (void)GetUserInfo:(CDVInvokedUrlCommand *)command;
- (void)GetConversationList:(CDVInvokedUrlCommand *)command;
- (void)RemoveConversation:(CDVInvokedUrlCommand *)command;

@end
