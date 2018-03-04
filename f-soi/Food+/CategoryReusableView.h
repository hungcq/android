//
//  CategoryReusableView.h
//  Food+
//
//  Created by Raijin Thunderkeg on 2/28/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KIImagePager.h"

@interface CategoryReusableView : UICollectionReusableView
@property (strong, nonatomic) IBOutlet KIImagePager *imagePager;
- (void) setUpImagePager;
@end
