//
//  NewsDetailViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 3/1/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "NewsDetailViewController.h"

@interface NewsDetailViewController ()

@end

@implementation NewsDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) loadData:(int) id {
    NSString *URLString = [NSString stringWithFormat:  @"http://api.foodplusvn.vn/api/eventsrv?opt=1&id=%d&scr=400x400", id];
    
    NSURL *url = [NSURL URLWithString:URLString];
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        if(data != nil) {
            NSError *error;
            NSMutableDictionary *dict = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
            NSDictionary *itemDict = [dict valueForKey:@"detail"];
            Base *base = [[Base alloc] initWithDictionary:itemDict error:&error];
            _titleLabel.text = base.eventname;
            _contentLabel.text = base.description;
            [MyUtils loadImage:[base.images objectAtIndex:0] into:_imageView];
        }
    }];
}
@end
