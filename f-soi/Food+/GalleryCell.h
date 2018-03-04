//
//  GalleryCell.h
//  Food+
//
//  Created by Anhvuive on 1/4/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GalleryCell : UICollectionViewCell{
    NSArray *arrSP;
}
@property (nonatomic,retain) NSArray *arrSP;

@property (weak, nonatomic) IBOutlet UIImageView *pic2;
@property (weak, nonatomic) IBOutlet UIImageView *pic3;
@property (weak, nonatomic) IBOutlet UIImageView *pic4;
@property (weak, nonatomic) IBOutlet UIImageView *pic5;
@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *diaChi;
@property (weak, nonatomic) IBOutlet UILabel *like;
@property (weak, nonatomic) IBOutlet UILabel *rate;
@property (weak, nonatomic) IBOutlet UILabel *cmt;
@property (weak, nonatomic) IBOutlet UILabel *pin;
- (IBAction)clickPic1:(id)sender;

@property (weak, nonatomic) IBOutlet UIButton *pic1;

@property (nonatomic) int idSP;
@property (nonatomic) int idLoai;
@property (nonatomic) int idDM;
-(void) setData:(NSArray*)data;
-(void) init: (NSString*)img;


@end
