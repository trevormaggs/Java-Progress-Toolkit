package progressbar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Collections;

public class TestSampleGui extends JFrame
{
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JButton startButton = new JButton("Start Batch Process");
    private final JTextArea logArea = new JTextArea(10, 30);

    public TestSampleGui()
    {
        setTitle("Media Patcher");
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(startButton);
        panel.add(progressBar);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        // ActionListener using Anonymous Inner Class
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                runTask();
            }
        });

        pack();
    }

    private void runTask()
    {
        int totalFiles = 50;

        startButton.setEnabled(false);
        logArea.append("Starting process...\n");

        SwingProgressAdapter adapter = new SwingProgressAdapter(progressBar, 1, totalFiles);

        WorkTask myTask = new WorkTask()
        {
            @Override
            public void execute(ProgressListener bridgeListener) throws Exception
            {
                for (int i = 1; i <= totalFiles; i++)
                {
                    Thread.sleep(100);
                    bridgeListener.onProgressUpdate(i);
                }
            }
        };

        // The Worker to handle threading
        ProgressWorker worker = new ProgressWorker(myTask, Collections.singletonList((ProgressListener) adapter))
        {
            @Override
            protected void done()
            {
                // This runs on the EDT when the background work is finished
                startButton.setEnabled(true);
                logArea.append("Batch Processing Complete!\n");
            }
        };

        worker.execute();
    }

    public static void main(String[] args)
    {
        // Ensure the GUI starts on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new TestSampleGui().setVisible(true);
            }
        });
    }
}