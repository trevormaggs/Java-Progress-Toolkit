package progressbar;

/**
 * A stateful ProgressListener that renders an ASCII progress bar to the standard output stream.
 * Mimics the behaviour of a JProgressBar for command-line interfaces.
 * 
 * @author Trevor Maggs
 * @version 0.3
 * @since 15 April 2026
 */
public final class ConsoleProgressBar implements ProgressListener
{
    private static final int BAR_WIDTH = 50;
    private final int min;
    private final int max;
    private int lastPercent = -1;

    /**
     * Constructs an instance with a default range of 0 to 100.
     */
    public ConsoleProgressBar()
    {
        this(0, 100);
    }

    /**
     * Constructs an instance with a specific range.
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
     * Updates the progress using the default maximum value defined at construction.
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
     * Updates the progress with a dynamically specified total.
     * 
     * <p>
     * This method calculates the completion percentage and triggers a re-render only if the
     * percentage has changed since the last call. This prevents unnecessary console flicker. If the
     * current value meets or exceeds the total, a new line is appended.
     * </p>
     * 
     * @param current
     *        the current progress value
     * @param total
     *        the total value. If it is 0 or less, the max value defined at constructor is assumed
     */
    @Override
    public void onProgressUpdate(int current, int total)
    {
        int actualMax = (total > 0 ? total : this.max);

        if (actualMax <= min)
        {
            return;
        }

        int safeCurrent = Math.max(min, current);
        int percent = (int) (((double) (safeCurrent - min) / (actualMax - min)) * 100);

        // Efficiency check: only redraw if the percentage has actually incremented
        if (percent == lastPercent && current < actualMax)
        {
            return;
        }

        lastPercent = percent;

        render(current, actualMax, percent);

        if (current >= actualMax)
        {
            System.out.println();
            lastPercent = -1;
        }
    }

    /**
     * Performs the actual terminal rendering using carriage returns and string formatting.
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
        StringBuilder sb = new StringBuilder(BAR_WIDTH + 10);
        int filledWidth = (percent * BAR_WIDTH) / 100;

        sb.append("\r[");

        for (int i = 0; i < BAR_WIDTH; i++)
        {
            if (i < filledWidth)
            {
                sb.append("=");
            }

            else if (i == filledWidth && current < total)
            {
                sb.append(">");
            }

            else
            {
                sb.append(" ");
            }
        }

        sb.append("] %3d%%");

        System.out.printf(sb.toString(), percent);
    }
}