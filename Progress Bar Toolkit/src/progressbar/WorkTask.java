package progressbar;

/**
 * Defines a functional contract for a task that can be executed by a {@link ProgressWorker} or
 * directly via a {@link ConsoleProgressBar}.
 * 
 * <p>
 * This interface allows for the separation of business logic from the specific implementation of
 * progress visualisation.
 * </p>
 * 
 * @author Trevor Maggs
 * @version 0.5
 * @since 15 April 2026
 */
public interface WorkTask
{
    /**
     * Executes the task logic.
     * 
     * @param listener
     *        a listener used to bridge progress updates from the task to the underlying progress
     *        implementation, such as console or GUI
     * 
     * @throws Exception
     *         if the task execution fails
     */
    void execute(ProgressListener listener) throws Exception;
}