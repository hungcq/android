//
//  GridCell.m
//  Food+
//
//  Created by Anhvuive on 1/20/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "GridCell.h"
#import "DetailViewController.h"

@implementation GridCell

@synthesize pic, name, address, like, rate, cmt, pin, gidSP, gtid, gcid;

-(void) setData:(Base *)base{
    
   
    [name setText:base.title];
    [address setText:base.description];
    [like setText:[NSString stringWithFormat:@"%d", [base.favourite intValue]]];
    [rate setText:[NSString stringWithFormat:@"%d", [base.star intValue]]];
    [cmt setText:[NSString stringWithFormat:@"%d", [base.comment intValue]]];
    [pin setText:[NSString stringWithFormat:@"%d", [base.pin intValue]]];
    [MyUtils loadImage:base.images[0] into:pic];
    
    gidSP = [base.id intValue];
    gtid = [base.tid intValue];
    gcid = [base.cid intValue];
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
    [vc requestData:gidSP withType:gtid withCId:gcid andP:1];
    UIViewController *par=[self parentViewController];
    [par showViewController:vc sender:nil];
    
}
@end
