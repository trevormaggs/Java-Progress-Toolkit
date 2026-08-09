package progressbar;

/**
 * Defines a listener that receives progress updates from long-running operations.
 * 
 * @author Trevor Maggs
 * @version 0.4
 * @since 15 April 2026
 */
public interface ProgressListener2
{
    /**
     * Notifies the listener that progress has advanced using the listener's configured total
     * workload, if applicable.
     *
     * @param current
     *        the current progress position
     */
    void onProgressUpdate(int current);

    /**
     * Notifies the listener that progress has advanced using the specified total workload.
     *
     * @param current
     *        the current progress position
     * @param total
     *        the total target workload value
     */
    void onProgressUpdate(int current, int total);

    /**
     * Resets any internal state maintained by the listener, preparing it for reuse.
     */
    default void reset()
    {
        // Default no-op for stateless listeners.
    }
}