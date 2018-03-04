//
//  NavContentViewController.m
//  Food+
//
//  Created by Raijin Thunderkeg on 3/7/17.
//  Copyright © 2017 Anhvuive. All rights reserved.
//

#import "NavContentViewController.h"

@interface NavContentViewController ()

@end

@implementation NavContentViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.tableView.separatorColor = [UIColor colorWithRed:150/255.0f green:161/255.0f blue:177/255.0f alpha:1.0f];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.opaque = NO;
    self.tableView.backgroundColor = [UIColor whiteColor];
    self.tableView.tableHeaderView = ({
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 0, 184.0f)];
        view.backgroundColor = [MyUtils colorFromHexString:@"#6ea516"];
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 40, 100, 100)];
        imageView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
        imageView.image = [UIImage imageNamed:@"ic_user_round"];
        imageView.layer.masksToBounds = YES;
        imageView.layer.cornerRadius = 50.0;
        imageView.layer.borderColor = [UIColor whiteColor].CGColor;
        imageView.layer.borderWidth = 3.0f;
        imageView.layer.rasterizationScale = [UIScreen mainScreen].scale;
        imageView.layer.shouldRasterize = YES;
        imageView.clipsToBounds = YES;
        
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 150, 0, 24)];
        label.text = @"Raijin Thunderkeg";
        label.font = [UIFont fontWithName:@"HelveticaNeue" size:21];
        label.backgroundColor = [UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        [label sizeToFit];
        label.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
        
        [view addSubview:imageView];
        [view addSubview:label];
        view;
    });
}

#pragma mark -
#pragma mark UITableView Delegate

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    cell.backgroundColor = [UIColor whiteColor];
    UIView *selectedView = [[UIView alloc] initWithFrame:cell.frame];
    selectedView.backgroundColor = [MyUtils colorFromHexString:@"#6ea516"];
    cell.selectedBackgroundView = selectedView;
    cell.textLabel.textColor = [MyUtils colorFromHexString:@"#508003"];
    cell.textLabel.highlightedTextColor = [UIColor whiteColor];
    cell.textLabel.font = [UIFont fontWithName:@"HelveticaNeue" size:17];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)sectionIndex
{
    if (sectionIndex == 0)
        return nil;
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 34)];
    view.backgroundColor = [UIColor colorWithRed:167/255.0f green:167/255.0f blue:167/255.0f alpha:0.6f];

    
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 8, 0, 0)];
    label.text = @"Khác";
    label.font = [UIFont systemFontOfSize:15];
    label.textColor = [UIColor whiteColor];

    label.backgroundColor = [UIColor clearColor];
    [label sizeToFit];
    [view addSubview:label];
    
    return view;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)sectionIndex
{
    if (sectionIndex == 0)
        return 0;
    
    return 34;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    NavigationViewController *navigationController = [self.storyboard instantiateViewControllerWithIdentifier:@"NavView"];
    ViewController *viewController = [self.storyboard instantiateViewControllerWithIdentifier:@"MainView"];
    CategoryViewController *categoryViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"CategoryView"];
    NSMutableArray *navigationArray = [[NSMutableArray alloc] initWithArray: navigationController.viewControllers];
    [navigationArray removeAllObjects];
    [navigationArray addObject:viewController];
    navigationController.viewControllers = navigationArray;

    // trang chu
    if (indexPath.section == 0 && indexPath.row == 0) {
        navigationController.viewControllers = @[viewController];
    } else if(indexPath.section == 0 && indexPath.row == 1) {
        // thuc pham
        [categoryViewController requestData:1 withCId:43 andP:1];
        [navigationController showViewController:categoryViewController sender:nil];
    } else if(indexPath.section == 0 && indexPath.row == 2) {
        // mon an
        [categoryViewController requestData:2 withCId:21 andP:1];
        [navigationController showViewController:categoryViewController sender:nil];
    } else if(indexPath.section == 0 && indexPath.row == 3) {
        // nau an
        [categoryViewController requestData:3 withCId:42 andP:1];
        [navigationController showViewController:categoryViewController sender:nil];
    } else if(indexPath.section == 0 && indexPath.row == 4) {
        // do uong
        [categoryViewController requestData:4 withCId:41 andP:1];
        [navigationController showViewController:categoryViewController sender:nil];
    } else if(indexPath.section == 0 && indexPath.row == 5) {
        // mon an
        [categoryViewController requestData:5 withCId:44 andP:1];
        [navigationController showViewController:categoryViewController sender:nil];
    } else if(indexPath.section == 1 && indexPath.row == 0) {
        LogInViewController *logInViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LogInView"];
        [navigationController showViewController:logInViewController sender:nil];
    } else if(indexPath.section == 1 && indexPath.row == 1) {
        PinViewController *pinViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"PinView"];
        [navigationController showViewController:pinViewController sender:nil];
    } else if(indexPath.section == 1 && indexPath.row == 2) {
        NewsViewController *newsViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"NewsView"];
        [navigationController showViewController:newsViewController sender:nil];
    } else if(indexPath.section == 1 && indexPath.row == 3) {
        NewsViewController *newsViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"NewsView"];
        [navigationController showViewController:newsViewController sender:nil];
    }
    
    self.frostedViewController.contentViewController = navigationController;
    [self.frostedViewController hideMenuViewController];
}

#pragma mark -
#pragma mark UITableView Datasource

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 54;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)sectionIndex
{
    if(sectionIndex == 0) return 6;
    return 4;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    
    if (indexPath.section == 0) {
        NSArray *titles = @[@"Trang chủ", @"Thực phẩm sạch", @"Món ăn", @"Công thức nấu ăn",
                        @"Đồ uống", @"Khoẻ và đẹp"];
        cell.textLabel.text = titles[indexPath.row];
    } else {
        NSArray *titles = @[@"Tài khoản", @"Bài ghim", @"Sự kiện, khuyến mại", @"Fanpage"];
        cell.textLabel.text = titles[indexPath.row];
    }
    
    return cell;
}

@end
