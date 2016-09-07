package raijin.session24_objectanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ObjectAnimator objectAnimator;
    private ObjectAnimator objectAnimator2;
    private AnimatorSet animatorSet;
    private float angle;
    private boolean max = true;
    private int x;
    private int y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        int location[] = new int[2];
        y = location[1];
        x = location[0];
        button.getLocationOnScreen(location);
        objectAnimator = ObjectAnimator.ofFloat(button, "y", y + button.getHeight()/2, getScreenHeight() - 300f);
        objectAnimator.setDuration(3000);
        objectAnimator2 = ObjectAnimator.ofFloat(button, "x", x + button.getWidth()/2, getScreenWidth());
        objectAnimator2.setDuration(3000);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, objectAnimator2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        angle = 0f;
    }
    public void onClick(View view) {
//        if (max) {
//            objectAnimator = ObjectAnimator.ofFloat(button, "y", y + button.getHeight()/2, getScreenHeight() - 300f);
//            max = false;
//        } else {
//            objectAnimator = ObjectAnimator.ofFloat(button, "y", getScreenHeight() - 300f, y + button.getHeight()/2);
//            max = true;
//        }
//        objectAnimator.setDuration(1000);
//        objectAnimator.start();
//        angle += 90f;
        animatorSet.start();
    }

    private float getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    private float getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    public static int getNavigationHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
