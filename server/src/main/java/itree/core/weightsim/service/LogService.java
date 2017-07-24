package itree.core.weightsim.service;

import itree.core.weightsim.model.SimConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

@Service
public class LogService
{
    private Logger logger = LoggerFactory.getLogger(LogService.class);
    private PrintWriter[] printWriters;
    private boolean logInit;
    private SimConfig simConfig;

    public LogService(SimConfig simConfig)
    {
        //create one print writer for each thread
        this.printWriters = new PrintWriter[simConfig.getNumThreads()];
        this.simConfig = simConfig;
    }

    @PostConstruct
    public void init()
    {
        File logDirectory = new File("logs");
        try
        {
            if (!logDirectory.exists())
            {
                boolean created = logDirectory.mkdir();
                if (created)
                {
                    logger.debug("Created log directory");
                }
            }
            for (int i = 0; i < simConfig.getNumThreads(); i++)
            {
                int port = simConfig.getStartPort();
                printWriters[i] = new PrintWriter(new FileOutputStream(new File(logDirectory, "packets-" + port + ".log"), true));
            }
            logInit = true;
        }
        catch(Exception e)
        {
            logger.debug("Failed to create log files", e);
        }
    }


    public void log(int idx, String msg)
    {
        printWriters[idx].print(msg);
        printWriters[idx].flush();
    }
}
