//
//  RootViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 3/7/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "RootViewController.h"

@interface RootViewController ()

@end

@implementation RootViewController

- (void)awakeFromNib {
    self.contentViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"NavView"];
    self.menuViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"NavContentView"];
    [super awakeFromNib];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
