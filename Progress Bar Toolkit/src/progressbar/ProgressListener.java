package progressbar;

/**
 * Defines a listener that receives progress updates from long-running operations.
 *
 * @author Trevor Maggs
 * @version 0.5
 * @since 9 August 2026
 */
public interface ProgressListener
{
    /**
     * Notifies the listener that progress has advanced using the specified total workload.
     *
     * <p>
     * When {@code current} reaches {@code total}, the operation is considered complete and
     * {@link #onCompleted(int)} is invoked.
     * </p>
     *
     * @param current
     *        the current progress position
     * @param total
     *        the total target workload value
     */
    void onProgressUpdate(int current, int total);

    /**
     * Notifies the listener that progress has advanced using a default total workload.
     *
     * @param current
     *        the current progress position
     */
    default void onProgressUpdate(int current)
    {
        onProgressUpdate(current, 0);
    }

    /**
     * Notifies the listener that the operation has completed.
     *
     * @param total
     *        the total workload completed
     */
    default void onCompleted(int total)
    {
        // Default no-op for listeners that do not need completion notification.
    }

    /**
     * Resets any internal state maintained by the listener, preparing it for reuse.
     */
    default void reset()
    {
        // Default no-op for state-less listeners.
    }
}