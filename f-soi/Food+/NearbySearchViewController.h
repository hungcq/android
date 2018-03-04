//
//  NearbySearchViewController.h
//  Food+
//
//  Created by Raijin Thunderkeg on 2/18/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Base.h"
#import "MyUtils.h"
#import "MapCell.h"
@import GoogleMaps;

@interface NearbySearchViewController : UIViewController <UICollectionViewDelegate,
    UICollectionViewDataSource,
    UICollectionViewDelegateFlowLayout,
    GMSMapViewDelegate,
    UIPickerViewDelegate,
    UIPickerViewDataSource,
    CLLocationManagerDelegate>
@property (strong, nonatomic) IBOutlet GMSMapView *mapView;
@property (strong, nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong, nonatomic) IBOutlet UIPickerView *pickerView;

@end
