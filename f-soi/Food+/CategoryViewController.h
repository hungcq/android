//
//  CategoryViewController.h
//  Food+
//
//  Created by Anhvuive on 1/23/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KIImagePager.h"
#import "CategoryReusableView.h"
#import "MyUtils.h"

@interface CategoryViewController : UIViewController<UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout,
KIImagePagerDelegate, KIImagePagerDataSource>

-(void) requestData:(int) t withCId:(int) c andP:(int)page;

@end
