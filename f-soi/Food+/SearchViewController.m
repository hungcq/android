//
//  SearchViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 3/3/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "SearchViewController.h"

@interface SearchViewController () {
    NSMutableArray *searchData;
}
@end

@implementation SearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initData];
    [self loadData:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initData {
    UINib *nib = [UINib nibWithNibName:@"RowCell" bundle:nil];
    [self.collectionView registerNib:nib forCellWithReuseIdentifier:@"RowCell"];
    searchData = [NSMutableArray array];
}

- (void) loadData:(NSString *) query {
//    int cityID = 100;
    NSString *URLString = [NSString stringWithFormat: @"http://api.foodplusvn.vn/api/searchsrv?scr=200x200&p=1&key=%@", query];
    NSURL *url = [NSURL URLWithString:URLString];
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        if(data != nil) {
            NSError *error;
            searchData = [Base arrayOfModelsFromData:data error:&error];
            [_collectionView reloadData];
        } else {
            [searchData removeAllObjects];
            [_collectionView reloadData];
        }
    }];
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return searchData.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    RowCell *cell = (RowCell *) [collectionView dequeueReusableCellWithReuseIdentifier:@"RowCell" forIndexPath:indexPath];
    Base *base = [searchData objectAtIndex:indexPath.row];
    [cell setData:base];
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(collectionView.frame.size.width, collectionView.frame.size.height/8);
}

-(void)textChanged:(id)sender {
    [self loadData:_textField.text];
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    UITouch *touch = [touches anyObject];
    if(![touch.view isMemberOfClass:[UITextField class]]) {
        [touch.view endEditing:YES];
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField endEditing:YES];
    return NO;
}
@end
