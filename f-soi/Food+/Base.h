//
//  Base.h
//  Food+
//
//  Created by Raijin Thunderkeg on 3/2/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <JSONModel/JSONModel.h>
@import GoogleMaps;

@interface Base : JSONModel
@property (nonatomic) NSNumber <Optional> *id;
@property (nonatomic) NSNumber <Optional> *tid;
@property (nonatomic) NSNumber <Optional> *cid;
@property (nonatomic) NSString <Optional> *title;
@property (nonatomic) NSString <Optional> *diachi;
@property (nonatomic) NSString <Optional> *dienThoai;
@property (nonatomic) NSString <Optional> *gioMoCua;
@property (nonatomic) NSString <Optional> *description;
@property (nonatomic) NSString <Optional> *eventname;
@property (nonatomic) NSString <Optional> *te;
@property (nonatomic) NSString <Optional> *mota;
@property (nonatomic) NSString <Optional> *ngaytao;
@property (nonatomic) NSNumber <Optional> *rating;
@property (nonatomic) NSNumber <Optional> *comment;
@property (nonatomic) NSNumber <Optional> *pin;
@property (nonatomic) NSNumber <Optional> *star;
@property (nonatomic) NSNumber <Optional> *favourite;
@property (nonatomic) NSMutableArray <Optional> *images;
@property (nonatomic) NSString <Optional> *icon;
@property (nonatomic) NSNumber <Optional> *latitude;
@property (nonatomic) NSNumber <Optional> *longitude;
@property (nonatomic) NSNumber <Optional> *distance;
@property (strong, nonatomic) GMSMarker <Ignore> *marker;
@end
