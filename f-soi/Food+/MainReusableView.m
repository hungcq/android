//
//  MainReusableView.m
//  Food+
//
//  Created by Anhvuive on 1/4/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "MainReusableView.h"

@implementation MainReusableView
- (void) setUpSlider {
    [self.imagePager setIndicatorDisabled:TRUE];
    [self.imagePager setImageCounterDisabled:TRUE];
    _imagePager.slideshowTimeInterval = 3;
}
@end
