//
//  NearbySearchViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 2/18/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "NearbySearchViewController.h"

@interface NearbySearchViewController () {
    CLLocation *defaultLocation;
    CLLocation *currentLocation;
    
    UIImage *selectedMarkerImage;
    UIImage *markerImage;
    
    NSMutableArray *placesData;
    GMSMarker *currentMarker;
    
    NSArray *pickerData;
    
    CLLocationManager *locationManager;
}
@end

@implementation NearbySearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UINib *nib = [UINib nibWithNibName:@"MapCell" bundle:nil];
    [self.collectionView registerNib:nib forCellWithReuseIdentifier:@"MapCell"];
    // Do any additional setup after loading the view.
}

- (void)loadView {
    [super loadView];
    [self initData];
    [self setUpMap];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initData {
    placesData = [[NSMutableArray alloc] init];
    defaultLocation = [[CLLocation alloc] initWithLatitude:21.027596 longitude:105.835414];
    currentLocation = defaultLocation;
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
        [locationManager requestWhenInUseAuthorization];
    
    [locationManager startUpdatingLocation];
    
    pickerData = @[@"0.5 km", @"1 km", @"2 km", @"5 km", @"10 km"];
}

- (void) setUpMap {
    [self.mapView animateWithCameraUpdate:[GMSCameraUpdate setTarget:CLLocationCoordinate2DMake(currentLocation.coordinate.latitude, currentLocation.coordinate.longitude) zoom:13]];
    
    NSError *error;
    
    // Set the map style by passing a valid JSON string.
    NSBundle *mainBundle = [NSBundle mainBundle];
    NSURL *styleUrl = [mainBundle URLForResource:@"style" withExtension:@"json"];
    GMSMapStyle *style = [GMSMapStyle styleWithContentsOfFileURL:styleUrl error:&error];
    [self.mapView setMapStyle:style];
    
    self.mapView.accessibilityElementsHidden = NO;
    
    self.mapView.myLocationEnabled = YES;
    
    self.mapView.delegate = self;
    
    UIImage *image = [UIImage imageNamed:@"ic_marker"];
    markerImage = [MyUtils imageWithImage:image scaledToSize:CGSizeMake([MyUtils getDeviceWidth]/25, [MyUtils getDeviceWidth]/25)];
    UIImage *selectedImage = [UIImage imageNamed:@"ic_marker_selected"];
    selectedMarkerImage = [MyUtils imageWithImage:selectedImage scaledToSize:CGSizeMake([MyUtils getDeviceWidth]/20, [MyUtils getDeviceWidth]/20)];
    [self addMarkerToMap];
}

- (BOOL)mapView:(GMSMapView *)mapView didTapMarker:(GMSMarker *)marker
{
    [self selectMarker:marker];
    return YES;
}

- (void) selectMarker: (GMSMarker *) marker {
    if (currentMarker != nil) {
        currentMarker.icon = markerImage;
    }
    marker.icon = selectedMarkerImage;
    currentMarker = marker;
    for (int i = 0; i < placesData.count; i++) {
        Base *base = placesData[i];
        if ([base.marker isEqual:marker]) {
            [_collectionView scrollToItemAtIndexPath:[NSIndexPath indexPathForItem:i inSection:0] atScrollPosition:UICollectionViewScrollPositionCenteredHorizontally animated:YES];
        }
    }
}

- (void) addMarkerToMap {
    
    NSString *scr = @"200x200";
    double lat = currentLocation.coordinate.latitude;
    double lng = currentLocation.coordinate.longitude;
    double dis;
    if ([_pickerView selectedRowInComponent:0] == 0) {
        dis = 0.5;
    } else if([_pickerView selectedRowInComponent:0] == 1) {
        dis = 1;
    } else if([_pickerView selectedRowInComponent:0] == 2) {
        dis = 2;
    } else if([_pickerView selectedRowInComponent:0] == 3) {
        dis = 5;
    } else if([_pickerView selectedRowInComponent:0] == 4) {
        dis = 10;
    }
    
    NSString *URLString = [NSString stringWithFormat: @"http://api.foodplusvn.vn/api/mapsrv?lat=%f&long=%f&dis=%f&scr=%@", lat, lng, dis, scr];
    
    NSURL *url = [NSURL URLWithString:URLString];
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        if(data != nil) {
            NSError *error;
            [self.mapView clear];
            placesData = [Base arrayOfModelsFromData:data error:&error];
            for (Base *base in placesData) {
                GMSMarker *marker = [GMSMarker markerWithPosition:CLLocationCoordinate2DMake([base.latitude doubleValue], [base.longitude doubleValue])];
                marker.icon = markerImage;
                marker.map = self.mapView;
                base.marker = marker;
            }
            [self.collectionView reloadData];
        } else {
            [placesData removeAllObjects];
            [self.collectionView reloadData];
        }
    }];
}

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return pickerData.count;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    [self addMarkerToMap];
}

-(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view
{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, pickerView.frame.size.width, pickerView.frame.size.height)];
    label.backgroundColor = [MyUtils colorFromHexString:@"#CCCCCC"];
    label.textColor = [UIColor blackColor];
    label.font = [UIFont systemFontOfSize:14];
    label.text = pickerData[row];
    return label;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return placesData.count;
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

 - (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
     MapCell *cell = (MapCell *) [collectionView dequeueReusableCellWithReuseIdentifier:@"MapCell" forIndexPath:indexPath];
     Base *base = [placesData objectAtIndex:indexPath.row];
     cell.titleLabel.text = base.title;
     cell.addressLabel.text = base.diachi;
     cell.distanceLabel.text = [NSString stringWithFormat:@"%3.1f km", [base.distance doubleValue]];
     [MyUtils loadImage:[base.images objectAtIndex:0] into:cell.featureImage];
     return cell;
}
-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(CGRectGetWidth(collectionView.frame), CGRectGetHeight(collectionView.frame));
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    UICollectionView *clV = (UICollectionView *) scrollView;
    NSInteger index = [[clV indexPathsForVisibleItems] objectAtIndex:0].row;
    Base *base = placesData[index];
    [self selectMarker:base.marker];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations {
    currentLocation = locations.lastObject;
    [self.mapView animateWithCameraUpdate:[GMSCameraUpdate setTarget:CLLocationCoordinate2DMake(currentLocation.coordinate.latitude, currentLocation.coordinate.longitude) zoom:13]];
    [self addMarkerToMap];
    [locationManager stopUpdatingLocation];
}
@end
