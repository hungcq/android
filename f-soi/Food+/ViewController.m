//
//  ViewController.m
//  Food+
//
//  Created by Anhvuive on 1/4/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "ViewController.h"
#import "MainReusableView.h"
#import "CollectionInCell.h"
#import "AppDelegate.h"
#import "Base.h"
#import "TitleBoxObject.h"
#import "CellObject.h"
@interface ViewController ()
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView1;

@end

@implementation ViewController



NSArray *arrImg;
NSMutableArray *arrData;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSString *URLString = [NSString stringWithFormat: @"http://api.foodplusvn.vn/api/mainscr?scr=600x600"];
    //  NSData *data = [NSData dataWithContentsOfURL: [NSURL URLWithString:url_string]];
    
    NSURL *url = [NSURL URLWithString:URLString];
    
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        // Check if any data returned.
        if (data != nil) {
            NSError *error;
            
            NSMutableDictionary *allCourses = [NSJSONSerialization
                                               JSONObjectWithData:data
                                               options:NSJSONReadingMutableContainers
                                               error:&error];
            
            NSArray *results=[allCourses valueForKey:@"content"];
            
            arrData=[[NSMutableArray alloc] init];
            for (NSDictionary *groupDic in results) {
                NSMutableArray *arrDataSection=[[NSMutableArray alloc] init];
                
                Base *base = [[Base alloc] initWithDictionary:groupDic error:&error];
                
                NSArray *titleBox=[NSArray arrayWithObjects:base, nil];
                
                CellObject *ob1=[[CellObject alloc] init];
                ob1.title=@"titlebox";
                ob1.arrData=titleBox;
                [arrDataSection addObject:ob1];
                NSArray *dataResults=[groupDic valueForKey:@"data"];
                
                for(NSDictionary *group in dataResults){
                    NSArray *datas=[group valueForKey:@"data"];
                    
                    NSMutableArray *arrDataCell=[[NSMutableArray alloc] init];
                    for(NSDictionary *item in datas){
                        Base *base = [[Base alloc] initWithDictionary:item error:&error];
                        [arrDataCell addObject:base];
                        
                    }
                    CellObject *ob=[[CellObject alloc] init];
                    ob.title=[group valueForKey:@"type"];
                    ob.arrData=arrDataCell;
                    [arrDataSection addObject:ob];
                    
                }
                
                [arrData addObject:arrDataSection];
            }
                       
        }
        [self.collectionView1 reloadData];
        
        
    }];

    
    UICollectionViewFlowLayout *flowlayout=(UICollectionViewFlowLayout*)self.collectionView1.collectionViewLayout;
    flowlayout.sectionInset=UIEdgeInsetsMake(5, 5, 5, 5);
    
    UINib *nib = [UINib nibWithNibName:@"CollectionCellNib" bundle:nil];
    [self.collectionView1 registerNib:nib forCellWithReuseIdentifier:@"CollectionInCell"];
}

- (NSArray *)arrayWithImages:(KIImagePager *)pager {
    return @[[UIImage imageNamed:@"icon_event_white-1"],
             [UIImage imageNamed:@"icon_food_white-1"],
             [UIImage imageNamed:@"icon_knife_white-1"],
             [UIImage imageNamed:@"icon_recipe_white-1"],
             [UIImage imageNamed:@"icon_coffee_white-1"],
             [UIImage imageNamed:@"icon_spa_white-1"]];
}

- (UIViewContentMode)contentModeForImage:(NSUInteger)image inPager:(KIImagePager *)pager {
    return UIViewContentModeScaleAspectFit;
}

-(UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath{
    UICollectionReusableView *reusableView=nil;
    if(kind==UICollectionElementKindSectionHeader){
        MainReusableView *headerView =[collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"HeaderBar" forIndexPath:indexPath];
        
        //headerView.bgImage.image=[UIImage imageNamed:@"pic_cafetra.jpg"];
       // [headerView setImg:@"pic_cafetra.jpg"];
        [headerView setUpSlider];
        reusableView=headerView;
    }
    return reusableView;
}

-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section{
    if(section==0){
        return CGSizeMake(CGRectGetWidth(collectionView.frame), 195);
    }else{
       return CGSizeZero;
    }
}

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return [[arrData objectAtIndex:section] count];
}
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return [arrData count];
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier=@"CollectionInCell";
    UICollectionViewCell *cell=nil;

    CellObject *obj=(CellObject*)[arrData[indexPath.section] objectAtIndex:indexPath.row];
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
  //  [cell setNeedsDisplay];
    
    return cell;
}
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    float width=CGRectGetWidth(collectionView.frame);
    CellObject *obj1=(CellObject*)[arrData[indexPath.section] objectAtIndex:indexPath.row];
    int count= (int) [obj1.arrData count];
    if([obj1.title isEqualToString:@"titlebox"]){
        return CGSizeMake(width-10, 40);
    }else if([obj1.title isEqualToString:@"gallery"]){
        return CGSizeMake(width-10, width/2);
    }else if([obj1.title isEqualToString:@"list"]){
        return CGSizeMake(width-10, 75*count);
    }else if([obj1.title isEqualToString:@"grid"]){
        return CGSizeMake(width-10, ((width-10)/2+10)*(count/2));
    }else if([obj1.title isEqualToString:@"big"]){
        return CGSizeMake(width-10, ((width-10)/2)*count);
    }else if([obj1.title isEqualToString:@"gridsimple"]){
        return CGSizeMake(width-10, ((width-10)/2)*(count/3));
    }else{
        return CGSizeMake(width-10, ((width-10)/2)*count);
    }
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
}


- (IBAction)menuAction:(id)sender {
    // Dismiss keyboard (optional)
    //
    [self.view endEditing:YES];
    self.frostedViewController.direction = REFrostedViewControllerDirectionRight;
    [self.frostedViewController.view endEditing:YES];
    
    // Present the view controller
    //
    [self.frostedViewController presentMenuViewController];
}
@end
