//
//  ViewController.h
//  Food+
//
//  Created by Anhvuive on 1/4/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KIImagePager.h"
#import "MyUtils.h"
#import "REFrostedViewController.h"

@interface ViewController :  UIViewController<UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout,
KIImagePagerDataSource,
KIImagePagerDelegate>

@property (weak, nonatomic) IBOutlet UIBarButtonItem *leftButton;

@property (weak, nonatomic) IBOutlet UIBarButtonItem *sidebarButton;
- (IBAction)menuAction:(id)sender;

@end

