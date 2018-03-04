//
//  TestSliderController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 2/27/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "TestSliderController.h"

@interface TestSliderController ()

@end

@implementation TestSliderController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated {
    _imagePager.slideshowTimeInterval = 3;
    _imagePager.imageCounterDisabled = TRUE;
//    _imagePager.pageControl.currentPageIndicatorTintColor = [UIColor redColor];
//    _imagePager.pageControl.pageIndicatorTintColor = [UIColor blackColor];
//    _imagePager.pageControlCenter = CGPointMake(CGRectGetWidth(_imagePager.frame) / 2, CGRectGetHeight(_imagePager.frame) - 42);
}

- (NSArray *)arrayWithImages:(KIImagePager *)pager {
    return @[
             @"https://raw.github.com/kimar/tapebooth/master/Screenshots/Screen1.png",
             [UIImage imageNamed:@"pic_cafetra"],
             @"https://raw.github.com/kimar/tapebooth/master/Screenshots/Screen2.png",
             [UIImage imageNamed:@"pic_cafetra"],
             @"https://raw.github.com/kimar/tapebooth/master/Screenshots/Screen3.png"
             ];
}

- (UIViewContentMode) contentModeForImage:(NSUInteger)image inPager:(KIImagePager*)pager
{
    return UIViewContentModeScaleAspectFill;
}

- (UIImage *)placeHolderImageForImagePager:(KIImagePager *)pager {
    return [UIImage imageNamed:@"pic"];
}
@end
