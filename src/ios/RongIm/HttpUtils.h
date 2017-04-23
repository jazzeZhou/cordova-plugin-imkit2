//
//  HttpUtils.h
//  RongImTest
//
//  Created by 周佳森 on 17/4/21.
//  Copyright © 2017年 周佳森. All rights reserved.
//

#ifndef HttpUtils_h
#define HttpUtils_h

@interface HttpUtils : NSObject

@property(nonatomic, strong) NSString *sHToken;
@property(nonatomic, strong) NSString *sGetUserUrl;

-(void)setSHToken:(NSString *)sHToken andUrl: (NSString *)url;

-(NSDictionary *)HttpGet: (NSString *)url;

-(NSDictionary *)GetUserInfo: (NSString *)userId;

@end

#endif /* HttpUtils_h */
