//
//  BigCell.m
//  Food+
//
//  Created by Anhvuive on 1/20/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "BigCell.h"
#import "DetailViewController.h"

@implementation BigCell
@synthesize pin, pic, name, like, rate, cmt, address, star, bidSP, btid, bcid;


-(void) setData:(Base *)base{
    [name setText:base.title];
    [address setText:base.description];
    [like setText:[NSString stringWithFormat:@"%d", [base.favourite intValue]]];
    [rate setText:[NSString stringWithFormat:@"%d", [base.star intValue]]];
    [cmt setText:[NSString stringWithFormat:@"%d", [base.comment intValue]]];
    [pin setText:[NSString stringWithFormat:@"%d", [base.pin intValue]]];
    [star setText:[NSString stringWithFormat:@"%1.1f", [base.rating doubleValue]]];
    [MyUtils loadImage:base.images[0] into:pic];
    bidSP = [base.id intValue];
    btid = [base.tid intValue];
    bcid = [base.cid intValue];
}

- (UIViewController *)parentViewController {
    UIResponder *responder = self;
    while ([responder isKindOfClass:[UIView class]])
        responder = [responder nextResponder];
    return (UIViewController *)responder;
}

-(void) touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    
}

-(void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    //  NSLog(@"cid%d", ridSP);
    UIStoryboard *mainStoryboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    DetailViewController *vc=(DetailViewController*)[mainStoryboard instantiateViewControllerWithIdentifier:@"DetailView"];
    [vc requestData:bidSP withType:btid withCId:bcid andP:1];
    UIViewController *par=[self parentViewController];
    [par showViewController:vc sender:nil];
}


@end
