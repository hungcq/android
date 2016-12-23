package raijin.taxi69.models;
import android.animation.TypeEvaluator;

/**
 * Inspired from {@link android.animation.FloatArrayEvaluator}
 * <p/>
 * This evaluator can be used to perform type interpolation between <code>double[]</code> values.
 * Each index into the array is treated as a separate value to interpolate. For example,
 * evaluating <code>{100, 200}</code> and <code>{300, 400}</code> will interpolate the value at
 * the first index between 100 and 300 and the value at the second index value between 200 and 400.
 */
public class DoubleArrayEvaluator implements TypeEvaluator<double[]> {

    private double[] mArray;

    /**
     * Create a DoubleArrayEvaluator that does not reuse the animated value. Care must be taken
     * when using this option because on every evaluation a new <code>double[]</code> will be
     * allocated.
     *
     * @see #DoubleArrayEvaluator(double[])
     */
    public DoubleArrayEvaluator() {
    }

    /**
     * Create a DoubleArrayEvaluator that reuses <code>reuseArray</code> for every evaluate() call.
     * Caution must be taken to ensure that the value returned from
     * {@link android.animation.ValueAnimator#getAnimatedValue()} is not cached, modified, or
     * used across threads. The value will be modified on each <code>evaluate()</code> call.
     *
     * @param reuseArray The array to modify and return from <code>evaluate</code>.
     */
    public DoubleArrayEvaluator(double[] reuseArray) {
        mArray = reuseArray;
    }

    /**
     * Interpolates the value at each index by the fraction. If
     * {@link #DoubleArrayEvaluator(double[])} was used to construct this object,
     * <code>reuseArray</code> will be returned, otherwise a new <code>double[]</code>
     * will be returned.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue The start value.
     * @param endValue   The end value.
     * @return A <code>double[]</code> where each element is an interpolation between
     * the same index in startValue and endValue.
     */
    @Override
    public double[] evaluate(float fraction, double[] startValue, double[] endValue) {
        double[] array = mArray;
        if (array == null) {
            array = new double[startValue.length];
        }

        for (int i = 0; i < array.length; i++) {
            double start = startValue[i];
            double end = endValue[i];
            array[i] = start + (fraction * (end - start));
        }
        return array;
    }
}