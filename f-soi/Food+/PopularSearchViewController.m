//
//  PopularSearchViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 2/24/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "PopularSearchViewController.h"
#import "GridCell.h"
#import "MyUtils.h"

@interface PopularSearchViewController () {
    int pageCount;
}

@end

@implementation PopularSearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    UINib *nib = [UINib nibWithNibName:@"GridCell" bundle:nil];
    [self.collectionView registerNib:nib forCellWithReuseIdentifier:@"GridCell"];
    [self initData];
    [self loadData];
}

- (void) initData {
    placesData = [NSMutableArray array];
    pageCount = 1;
}

- (void) loadData {
    NSString *scr = @"200x200";
    NSString *URLString = [NSString stringWithFormat:  @"http://api.foodplusvn.vn/api/mapsrv?scr=400x400&t=1&p=%d&scr=%@", pageCount, scr];
    
    NSURL *url = [NSURL URLWithString:URLString];
    
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        if(data != nil) {
            NSError *error;
            
            NSMutableArray *array = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
            
            for (NSDictionary *itemDict in array) {
                Base *base = [[Base alloc] initWithDictionary:itemDict error:&error];
                [placesData addObject:base];
            }
            [self.collectionView reloadData];
        }
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return placesData.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
  
    GridCell *gridCell = (GridCell *) [collectionView dequeueReusableCellWithReuseIdentifier:@"GridCell" forIndexPath:indexPath];
    Base *base = [placesData objectAtIndex:indexPath.row];
    [gridCell setData:base];

    return gridCell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((CGRectGetWidth(collectionView.frame))/2-7.5, (CGRectGetWidth(collectionView.frame))/2);
}

- (void)scrollViewDidScroll: (UIScrollView*)scrollView
{
    float scrollViewHeight = scrollView.frame.size.height;
    float scrollContentSizeHeight = scrollView.contentSize.height;
    float scrollOffset = scrollView.contentOffset.y;
    
    if (scrollOffset == 0)
    {
        // then we are at the top
    }
    else if (scrollOffset + scrollViewHeight == scrollContentSizeHeight)
    {
        pageCount++;
        [self loadData];
    }
}
@end
