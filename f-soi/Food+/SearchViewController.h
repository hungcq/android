//
//  SearchViewController.h
//  Food+
//
//  Created by Raijin Thunderkeg on 3/3/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RowCell.h"
#import "Base.h"
#import "MyUtils.h"

@interface SearchViewController : UIViewController <
UICollectionViewDelegate,
UICollectionViewDataSource,
UICollectionViewDelegateFlowLayout,
UITextFieldDelegate>
@property (strong, nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong, nonatomic) IBOutlet UITextField *textField;
- (IBAction)textChanged:(id)sender;

@end
