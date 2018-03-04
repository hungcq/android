//
//  BigCell.h
//  Food+
//
//  Created by Anhvuive on 1/20/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Base.h"

@interface BigCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *pic;
@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *address;
@property (weak, nonatomic) IBOutlet UILabel *star;
@property (weak, nonatomic) IBOutlet UILabel *like;
@property (weak, nonatomic) IBOutlet UILabel *rate;
@property (weak, nonatomic) IBOutlet UILabel *cmt;
@property (weak, nonatomic) IBOutlet UILabel *pin;
-(void)setData:(Base*) base;

@property int bidSP, btid, bcid;

@end
