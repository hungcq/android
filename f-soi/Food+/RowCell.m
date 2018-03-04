//
//  RowCell.m
//  Food+
//
//  Created by Anhvuive on 1/20/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "RowCell.h"
#import "DetailViewController.h"

@implementation RowCell

@synthesize pic, name, address, star, ridSP, rtid, rcid;

//int ridSP, rtid, rcid;
- (UIViewController *)parentViewController {
    UIResponder *responder = self;
    while ([responder isKindOfClass:[UIView class]])
        responder = [responder nextResponder];
    return (UIViewController *)responder;
}

- (void)setData:(Base *) base {
    [name setText:base.title];
    [address setText:base.description];
    star.text = [NSString stringWithFormat:@"%1.1f", [base.rating doubleValue]];
    [MyUtils loadImage:base.images[0] into:pic];
    ridSP = [base.id intValue];
    rtid = [base.tid intValue];
    rcid = [base.cid intValue];
}

-(void) touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    
}

- (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    UIStoryboard *mainStoryboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    DetailViewController *vc=(DetailViewController*)[mainStoryboard instantiateViewControllerWithIdentifier:@"DetailView"];
    [vc requestData:ridSP withType:rtid withCId:rcid andP:1];
    UIViewController *par=[self parentViewController];
    [par showViewController:vc sender:nil];

}
@end
