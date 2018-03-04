//
//  NewsViewController.h
//  Food+
//
//  Created by Raijin Thunderkeg on 2/28/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyUtils.h"
#import "Base.h"
#import "NewsCell.h"
#import "NewsDetailViewController.h"

@interface NewsViewController : UIViewController <
UICollectionViewDelegate,
UICollectionViewDataSource,
UICollectionViewDelegateFlowLayout,
UIScrollViewDelegate >
@property (strong, nonatomic) IBOutlet UICollectionView *collectionView;

@end
