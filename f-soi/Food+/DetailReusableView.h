//
//  DetailReusableView.h
//  Food+
//
//  Created by Anhvuive on 2/7/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailReusableView : UICollectionReusableView
@property (weak, nonatomic) IBOutlet UIImageView *pic1;
@property (weak, nonatomic) IBOutlet UIView *viewRate;
@property (weak, nonatomic) IBOutlet UILabel *lbTit;
@property (weak, nonatomic) IBOutlet UILabel *lbRate;
@property (weak, nonatomic) IBOutlet UIView *morepic;
@property (weak, nonatomic) IBOutlet UIImageView *pic2;
@property (weak, nonatomic) IBOutlet UIImageView *pic3;
@property (weak, nonatomic) IBOutlet UIImageView *pic4;
@property (weak, nonatomic) IBOutlet UIImageView *pic5;

@end
