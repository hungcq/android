//
//  RowCell.h
//  Food+
//
//  Created by Anhvuive on 1/20/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Base.h"

@interface RowCell : UICollectionViewCell{

}
@property (weak, nonatomic) IBOutlet UIImageView *pic;
@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *address;
@property (weak, nonatomic) IBOutlet UILabel *star;

@property int ridSP, rtid, rcid;

- (void)setData:(Base *) base;

@end
