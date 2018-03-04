//
//  GalleryCell.m
//  Food+
//
//  Created by Anhvuive on 1/4/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "GalleryCell.h"
#import "Base.h"
#import "DetailViewController.h"


@implementation GalleryCell

@synthesize pic1, pic2, pic3, pic4, pic5, name, diaChi, like, rate, cmt, pin, arrSP, idDM, idSP, idLoai;

- (UIViewController *)parentViewController {
    UIResponder *responder = self;
    while ([responder isKindOfClass:[UIView class]])
        responder = [responder nextResponder];
    return (UIViewController *)responder;
}

- (IBAction)clickPic1:(id)sender {
    UIStoryboard *mainStoryboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
   // UIViewController *vc=[mainStoryboard instantiateViewControllerWithIdentifier:@"DetailView"];
    DetailViewController *vc=(DetailViewController*)[mainStoryboard instantiateViewControllerWithIdentifier:@"DetailView"];
    [vc requestData:idSP withType:idDM withCId:idLoai andP:1];
    UIViewController *par=[self parentViewController];
    [par showViewController:vc sender:nil];
}

- (void)setData:(NSArray *)data{
    arrSP=data;
    for (int i=0; i<[arrSP count]; i++) {
        Base *base=(Base *)[arrSP objectAtIndex:i];
        switch (i) {
            case 0:
            {
                idSP=[base.id intValue];
                idLoai= [base.tid intValue];
                idDM= [base.cid intValue];
                [name setText:base.title];
                [diaChi setText:base.description];
                [like setText:[NSString stringWithFormat:@"%d", [base.favourite intValue]]];
                [rate setText:[NSString stringWithFormat:@"%d", [base.star intValue]]];
                [cmt setText:[NSString stringWithFormat:@"%d", [base.comment intValue]]];
                [pin setText:[NSString stringWithFormat:@"%d", [base.pin intValue]]];
                NSOperationQueue *myQueue = [[NSOperationQueue alloc] init];
                [myQueue addOperationWithBlock:^{
                    UIImage *img =  [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:base.images[0]]]]?:[UIImage imageNamed:@"pic.jpg"];
                    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
                        [pic1 setImage:img forState:UIControlStateNormal];
                    }];
                }];
            }
            case 1:
                [MyUtils loadImage:base.images[0] into:pic2];
                break;
            case 2:
                [MyUtils loadImage:base.images[0] into:pic3];
                break;
            case 3:
                [MyUtils loadImage:base.images[0] into:pic4];
                break;
            case 4:
                [MyUtils loadImage:base.images[0] into:pic5];
                break;
            default:
                break;
        }
    }
    [pic2 setUserInteractionEnabled:YES];
    [pic3 setUserInteractionEnabled:YES];
    [pic4 setUserInteractionEnabled:YES];
    [pic5 setUserInteractionEnabled:YES];
}


-(void) touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
   
}


-(void) touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    UITouch *touch=[touches anyObject];
    Base *base;
    if([touch view] ==pic2){
        base = (Base *) [arrSP objectAtIndex:1];
    }else if([touch view] ==pic3){
        base = (Base *) [arrSP objectAtIndex:2];
        
    }else if([touch view] ==pic4){
        base = (Base *) [arrSP objectAtIndex:3];
    }else if([touch view] ==pic5){
        base = (Base *) [arrSP objectAtIndex:4];
    }else{
        base = nil;
    }
    if(base != nil){
        UIStoryboard *mainStoryboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
        DetailViewController *vc=(DetailViewController*)[mainStoryboard instantiateViewControllerWithIdentifier:@"DetailView"];
        [vc requestData:[base.id intValue] withType:[base.tid intValue] withCId:[base.cid intValue] andP:1];
        UIViewController *par=[self parentViewController];
        [par showViewController:vc sender:nil];
    }
}


-(void)init:(NSString *)img{
    [pic1 setImage:[UIImage imageNamed:img] forState:UIControlStateNormal];
    pic2.image=[UIImage imageNamed:img];
    pic3.image=[UIImage imageNamed:img];
    pic4.image=[UIImage imageNamed:img];
    pic5.image=[UIImage imageNamed:img];
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}



@end
