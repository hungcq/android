//
//  CategoryReusableView.m
//  Food+
//
//  Created by Raijin Thunderkeg on 2/28/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "CategoryReusableView.h"

@implementation CategoryReusableView
- (void)setUpImagePager {
    [self.imagePager setImageCounterDisabled:TRUE];
    _imagePager.slideshowTimeInterval = 3;
}
@end
