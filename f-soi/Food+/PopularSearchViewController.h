//
//  PopularSearchViewController.h
//  Food+
//
//  Created by Raijin Thunderkeg on 2/24/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PopularSearchViewController : UIViewController
<UICollectionViewDataSource,
UICollectionViewDelegate,
UICollectionViewDelegateFlowLayout,
UIScrollViewDelegate> {
    NSMutableArray *placesData;
}
@property (strong, nonatomic) IBOutlet UICollectionView *collectionView;

@end
