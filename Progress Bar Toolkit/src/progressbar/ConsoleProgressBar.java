package progressbar;

import java.util.Arrays;

/**
 * A stateful {@link ProgressListener} that renders an ASCII progress bar to the standard output
 * stream. It mimics the behaviour of a {@code JProgressBar} for command-line applications.
 *
 * @author Trevor Maggs
 * @version 0.4
 * @since 15 April 2026
 */
public final class ConsoleProgressBar implements ProgressListener
{
    private static final int BAR_WIDTH = 50;
    private final char[] barBuffer = new char[BAR_WIDTH];
    private final int min;
    private final int max;
    private long lastMetricsSnapshot = Long.MIN_VALUE;
    private boolean done;

    /**
     * Constructs an instance with a default range of 0 to 100.
     */
    public ConsoleProgressBar()
    {
        this(0, 100);
    }

    /**
     * Constructs a progress bar with the specified minimum and maximum values.
     *
     * @param min
     *        the starting value of the range
     * @param max
     *        the ending value of the range
     */
    public ConsoleProgressBar(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    /**
     * Updates the progress using the maximum value specified when this progress bar was
     * constructed.
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
     * Updates the progress bar with a dynamically specified total workload.
     *
     * <p>
     * The current progress and total workload values are packed into a single 64-bit state
     * snapshot. The progress bar is re-rendered only when this snapshot differs from the previous
     * update, preventing redundant console redraws (flickering) while preserving exact change
     * detection.
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
        int actualCurrent = Math.max(min, current);
        int actualMax = (total <= 0 ? max : total);

        if (actualCurrent < actualMax)
        {
            done = false;
        }

        if (done || min > actualMax)
        {
            return;
        }

        // Compute single 64-bit primitive snapshot for fast comparison and prevent flickering
        long newMetrics = ((long) actualMax << 32) | (actualCurrent & 0xFFFFFFFFL);

        if (newMetrics == lastMetricsSnapshot)
        {
            return;
        }

        int range = actualMax - min;
        int percent = (range > 0) ? (int) (((double) (actualCurrent - min) / range) * 100) : 0;

        lastMetricsSnapshot = newMetrics;

        render(current, actualMax, percent);

        if (actualCurrent >= actualMax || percent >= 100)
        {
            System.out.println();
            System.out.println();
            done = true;
        }
    }

    /**
     * Resets the progress bar so it can be reused for another operation.
     */
    @Override
    public void reset()
    {
        this.done = false;
        this.lastMetricsSnapshot = Long.MIN_VALUE;
    }

    /**
     * Renders the current progress bar state to the terminal.
     *
     * @param current
     *        the current progress value
     * @param total
     *        the total target value
     * @param percent
     *        the pre-calculated percentage (0-100)
     */
    private void render(int current, int total, int percent)
    {
        int filled = (percent * BAR_WIDTH) / 100;

        Arrays.fill(barBuffer, 0, filled, '=');

        if (filled < BAR_WIDTH)
        {
            barBuffer[filled] = (current < total) ? '>' : ' ';

            if (filled + 1 < BAR_WIDTH)
            {
                Arrays.fill(barBuffer, filled + 1, BAR_WIDTH, ' ');
            }
        }

        System.out.print("\r[");
        System.out.print(barBuffer);
        System.out.printf("] %3d%%", percent);
    }
}