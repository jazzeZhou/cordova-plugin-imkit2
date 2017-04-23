//
//  RIChatViewController.m
//  RongImTest
//
//  Created by 周佳森 on 17/4/20.
//  Copyright © 2017年 周佳森. All rights reserved.
//

#import "RIChatsViewController.h"
#import "RIChatViewController.h"

@interface RIChatsViewController ()

@end

@implementation RIChatsViewController

-(id)init
{
    self = [super init];
    if (self) {
        //设置需要显示哪些类型的会话
        [self setDisplayConversationTypes:@[@(ConversationType_PRIVATE),
                                            @(ConversationType_DISCUSSION),
                                            @(ConversationType_CHATROOM),
                                            @(ConversationType_GROUP),
                                            @(ConversationType_APPSERVICE),
                                            @(ConversationType_SYSTEM)]];
        //设置需要将哪些类型的会话在会话列表中聚合显示
        [self setCollectionConversationType:@[@(ConversationType_DISCUSSION),
                                              @(ConversationType_GROUP)]];
        
    }
    return self;
}

-(void)onSelectedTableRow:(RCConversationModelType)conversationModelType conversationModel:(RCConversationModel *)model atIndexPath:(NSIndexPath *)indexPath {
    RIChatViewController *chat = [[RIChatViewController alloc] init];
    chat.conversationType = ConversationType_PRIVATE;
    chat.targetId = model.targetId;
    chat.title = @"会话";
    
    [self.navigationController pushViewController:chat animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
