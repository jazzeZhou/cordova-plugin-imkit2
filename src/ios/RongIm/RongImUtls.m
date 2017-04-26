//
//  RongImUtls.m
//  RongImTest
//
//  Created by 周佳森 on 17/4/20.
//  Copyright © 2017年 周佳森. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <RongIMKit/RongIMKit.h>
#import "HttpUtils.h"
#import "RIChatsViewController.h"
#import "RIChatViewController.h"
#import "RongImUtls.h"

@interface RongImUtls ()
    
    @end

@implementation RongImUtls
    
-(void)backAction: (id)sender {
    NSLog(@"backAction");
    if( self.appRootVC )
    {
        if (_backFunc != NULL) {
            ((RecvMsgFunc)_backFunc)(@"");
            _backFunc = NULL;
        }
        [self.appRootVC dismissViewControllerAnimated:NO completion:nil];
        self.appRootVC = nil;
    }
}
    
    
-(void)getUserInfoWithUserId:(NSString *)userId completion:(void (^)(RCUserInfo *))completion {
    RCUserInfo *user = [[RCUserInfo alloc] init];
    user.userId = userId;
    
    NSDictionary *dicUser = [_httpUtils GetUserInfo:userId];
    if (dicUser != nil) {
        user.name = [NSString stringWithFormat:@"%@", [dicUser objectForKey:@"name"] ];
        user.portraitUri = [NSString stringWithFormat:@"%@", [dicUser objectForKey:@"portrait"] ];
    } else {
        user.name = userId;
    }
    completion(user);
}
    
-(void)onRCIMReceiveMessage:(RCMessage *)message left:(int)left {
    NSLog(@"left:%d, message: %@", left, message.senderUserId);
    ((RecvMsgFunc)_recvFunc)(@"");
}
    
    // 将字典或者数组转化为JSON串
- (NSString *)toJSONData:(id)theData{
    
    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:theData
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    
    if ( error != nil || [jsonData length] == 0 ){
        return nil;
    }
    
    //使用这个方法的返回，我们就可以得到想要的JSON串
    NSString *jsonString = [[NSString alloc] initWithData:jsonData
                                                 encoding:NSUTF8StringEncoding];
    
    return jsonString;
}
    
-(void)init: (NSString *)key andSuccessFunc: (RecvMsgFunc *)func {
    _recvFunc = func;
    
    [[RCIM sharedRCIM] initWithAppKey:key];
    [[RCIM sharedRCIM] setUserInfoDataSource:self];
    [[RCIM sharedRCIM] setReceiveMessageDelegate:self];
    
    _barButton = [[UIBarButtonItem alloc]initWithTitle:@"返回" style:UIBarButtonItemStylePlain target:self action:@selector(backAction:)];
    [_barButton setTintColor:[UIColor whiteColor]];
    
    //23c2aa
    _barColor = [UIColor colorWithRed:(CGFloat)35/255.0 green:(CGFloat)194/255.0 blue:(CGFloat)170/255.0 alpha:(CGFloat)1.0];
    
    _httpUtils = [[HttpUtils alloc] init];
}
    
-(void)connect: (NSString *)h_token andImToken: (NSString *)im_token andGetUserUrl: (NSString *)get_user_url success: (void (^)(NSString *content))successBlock {
    if (_httpUtils) {
        [_httpUtils setSHToken:h_token andUrl:get_user_url];
    }
    [[RCIM sharedRCIM] connectWithToken:im_token success:^(NSString *userId) {
        NSLog(@"success: %@", userId);
        
        successBlock(userId);
        
    } error:^(RCConnectErrorCode status) {
        NSLog(@"error: %ld", status);
    } tokenIncorrect:^{
        NSLog(@"tokenIncorrect");
    }];
    
}
    
-(void)getConversationList: (void (^)(NSString *content))successBlock {
    NSArray *conversationList = [[RCIMClient sharedRCIMClient]
                                 getConversationList:@[@(ConversationType_PRIVATE),
                                                       @(ConversationType_DISCUSSION),
                                                       @(ConversationType_GROUP),
                                                       @(ConversationType_SYSTEM),
                                                       @(ConversationType_APPSERVICE),
                                                       @(ConversationType_PUBLICSERVICE)]];
    
    NSMutableArray *retInfo = [[NSMutableArray alloc] init];
    for (RCConversation *conversation in conversationList) {
        NSLog(@"conversation: %@", conversation.targetId);
        NSString *sContent = [conversation.objectName isEqual:@"RC:TxtMsg"] ? ((RCTextMessage *)conversation.lastestMessage).content :@"";
        NSNumber *lasttime = [NSNumber numberWithLongLong:(conversation.sentTime > conversation.receivedTime ? conversation.sentTime:conversation.receivedTime)];
        NSDictionary *itemInfo = [NSDictionary dictionaryWithObjectsAndKeys:
                                  conversation.targetId, @"id",
                                  conversation.objectName, @"type",
                                  [NSNumber numberWithInt:conversation.unreadMessageCount], @"unreadMessageCount",
                                  sContent, @"content",
                                  lasttime, @"lastTime",
                                  nil];
        [retInfo addObject:itemInfo];
    }
    
    NSString  *sResultInfo = [self toJSONData:retInfo];
    
    if( nil == sResultInfo )
    {
        sResultInfo = @"";
    }
    NSLog(@"getConversationList: %@", sResultInfo);
    successBlock(sResultInfo);
}
    
-(void)getUserInfo:(NSString *)userid success: (void (^)(NSString *content))successBlock {
    NSDictionary *user = [_httpUtils GetUserInfo:userid];
    NSString *sResultInfo = [self toJSONData:user];
    NSLog(@"%@", sResultInfo);
    successBlock(sResultInfo);
}
    
-(void)launchChat: (NSString *)userId success: (RecvMsgFunc *)func {
    NSLog(@"launchChat: %@", userId);
    _backFunc = func;
    
    RIChatViewController *chat = [[RIChatViewController alloc] init];
    chat.conversationType = ConversationType_PRIVATE;
    chat.targetId = userId;
    chat.title = @"会话";
    
    chat.navigationItem.leftBarButtonItem = _barButton;
    
    UIWindow *window = [[UIApplication sharedApplication] keyWindow];
    self.appRootVC = window.rootViewController;
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:chat];
    [nav.navigationBar setBarTintColor: _barColor];
    [self.appRootVC presentViewController:nav animated:NO completion:nil];
    
}
    
-(void)launchChats: (RecvMsgFunc *)func {
    _backFunc = func;
    
    RIChatsViewController *chats = [[RIChatsViewController alloc] init];
    chats.title = @"聊天列表";
    
    chats.navigationItem.leftBarButtonItem = _barButton;
    
    UIWindow *window = [[UIApplication sharedApplication] keyWindow];
    self.appRootVC = window.rootViewController;
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:chats];
    [nav.navigationBar setBarTintColor: _barColor];
    [self.appRootVC presentViewController:nav animated:NO completion:nil];
    
}
    
-(void)exit {
    [[RCIM sharedRCIM] logout];
}
    
@end



