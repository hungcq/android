//
//  GridCell.h
//  Food+
//
//  Created by Anhvuive on 1/20/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Base.h"

@interface GridCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *pic;
@property (weak, nonatomic) IBOutlet UILabel *like;
@property (weak, nonatomic) IBOutlet UILabel *rate;
@property (weak, nonatomic) IBOutlet UILabel *cmt;
@property (weak, nonatomic) IBOutlet UILabel *pin;
@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *address;
@property (strong, nonatomic) NSString *type;

-(void)setData:(Base*) base;
@property int gidSP, gtid, gcid;

@end
