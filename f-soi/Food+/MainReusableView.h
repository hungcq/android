//
//  MainReusableView.h
//  Food+
//
//  Created by Anhvuive on 1/4/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KIImagePager.h"

@interface MainReusableView : UICollectionReusableView
@property (strong, nonatomic) IBOutlet KIImagePager *imagePager;
- (void) setUpSlider;
@end
