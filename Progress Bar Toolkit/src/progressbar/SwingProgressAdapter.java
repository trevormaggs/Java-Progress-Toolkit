package progressbar;

import javax.swing.JProgressBar;

/**
 * An adapter that connects a {@link ProgressListener} to a Swing {@link JProgressBar}.
 * 
 * <p>
 * This class translates progress updates into Swing-compatible calls, allowing background tasks to
 * update a graphical progress bar without being directly coupled to the Swing API.
 * </p>
 * 
 * @author Trevor Maggs
 * @version 0.2
 * @since 21 April 2026
 */
public class SwingProgressAdapter implements ProgressListener
{
    private final int max;
    private final JProgressBar progressBar;

    /**
     * Constructs an adapter for a specific JProgressBar with a default range of 0 to 100.
     * 
     * @param progressBar
     *        the Swing component to be updated
     */
    public SwingProgressAdapter(JProgressBar progressBar)
    {
        this(progressBar, 0, 100);
    }

    /**
     * Constructs an adapter with a specific progress range.
     * 
     * @param progressBar
     *        the Swing component to be updated
     * @param min
     *        the minimum value of the progress range
     * @param max
     *        the maximum value of the progress range
     */
    public SwingProgressAdapter(JProgressBar progressBar, int min, int max)
    {
        this.max = max;
        this.progressBar = progressBar;
        this.progressBar.setMinimum(min);
        this.progressBar.setMaximum(max);
        this.progressBar.setValue(min);
    }

    /**
     * Updates the progress bar value using the default maximum.
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
     * Updates the progress bar value and optionally synchronises the maximum range.
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

        progressBar.setMaximum(actualMax);
        progressBar.setValue(current);
    }
}