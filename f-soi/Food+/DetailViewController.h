//
//  DetailViewController.h
//  Food+
//
//  Created by Anhvuive on 2/3/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyUtils.h"

@interface DetailViewController : UIViewController<UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>

@property (nonatomic) int idSP;
@property (nonatomic) int idLoai;
@property (nonatomic) int idDM;
@property (strong, nonatomic) NSString *titName;
@property (strong, nonatomic) NSString *address;
@property (strong, nonatomic) NSString *content;
@property (nonatomic) int isLike;
@property (nonatomic) int isPin;
@property (nonatomic) float longitude;
@property (nonatomic) float latitude;
@property (nonatomic) float rate;
@property (strong, nonatomic) NSMutableArray *urlImages;
@property (strong, nonatomic) NSMutableArray *arrRate;



-(void) requestData:(int) i withType:(int) t withCId:(int) cid andP:(int)p;
@end
