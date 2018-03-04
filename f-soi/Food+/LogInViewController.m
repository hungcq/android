//
//  LogInViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 3/3/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "LogInViewController.h"

@interface LogInViewController () {
    NSString *token;
    NSString *au;
}

@end

@implementation LogInViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self getToken];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) getToken {
    NSString *URLString = @"http://api.foodplusvn.vn/api/usersrv?t=token";
    
    NSURL *url = [NSURL URLWithString:URLString];
    
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        if(data != nil) {
            NSError *error;
            
            NSMutableDictionary *dict = [NSJSONSerialization                       JSONObjectWithData:data
                                         options:NSJSONReadingMutableContainers
                                         error:&error];
            NSLog(@"%@", dict);
            NSString *tokenUser = [dict valueForKey:@"token_user"];
            NSString *tokenPassword = [dict valueForKey:@"token_password"];
            token = [NSString stringWithFormat:@"%@%@", tokenUser, tokenPassword];
            NSString *abc = [@"user1user123qwe" md5];
            NSLog([MyUtils sub20string:abc]);
        }
    }];
}

@end
