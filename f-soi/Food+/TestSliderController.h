//
//  TestSliderController.h
//  Food+
//
//  Created by Raijin Thunderkeg on 2/27/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KIImagePager.h"

@interface TestSliderController : UIViewController <KIImagePagerDelegate, KIImagePagerDataSource>
@property (strong, nonatomic) IBOutlet KIImagePager *imagePager;

@end
