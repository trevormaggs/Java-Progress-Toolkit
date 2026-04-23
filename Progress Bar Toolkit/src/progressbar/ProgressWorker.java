package progressbar;

import javax.swing.SwingWorker;
import java.util.List;

/**
 * A generic background worker that executes a {@link WorkTask} and notifies registered
 * {@link ProgressListener}s on the Event Dispatch Thread (EDT).
 * 
 * <p>
 * This class leverages {@link SwingWorker} to handle the complexity of thread synchronisation,
 * ensuring that heavy background processing does not freeze the user interface while progress
 * updates are safely delivered to GUI components.
 * </p>
 * 
 * @author Trevor Maggs
 * @version 0.4
 * @since 21 April 2026
 */
public class ProgressWorker extends SwingWorker<Void, ProgressWorker.ProgressUpdate>
{
    private final TaskDespatcher task;
    private final List<ProgressListener> guiListeners;

    /**
     * A simple data carrier for progress state.
     */
    public static class ProgressUpdate
    {
        public final int current;
        public final int total;

        /**
         * Constructs a new progress update.
         * 
         * @param c
         *        the current value
         * @param t
         *        the total value
         */
        public ProgressUpdate(int c, int t)
        {
            this.current = c;
            this.total = t;
        }
    }

    /**
     * Constructs a ProgressWorker with a task and a list of listeners.
     * 
     * @param task
     *        the work to be performed
     * @param guiListeners
     *        listeners (usually UI adapters) that require updates on the EDT
     */
    public ProgressWorker(TaskDespatcher task, List<ProgressListener> guiListeners)
    {
        this.task = task;
        this.guiListeners = guiListeners;
    }

    /**
     * Executes the task in a background thread.
     * 
     * @return null
     * 
     * @throws Exception
     *         if the underlying task throws an exception
     */
    @Override
    protected Void doInBackground() throws Exception
    {
        task.despatch(new ProgressListener()
        {
            @Override
            public void onProgressUpdate(int current)
            {
                onProgressUpdate(current, 0);
            }

            @Override
            public void onProgressUpdate(int current, int total)
            {
                publish(new ProgressUpdate(current, total));
            }
        });

        return null;
    }

    /**
     * Receives progress updates published from the background thread and dispatches them to the
     * listeners on the Event Dispatch Thread.
     * 
     * <p>
     * To optimise performance, only the latest update in a batch of chunks is processed,
     * effectively coalescing rapid updates.
     * </p>
     * 
     * @param chunks
     *        the list of progress updates to process
     */
    @Override
    protected void process(List<ProgressUpdate> chunks)
    {
        if (chunks.isEmpty())
        {
            return;
        }

        ProgressUpdate latest = chunks.get(chunks.size() - 1);

        for (ProgressListener listener : guiListeners)
        {
            listener.onProgressUpdate(latest.current, latest.total);
        }
    }
}