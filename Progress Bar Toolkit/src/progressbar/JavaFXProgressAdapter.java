package progressbar;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

/**
 * An adapter that updates a JavaFX {@link ProgressBar} from background worker threads.
 *
 * <p>
 * Ensures all UI modifications to the wrapped progress bar are scheduled safely on the JavaFX
 * Application Thread using {@link Platform#runLater(Runnable)}.
 * </p>
 *
 * @author Trevor Maggs
 * @version 0.4
 * @since 15 April 2026
 */
@SuppressWarnings("deprecation")
public class JavaFXProgressAdapter implements ProgressListener
{
    private final int min;
    private final int max;
    private final ProgressBar progressBar;
    private volatile long lastMetricsSnapshot = Long.MIN_VALUE;

    /**
     * Constructs an instance with a default range of 0 to 100.
     *
     * @param progressBar
     *        the JavaFX control to be updated
     */
    public JavaFXProgressAdapter(ProgressBar progressBar)
    {
        this(progressBar, 0, 100);
    }

    /**
     * Constructs an instance with a specific range.
     *
     * @param progressBar
     *        the JavaFX control to be updated
     * @param min
     *        the starting value of the range
     * @param max
     *        the ending value of the range
     */
    public JavaFXProgressAdapter(ProgressBar progressBar, int min, int max)
    {
        this.min = min;
        this.max = max;
        this.progressBar = progressBar;

        if (progressBar != null)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    progressBar.setProgress(0.0);
                }
            });
        }
    }

    /**
     * Updates the progress bar using the default maximum value defined at construction.
     *
     * @param current
     *        the current progress value
     */
    @Override
    public void onProgressUpdate(int current)
    {
        onProgressUpdate(current, 0);
    }

    /**
     * Updates the JavaFX progress bar with a dynamically specified total workload.
     *
     * <p>
     * Performs change detection on background updates using a 64-bit primitive state snapshot to
     * avoid queueing redundant {@link Platform#runLater(Runnable)} tasks onto the FX thread.
     * </p>
     *
     * @param current
     *        the current progress value
     * @param total
     *        the total workload value. If {@code 0} or less, the fallback maximum value specified
     *        at construction time is used
     */
    @Override
    public void onProgressUpdate(int current, int total)
    {
        if (progressBar != null)
        {
            int actualCurrent = Math.max(min, current);
            int actualMax = (total <= 0 ? max : total);

            if (min > actualMax)
            {
                return;
            }

            // Fast state comparison to prevent dispatching duplicate tasks to FX Application Thread
            long newMetrics = ((long) actualMax << 32) | (actualCurrent & 0xFFFFFFFFL);

            if (newMetrics == lastMetricsSnapshot)
            {
                return;
            }

            lastMetricsSnapshot = newMetrics;

            int range = actualMax - min;
            double progress = (range > 0) ? (double) (actualCurrent - min) / range : 0.0;

            // Bound double to [0.0, 1.0] interval
            double boundedProgress = Math.min(1.0, Math.max(0.0, progress));

            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    progressBar.setProgress(boundedProgress);
                }
            });
        }
    }

    /**
     * Resets the adapter state and resets the UI progress bar back to zero.
     */
    @Override
    public void reset()
    {
        this.lastMetricsSnapshot = Long.MIN_VALUE;

        if (progressBar != null)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    progressBar.setProgress(0.0);
                }
            });
        }
    }
}