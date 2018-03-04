//
//  TitleBoxCell.h
//  Food+
//
//  Created by Anhvuive on 1/17/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Base.h"

@interface TitleBoxCell : UICollectionViewCell{

}
@property (weak, nonatomic) IBOutlet UIImageView *icon;
@property (weak, nonatomic) IBOutlet UILabel *name;

-(void)setData:(Base*)base;

@end
