//
//  NewsCell.m
//  Food+
//
//  Created by Raijin Thunderkeg on 2/28/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "NewsCell.h"

@implementation NewsCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (UIViewController *)parentViewController {
    UIResponder *responder = self;
    while ([responder isKindOfClass:[UIView class]])
        responder = [responder nextResponder];
    return (UIViewController *)responder;
}

- (void) setData:(Base *) base {
    _titleLabel.text = base.te;
    _contentLabel.text = base.mota;
    _timeLabel.text = base.ngaytao;
    _imageView.image = nil;
    [MyUtils loadImage:[base.images objectAtIndex:0] into:_imageView];
    _id = [base.id intValue];
}

-(void) touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    
}

- (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    UIStoryboard *mainStoryboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    NewsDetailViewController *viewController = (NewsDetailViewController *) [mainStoryboard instantiateViewControllerWithIdentifier:@"NewsDetailView"];
    [viewController loadData:_id];
    UIViewController *parrent = [self parentViewController];
    [parrent showViewController:viewController sender:nil];
}

@end
