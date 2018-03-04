//
//  DetailViewController.m
//  Food+
//
//  Created by Anhvuive on 2/3/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "DetailViewController.h"
#import "AppDelegate.h"
#import "Base.h"
#import "NSString+HTML.h"
#import "DetailReusableView.h"
#import "CollectionInCell.h"
#import "CellObject.h"
#import "CellContent.h"

@interface DetailViewController ()
@property (weak, nonatomic) IBOutlet UICollectionView *myCollectionView;

@end

@implementation DetailViewController

@synthesize idSP, idLoai, isPin, isLike, longitude, latitude, rate, address, titName, urlImages, arrRate, content;

NSMutableArray *arrDataDetail;

- (void)viewDidLoad {
    [super viewDidLoad];
    
  UINib *nib = [UINib nibWithNibName:@"CollectionCellNib" bundle:nil];
    [self.myCollectionView registerNib:nib forCellWithReuseIdentifier:@"CollectionInCell"];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}
-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    //NSLog(@"count==%d", arrDataDetail.count);
    return arrDataDetail.count+1;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    UICollectionViewCell *cell=nil;
    if(indexPath.row==0){
        CellContent *contentCell=(CellContent*)[collectionView dequeueReusableCellWithReuseIdentifier:@"CellContent" forIndexPath:indexPath];
        [contentCell.webView loadHTMLString:[NSString stringWithFormat: @"<style>img{max-width:100%@;}</style>%@",@"%",self.content] baseURL:nil];
        NSLog(@"Height==%f", contentCell.webView.scrollView.contentSize.height);
        cell=contentCell;
    }else{
        CellObject *obj=(CellObject*)[arrDataDetail objectAtIndex:(indexPath.row-1)];
        CollectionInCell *collectionCell=(CollectionInCell*)[collectionView dequeueReusableCellWithReuseIdentifier:@"CollectionInCell" forIndexPath:indexPath];
        
        NSString *type=obj.title;
    //NSLog(type);
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
    
   }
    
    return cell;
}

