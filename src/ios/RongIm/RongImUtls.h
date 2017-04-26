//
//  RongImUtls.h
//  RongImTest
//
//  Created by 周佳森 on 17/4/20.
//  Copyright © 2017年 周佳森. All rights reserved.
//

#ifndef RongImUtls_h
#define RongImUtls_h

#import <Foundation/Foundation.h>
#import <RongIMKit/RongIMKit.h>
#import <Cordova/CDVPlugin.h>
#import "HttpUtils.h"

typedef void (*RecvMsgFunc)(NSString *content);

@interface RongImUtls : NSObject<RCIMUserInfoDataSource, RCIMReceiveMessageDelegate>
    
    @property(nonatomic, assign) RecvMsgFunc *recvFunc;
    @property(nonatomic, assign) RecvMsgFunc *backFunc;
    
    @property(nonatomic, retain) HttpUtils *httpUtils;
    @property(nonatomic, strong) UIViewController *appRootVC;
    @property(nonatomic, strong) UIBarButtonItem *barButton;
    @property(nonatomic, strong) UIColor *barColor;
    
-(void)backAction: (id)sender;
- (NSString *)toJSONData:(id)theData;
    
-(void)init: (NSString *)key andSuccessFunc: (RecvMsgFunc *)func;
-(void)connect: (NSString *)h_token andImToken: (NSString *)im_token andGetUserUrl: (NSString *)get_user_url success: (void (^)(NSString *content))successBlock;
-(void)getConversationList: (void (^)(NSString *content))successBlock;
-(void)getUserInfo: (NSString *)userid success: (void (^)(NSString *content))successBlock;
-(void)launchChats: (RecvMsgFunc *)func;
-(void)launchChat: (NSString *)userId success: (RecvMsgFunc *)func;
-(void)exit;
    
@end

#endif /* RongImUtls_h */
