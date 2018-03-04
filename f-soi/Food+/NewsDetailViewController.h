//
//  NewsDetailViewController.h
//  Food+
//
//  Created by Raijin Thunderkeg on 3/1/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyUtils.h"
#import "Base.h"

@interface NewsDetailViewController : UIViewController
@property (strong, nonatomic) IBOutlet UIImageView *imageView;
@property (strong, nonatomic) IBOutlet UILabel *titleLabel;
@property (strong, nonatomic) IBOutlet UILabel *contentLabel;
@property (nonatomic) int id;
- (void) loadData:(int) id;
@end
