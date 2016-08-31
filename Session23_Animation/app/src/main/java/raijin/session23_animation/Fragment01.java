package raijin.session23_animation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment01 extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private Animation animationIn;
    private Animation animationOut;

    public Fragment01() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment01, container, false);
        initialize(view);
        return view;
    }


    private void initialize(View view) {
        imageView = (ImageView) view.findViewById(R.id.image_aa);
        imageView.setOnClickListener(this);

        animationIn = AnimationUtils.loadAnimation(view.getContext(), R.anim.zoom_in);
        animationOut = AnimationUtils.loadAnimation(view.getContext(), R.anim.zoom_out);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.trans_in, R.anim.trans_out,
                R.anim.trans_back_in, R.anim.trans_back_out);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        imageView.startAnimation(animationOut);
        animationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openFragment(new Fragment02());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animationIn);
    }
}
