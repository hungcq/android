//
//  NewsViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 2/28/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "NewsViewController.h"

@interface NewsViewController () {
    NSMutableArray *newsData;
    int pageCount;
}

@end

@implementation NewsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initData];
    [self loadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initData {
    UINib *nib = [UINib nibWithNibName:@"NewsCell" bundle:nil];
    [self.collectionView registerNib:nib forCellWithReuseIdentifier:@"NewsCell"];
    newsData = [NSMutableArray array];
    pageCount = 1;
}

- (void) loadData {
    NSString *URLString = [NSString stringWithFormat: @"http://api.foodplusvn.vn/api/eventsrv?p=%d&scr=200x200", pageCount];
    NSURL *url = [NSURL URLWithString:URLString];
    
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        if(data != nil) {
            NSError *error;
            NSArray *array = [Base arrayOfModelsFromData:data error:&error];
            for (Base *base in array) {
                [newsData addObject:base];
            }
            [self.collectionView reloadData];
        }
    }];
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return newsData.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    NewsCell *cell = (NewsCell *) [collectionView dequeueReusableCellWithReuseIdentifier:@"NewsCell" forIndexPath:indexPath];
    Base *base = [newsData objectAtIndex:indexPath.row];
    [cell setData:base];
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(CGRectGetWidth(collectionView.frame)-10, CGRectGetHeight(collectionView.frame)/7);
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
