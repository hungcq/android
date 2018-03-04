//
//  MyUtils.h
//  Food+
//
//  Created by Raijin Thunderkeg on 2/16/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import <SDWebImage/UIImageView+WebCache.h>

@interface MyUtils : NSObject
+ (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize;
+ (double)getDeviceHeight;
+ (double)getDeviceWidth;
+ (UIColor *)colorFromHexString:(NSString *)hexString;
+ (void)downloadDataFromURL:(NSURL *)url withCompletionHandler:(void (^)(NSData *))completionHandler;
+ (void) loadImage:(NSString *) urlString into:(UIImageView *) image;
+ (NSString *) sub20string: (NSString *) input;
@end
