//
//  CollectionInCell.h
//  Food+
//
//  Created by Anhvuive on 1/5/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CollectionInCell : UICollectionViewCell<UICollectionViewDelegate, UICollectionViewDataSource>{
NSString *identifier;
    NSArray *arrDatas;
}

@property (nonatomic,retain) NSString *identifier;
@property (nonatomic,retain) NSArray *arrDatas;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionCell;
-(void)setNib:(NSString*)type withIdentifier:(NSString*)mIdentifier withData:(NSArray*)data;

@end