-(UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath{
    UICollectionReusableView *reusableView=nil;
    if(kind==UICollectionElementKindSectionHeader){
        DetailReusableView *headerView =[collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"DetailReusable" forIndexPath:indexPath];
        [headerView.lbTit setText:self.titName];
        headerView.lbRate.text=[NSString stringWithFormat:@"%.1f", self.rate];
        UIImageView * imgView =headerView.pic1;
        if(self.urlImages.count>0){
            switch (self.urlImages.count) {
                case 1:
                    {
                      [self setImageFor:imgView with:[self.urlImages objectAtIndex:0]];
                        headerView.morepic.hidden=YES;

                    }
                    break;
                case 2:
                {
                    [self setImageFor:imgView with:[self.urlImages objectAtIndex:0]];
                    headerView.morepic.hidden=NO;
                    [self setImageFor:headerView.pic2 with:[self.urlImages objectAtIndex:1]];
                    headerView.pic3.hidden=YES;
                    headerView.pic4.hidden=YES;
                    headerView.pic5.hidden=YES;
                    
                }
                    break;

                case 3:
                {
                    [self setImageFor:imgView with:[self.urlImages objectAtIndex:0]];
                    headerView.morepic.hidden=NO;
                    [self setImageFor:headerView.pic2 with:[self.urlImages objectAtIndex:1]];
                    headerView.pic3.hidden=NO;
                    headerView.pic4.hidden=YES;
                    headerView.pic5.hidden=YES;
                    [self setImageFor:headerView.pic3 with:[self.urlImages objectAtIndex:2]];
                    
                }
                    break;

                case 4:
                {
                    [self setImageFor:imgView with:[self.urlImages objectAtIndex:0]];
                    headerView.morepic.hidden=NO;
                    [self setImageFor:headerView.pic2 with:[self.urlImages objectAtIndex:1]];
                    headerView.pic3.hidden=NO;
                    headerView.pic4.hidden=NO;
                    headerView.pic5.hidden=YES;
                    [self setImageFor:headerView.pic3 with:[self.urlImages objectAtIndex:2]];
                    [self setImageFor:headerView.pic4 with:[self.urlImages objectAtIndex:3]];
                }
                    break;

                case 5:
                {
                    [self setImageFor:imgView with:[self.urlImages objectAtIndex:0]];
                    headerView.morepic.hidden=NO;
                    [self setImageFor:headerView.pic2 with:[self.urlImages objectAtIndex:1]];
                    headerView.pic3.hidden=NO;
                    headerView.pic4.hidden=NO;
                    headerView.pic5.hidden=NO;
                    [self setImageFor:headerView.pic3 with:[self.urlImages objectAtIndex:2]];
                    [self setImageFor:headerView.pic4 with:[self.urlImages objectAtIndex:3]];
                    [self setImageFor:headerView.pic5 with:[self.urlImages objectAtIndex:4]];

                }
                    break;

                    
                default:
                {
                    [self setImageFor:imgView with:[self.urlImages objectAtIndex:0]];
                    headerView.morepic.hidden=NO;
                    [self setImageFor:headerView.pic2 with:[self.urlImages objectAtIndex:1]];
                    headerView.pic3.hidden=NO;
                    headerView.pic4.hidden=NO;
                    headerView.pic5.hidden=NO;
                    [self setImageFor:headerView.pic3 with:[self.urlImages objectAtIndex:2]];
                    [self setImageFor:headerView.pic4 with:[self.urlImages objectAtIndex:3]];
                    [self setImageFor:headerView.pic5 with:[self.urlImages objectAtIndex:4]];
                    
                }
                    break;
            }
        }else{
            
        }
        reusableView=headerView;
        
    }
    return reusableView;
}

-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section{
    float width=CGRectGetWidth(collectionView.frame);
    if(self.urlImages.count>1){
        return CGSizeMake(width, 3*width/4);
    }else{
        return CGSizeMake(width, width/2);
    }
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    float width=CGRectGetWidth(collectionView.frame);
    if(indexPath.row==0){
        return CGSizeMake(width-10, 848);
    }else{
        CellObject *obj1=(CellObject*)[arrDataDetail objectAtIndex:indexPath.row-1];
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
}
-(void) requestData:(int) i withType:(int) t withCId:(int) cid andP:(int)p{
    
    NSString *URLString = [NSString stringWithFormat: @"http://api.foodplusvn.vn/api/detail?t=%d&id=%d&cid=%d&p=%d&scr=600x600", t, i, cid,p];
   // NSLog(URLString);
    NSURL *url = [NSURL URLWithString:URLString];
    
    [MyUtils downloadDataFromURL:url withCompletionHandler:^(NSData *data) {
        // Check if any data returned.
        if (data != nil) {
            NSError *error;
            
            //NSLog(@"json: %@", data);
            
            NSMutableDictionary *datas = [NSJSONSerialization
                                          JSONObjectWithData:data
                                          options:NSJSONReadingMutableContainers
                                          error:&error];
            
            NSDictionary *results=[datas valueForKey:@"detail"];
            NSDictionary *object=[ results valueForKey:@"object"];
            self.idLoai=t;
            [self readContent:object];
            
            arrDataDetail=[[NSMutableArray alloc] init];
            NSArray *dataResults=[datas valueForKey:@"content"];
            //NSLog(@"crrrr==%d", dataResults.count);
            for(NSDictionary *group in dataResults){
                NSArray *datas=[group valueForKey:@"data"];
                
                NSMutableArray *arrDataCell=[[NSMutableArray alloc] init];
                // NSLog(@"item%d", datas[0]);
                for(NSDictionary *item in datas){
                    // NSLog(@"item%d", item);
                    Base *base=[[Base alloc] initWithDictionary:item error:&error];
                    [arrDataCell addObject:base];
                    
                }
                CellObject *ob=[[CellObject alloc] init];
                ob.title=[group valueForKey:@"type"];
                ob.arrData=arrDataCell;
                [arrDataDetail addObject:ob];
            }

            [self.myCollectionView reloadData];

        }
        
        
    }];
    
}

-(void) setImageFor:(UIImageView*) imgView with:(NSString*) url{
    NSOperationQueue *myQueue = [[NSOperationQueue alloc] init];
    [myQueue addOperationWithBlock:^{
        UIImage *img =  [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:url]]]?:[UIImage imageNamed:@"pic.jpg"];
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{
            imgView.image = img;
            
        }];
    }];
}

-(void) readContent:(NSDictionary*) item{
    self.idSP=[[item valueForKey:@"id"] intValue];
    self.titName=[item valueForKey:@"title"];
    self.idDM=[[item valueForKey:@"cid"] intValue];
    NSString *s=[item valueForKey:@"description"];
    if(![s isKindOfClass:[NSNull class]]){
        self.address=s;
    }else{
        self.address=@"";
    }
    //NSLog(self.titName);
    self.rate=[[item valueForKey:@"rating"] floatValue];
    self.longitude=[[item valueForKey:@"longitude"] floatValue];
    self.latitude=[[item valueForKey:@"latitude"] floatValue];
    self.content=[item valueForKey:@"content"];
    self.urlImages=[item valueForKey:@"images"];
    //self.arrRate=[item valueForKey:@"rate"];
    self.isLike=[[item valueForKey:@"is_like"] intValue];
    self.isPin=[[item valueForKey:@"is_spin"] intValue];
}

@end
