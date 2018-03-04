//
//  TitleBoxCell.m
//  Food+
//
//  Created by Anhvuive on 1/17/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "TitleBoxCell.h"
#import "CategoryViewController.h"

@implementation TitleBoxCell

@synthesize name, icon;
int ttid, tcid;
-(void)setData:(Base *)base{
    ttid=[base.tid intValue];
    tcid=[base.cid intValue];
    name.text = base.title;
    [MyUtils loadImage:base.icon into:icon];
}

- (UIViewController *)parentViewController {
    UIResponder *responder = self;
    while ([responder isKindOfClass:[UIView class]])
        responder = [responder nextResponder];
    return (UIViewController *)responder;
}

-(void) touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    
}

-(void) touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    UIStoryboard *mainStoryboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    CategoryViewController *vc=(CategoryViewController*)[mainStoryboard instantiateViewControllerWithIdentifier:@"CategoryView"];
    
    [vc requestData:ttid withCId:tcid andP:1];
    UIViewController *par=[self parentViewController];
    [par showViewController:vc sender:nil];
}

@end
