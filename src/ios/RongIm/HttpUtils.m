//
//  HttpUtils.m
//  RongImTest
//
//  Created by 周佳森 on 17/4/21.
//  Copyright © 2017年 周佳森. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HttpUtils.h"

@implementation HttpUtils

-(void)setSHToken:(NSString *)sHToken andUrl:(NSString *)url {
    self.sHToken = sHToken;
    self.sGetUserUrl = url;
}

-(NSDictionary *)HttpGet: (NSString *)url {
    NSDictionary *dic = nil;
    
    do {
        if (url == nil||url.length == 0) {
            break;
        }
        NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString: url] cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60];
        if (request == nil) {
            break;
        }
        
        NSMutableURLRequest *mutableRequest = [request mutableCopy];
        [mutableRequest addValue:self.sHToken forHTTPHeaderField:@"Authorization"];
        request = [mutableRequest copy];
        
        NSData *retHttp = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
        if (retHttp == nil) {
            break;
        }
        NSDictionary *responseDic = [NSJSONSerialization JSONObjectWithData:retHttp options:NSJSONReadingMutableLeaves error:nil];
        if (responseDic == nil||[responseDic objectForKey:@"success"] == nil||![[responseDic objectForKey:@"success"] boolValue]) {
            break;
        }
        dic = [responseDic objectForKey:@"data"];
    } while (0);
    return dic;
}

-(NSDictionary *)GetUserInfo:(NSString *)userId {
    NSDictionary *dic = nil;
    
    do {
        if (self.sHToken == nil || self.sGetUserUrl == nil) {
            break;
        }
        if (self.sHToken.length == 0 || self.sGetUserUrl.length == 0) {
            break;
        }
        NSString *sUrl = [NSString stringWithFormat:@"%@%@", self.sGetUserUrl, userId];
        
        NSDictionary *resData = [self HttpGet:sUrl];
        if (nil == resData) {
            break;
        }
        
        NSString *sPortrait = [NSString stringWithFormat:@"%@", [resData objectForKey:@"portrait"]];
        NSString *sName = [NSString stringWithFormat:@"%@", [resData objectForKey:@"name"]];
        if (sName == nil||sName.length == 0||[sName isEqualToString:@"<null>"]) {
            sName = [NSString stringWithFormat:@"%@", [resData objectForKey:@"username"]];
        }
        
        if (sPortrait == nil||sPortrait.length == 0||[sPortrait isEqualToString:@"<null>"]) {
            sPortrait = @"";
        }
        if (sName == nil||sName.length == 0||[sName isEqualToString:@"<null>"]) {
            sName = @"";
        }
        
        dic = [NSDictionary dictionaryWithObjectsAndKeys:userId, @"uid", sName, @"name", sPortrait, @"portrait", nil];
        
    } while (0);
    return dic;
}

@end



