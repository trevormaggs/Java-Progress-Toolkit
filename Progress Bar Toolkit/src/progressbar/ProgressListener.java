package progressbar;

/**
 * Interface for receiving progress updates from long-running tasks.
 */
public interface ProgressListener
{
    public void onProgressUpdate(int current);
    public void onProgressUpdate(int current, int total);
}