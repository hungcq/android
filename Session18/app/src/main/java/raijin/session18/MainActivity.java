package raijin.session18;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    DefaultSliderView sliderView;
    for (Số lượng image) {
        sliderView = new DefaultSliderView(context);
        sliderView.image(url or đường dẫn ảnh trong bộ nhớ);
        sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
        sliderLayout.addSlider(sliderView);
    }

    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    sliderLayout.setCustomAnimation(new DescriptionAnimation());
    sliderLayout.setDuration(3000);
}
