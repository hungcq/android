//
//  CollectionInCell.m
//  Food+
//
//  Created by Anhvuive on 1/5/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "CollectionInCell.h"
#import "TitleBoxCell.h"
#import "SmallCell.h"
#import "GalleryCell.h"
#import "RowCell.h"
#import "GridCell.h"
#import "BigCell.h"


@implementation CollectionInCell
@synthesize collectionCell, identifier, arrDatas;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{

    UINib *nib = [UINib nibWithNibName:@"SmallCell" bundle:nil];
    [self.collectionCell registerNib:nib forCellWithReuseIdentifier:@"SmallCell"];
    return self;
}

-(void)setNib:(NSString*) type withIdentifier:(NSString *)mIdentifier withData:(NSArray *)data{
    identifier=mIdentifier;
    arrDatas=data;
    UINib *nib = [UINib nibWithNibName:type bundle:nil];
    [self.collectionCell registerNib:nib forCellWithReuseIdentifier:identifier];
    [self.collectionCell reloadData];
  
}

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    
    if([identifier isEqualToString:@"GalleryCell"])
        return 1;
    return [arrDatas count];
}
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    
    return 1;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    UICollectionViewCell *cell=nil;
       // NSLog(identifier);
    if([identifier isEqualToString:@"TitleBoxCell"]){
     TitleBoxCell *c1= (TitleBoxCell*)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
        [c1 setData:(Base *)[arrDatas objectAtIndex:indexPath.row]];
        cell=c1;

    }else if([identifier isEqualToString:@"GalleryCell"]){
        GalleryCell *c2=(GalleryCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
        [c2 setData:arrDatas];
        cell=c2;
    }else if([identifier isEqualToString:@"RowCell"]){
       RowCell *c3=(RowCell*)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
        [c3 setData:[arrDatas objectAtIndex:indexPath.row]];
        cell=c3;
    }else if([identifier isEqualToString:@"GridCell"]){
        GridCell *c4=(GridCell*)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
        [c4 setData:[arrDatas objectAtIndex:indexPath.row]];
        cell=c4;
    }else if([identifier isEqualToString:@"CellGrid"]){
        SmallCell *c5=(SmallCell*)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
        [c5 setData:[arrDatas objectAtIndex:indexPath.row]];
         cell=c5;
    }else if([identifier isEqualToString:@"BigCell"]){
        BigCell *c6=(BigCell*)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
        [c6 setData:[arrDatas objectAtIndex:indexPath.row]];
        cell=c6;
    }else{
        cell=[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
    }
       return cell;
}
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if([identifier isEqualToString:@"TitleBoxCell"]){
        return CGSizeMake((CGRectGetWidth(collectionView.frame)), 40);
    }else if([identifier isEqualToString:@"GalleryCell"]){
        return CGSizeMake((CGRectGetWidth(collectionView.frame)), (CGRectGetWidth(collectionView.frame)/2));
    }else if([identifier isEqualToString:@"RowCell"]){
        return CGSizeMake((CGRectGetWidth(collectionView.frame)), 75);
    }else if([identifier isEqualToString:@"GridCell"]){
        return CGSizeMake((CGRectGetWidth(collectionView.frame))/2-2, (CGRectGetWidth(collectionView.frame))/2+10);
    }else if([identifier isEqualToString:@"CellGrid"]){
        return CGSizeMake((CGRectGetWidth(collectionView.frame))/2-2, (CGRectGetWidth(collectionView.frame))/2);
    }else{
        return CGSizeMake((CGRectGetWidth(collectionView.frame)), (CGRectGetWidth(collectionView.frame))/2);

    }

}


@end
