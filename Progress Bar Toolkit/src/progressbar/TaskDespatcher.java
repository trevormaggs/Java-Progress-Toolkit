package progressbar;

/**
 * Defines a functional contract for a task that can be despatched by a {@link ProgressWorker} or
 * executed directly.
 *
 * <p>
 * This interface facilitates the separation of business logic from the specific implementation of
 * progress visualisation, allowing the logic to be despatched across different threading or logging
 * environments.
 * </p>
 *
 * @author Trevor Maggs
 * @version 0.5
 * @since 15 April 2026
 */
public interface TaskDespatcher
{
    /**
     * Despatches the task logic and reports progress to the provided listener.
     *
     * @param listener
     *        the listener utilised to bridge progress updates to the underlying implementation
     *
     * @throws Exception
     *         if an error occurs during the despatch of the task
     */
    void despatch(ProgressListener listener) throws Exception;
}