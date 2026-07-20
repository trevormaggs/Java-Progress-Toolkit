package progressbar;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

@SuppressWarnings("deprecation")
public class JavaFXProgressAdapter implements ProgressListener
{
    private final int min;
    private final int max;
    private final ProgressBar progressBar;

    public JavaFXProgressAdapter(ProgressBar progressBar)
    {
        this(progressBar, 0, 100);
    }

    public JavaFXProgressAdapter(ProgressBar progressBar, int min, int max)
    {
        this.min = min;
        this.max = max;
        this.progressBar = progressBar;

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                JavaFXProgressAdapter.this.progressBar.setProgress(0.0);
            }
        });
    }

    @Override
    public void onProgressUpdate(int current)
    {
        onProgressUpdate(current, 0);
    }

    @Override
    public void onProgressUpdate(int current, int total)
    {
        int actualCurrent = Math.max(min, current);
        int actualMax = (total > 0 ? total : this.max);
        int range = actualMax - min;

        final double progress = (range > 0) ? (double) (actualCurrent - min) / range : 0.0;

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                progressBar.setProgress(progress);
            }
        });
    }
}