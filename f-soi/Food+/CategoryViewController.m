//
//  CategoryViewController.m
//  Food+
//
//  Created by Anhvuive on 1/23/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "CategoryViewController.h"
#import "AppDelegate.h"
#import "Base.h"
#import "CellObject.h"
#import "CollectionInCell.h"

@interface CategoryViewController () {
    int tid;
    int cid;
    NSString *title;
    NSMutableArray *dataCategory;
    int p;
    bool canRequest;
}
@property (weak, nonatomic) IBOutlet UICollectionView *viewCategory;

@end

@implementation CategoryViewController
- (void)viewDidLoad {
    [super viewDidLoad];
    p = 1;
    canRequest = TRUE;
    dataCategory=[[NSMutableArray alloc] init];
    
    UINib *nib = [UINib nibWithNibName:@"CollectionCellNib" bundle:nil];
    [self.viewCategory registerNib:nib forCellWithReuseIdentifier:@"CollectionInCell"];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return [dataCategory count];
}
-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier=@"CollectionInCell";
    UICollectionViewCell *cell=nil;
    
    CellObject *obj=(CellObject*)[dataCategory objectAtIndex:indexPath.row];
    CollectionInCell *collectionCell=(CollectionInCell*)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
    NSString *type=obj.title;
    if([type isEqualToString:@"titlebox"]){
        [collectionCell setNib:@"TitleBoxCell" withIdentifier:@"TitleBoxCell" withData:obj.arrData];
    }else if([type isEqualToString:@"gallery"]){
        [collectionCell setNib:@"GalleryCell" withIdentifier:@"GalleryCell" withData:obj.arrData];
    }else if([type isEqualToString:@"grid"]){
        [collectionCell setNib:@"GridCell" withIdentifier:@"GridCell" withData:obj.arrData];
    }else if([type isEqualToString:@"list"]){
        [collectionCell setNib:@"RowCell" withIdentifier:@"RowCell" withData:obj.arrData];
    }else if([type isEqualToString:@"big"]){
        [collectionCell setNib:@"BigCell" withIdentifier:@"BigCell" withData:obj.arrData];
    }else if([type isEqualToString:@"gridsimple"]){
        [collectionCell setNib:@"SmallCell" withIdentifier:@"SmallCell" withData:obj.arrData];
    }else{
        [collectionCell setNib:@"BigCell" withIdentifier:@"BigCell" withData:obj.arrData];
    }
    
    cell=collectionCell;
    if((indexPath.item ==[dataCategory count]-1)&&canRequest){
        p++;
        [self requestData:tid withCId:cid andP:p];
    }
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    float width=CGRectGetWidth(collectionView.frame);
    CellObject *obj1=(CellObject*)[dataCategory objectAtIndex:indexPath.row];
    int count= (int) [obj1.arrData count];
    if([obj1.title isEqualToString:@"titlebox"]){
        return CGSizeMake(width-10, 40);
    }else if([obj1.title isEqualToString:@"gallery"]){
        return CGSizeMake(width-10, width/2);
    }else if([obj1.title isEqualToString:@"list"]){
        return CGSizeMake(width-10, 75*count);
    }else if([obj1.title isEqualToString:@"grid"]){
        return CGSizeMake(width-10, ((width)/2+10)*(count/2));
    }else if([obj1.title isEqualToString:@"big"]){
        return CGSizeMake(width-10, ((width-10)/2)*count);
    }else if([obj1.title isEqualToString:@"gridsimple"]){
        return CGSizeMake(width-10, ((width-10)/2)*(count/3));
    }else{
        return CGSizeMake(width-10, (width-10)/2);
    }
    return CGSizeMake(0,0);
}

-(void) requestData:(int) t withCId:(int) c andP:(int)page{
    
    tid=t;
    cid=c;
    p = page;
    NSLog(@"cid%d", cid);
    NSLog(@"tid%d", tid);
    NSString *URLString = [NSString stringWithFormat: @"http://api.foodplusvn.vn/api/productsrv?t=%d&cid=%d&p=%d&scr=600x600", tid, cid,p];
    NSURL *url = [NSURL URLWithString:URLString];
    
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        // Check if any data returned.
        if (data != nil) {
            NSError *error;
            NSMutableDictionary *datas = [NSJSONSerialization
                                          JSONObjectWithData:data
                                          options:NSJSONReadingMutableContainers
                                          error:&error];
            NSMutableArray* arrData=[[NSMutableArray alloc] init];
            
            NSArray *arr=[datas valueForKey:@"content"];
            if([datas count]>0){
                for(NSDictionary *item in arr){
                    Base *base = [[Base alloc] initWithDictionary:item error:&error];
                    [arrData addObject:base];
                }
                [self updateData:arrData withPage:p];
            }else{
                canRequest=false;
            }
        }
    }];
    
}

-(void) updateData:(NSMutableArray*)arr withPage:(int)page{
    if([arr count]>10){
        NSArray *arr1=[arr subarrayWithRange:NSMakeRange(0, 10)];
        NSArray *arr2=[arr subarrayWithRange:NSMakeRange(10, [arr count]-10)];
        if(page%2==0){
            CellObject *ob1=[[CellObject alloc] init];
            ob1.title=@"gridsimple";
            ob1.arrData=arr1;
            [dataCategory addObject:ob1];
            CellObject *ob2=[[CellObject alloc] init];
            ob2.title=@"big";
            ob2.arrData=arr2;
            [dataCategory addObject:ob2];
            
        }else{
            CellObject *ob3=[[CellObject alloc] init];
            ob3.title=@"grid";
            ob3.arrData=arr1;
            [dataCategory addObject:ob3];
            CellObject *ob4=[[CellObject alloc] init];
            ob4.title=@"list";
            ob4.arrData=arr2;
            [dataCategory addObject:ob4];
        }
    }else{
        NSArray *arr1=[arr subarrayWithRange:NSMakeRange(0, [arr count])];
        if(page%2==0){
            CellObject *ob1=[[CellObject alloc] init];
            ob1.title=@"gridsimple";
            ob1.arrData=arr1;
            [dataCategory addObject:ob1];
            
        }else{
            CellObject *ob3=[[CellObject alloc] init];
            ob3.title=@"grid";
            ob3.arrData=arr1;
            [dataCategory addObject:ob3];
        }
    }
    
    [self.viewCategory reloadData];
}

- (NSArray *)arrayWithImages:(KIImagePager *)pager {
    return @[[UIImage imageNamed:@"pic_cafetra"],
             [UIImage imageNamed:@"pic_ctnauan"],
             [UIImage imageNamed:@"pic_sukien"],
             [UIImage imageNamed:@"pic_tpsach"],
             [UIImage imageNamed:@"pic_suckhoe"],
             [UIImage imageNamed:@"pic_nhahang"]];
}

- (UIViewContentMode)contentModeForImage:(NSUInteger)image inPager:(KIImagePager *)pager {
    return UIViewContentModeScaleAspectFill;
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath {
    UICollectionReusableView *reusableView=nil;
    if(kind==UICollectionElementKindSectionHeader){
        CategoryReusableView *headerView =[collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"Header" forIndexPath:indexPath];
        [headerView setUpImagePager];
        reusableView=headerView;
    }
    return reusableView;
}

@end
