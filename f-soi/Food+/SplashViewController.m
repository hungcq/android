//
//  SplashViewController.m
//  Food+
//
//  Created by Anhvuive on 1/18/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "SplashViewController.h"
#import "AppDelegate.h"
#import "Base.h"
#import "TitleBoxObject.h"
#import "CellObject.h"
@interface SplashViewController ()

@end

@implementation SplashViewController
NSMutableArray *arrData2;
- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIStoryboard *myStoryboad=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    UIViewController *vc=[myStoryboad instantiateViewControllerWithIdentifier:@"RootView"];
    [self presentViewController:vc animated:YES completion:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
