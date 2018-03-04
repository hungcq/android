//
//  MapCell.h
//  Food+
//
//  Created by Raijin Thunderkeg on 2/21/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MapCell : UICollectionViewCell
@property (strong, nonatomic) IBOutlet UILabel *titleLabel;
@property (strong, nonatomic) IBOutlet UILabel *addressLabel;
@property (strong, nonatomic) IBOutlet UIImageView *featureImage;
@property (strong, nonatomic) IBOutlet UILabel *distanceLabel;

@end
