//
//  SmallCell.m
//  Food+
//
//  Created by Anhvuive on 1/20/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "SmallCell.h"
#import "DetailViewController.h"

@implementation SmallCell
@synthesize pic, name, sidSP, stid, scid;

-(void) setData:(Base *)base{
    sidSP = [base.id intValue];
    stid = [base.tid intValue];
    scid = [base.cid intValue];
    [name setText:base.title];
    [MyUtils loadImage:base.images[0] into:pic];
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
    UIStoryboard *mainStoryboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    DetailViewController *vc=(DetailViewController*)[mainStoryboard instantiateViewControllerWithIdentifier:@"DetailView"];
    [vc requestData:sidSP withType:stid withCId:scid andP:1];
    UIViewController *par=[self parentViewController];
    [par showViewController:vc sender:nil];
}

@end
