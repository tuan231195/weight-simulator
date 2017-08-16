package itree.core.weightsim.model;

import itree.core.weightsim.util.LoggerWrapper;
import itree.core.weightsim.util.LoggerWrapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@Component
@PropertySources({@PropertySource("classpath:config.properties"), @PropertySource(value = "${overrideConfigFile}", ignoreResourceNotFound = true)})
public class SimConfig
{
    private LoggerWrapper loggerWrapper = LoggerWrapperFactory.getLogger(SimConfig.class);
    private static final String TICK_RATES_KEY = "app.tick-rate";
    private static final String HOST_NAME_KEY = "app.host-name";
    private static final String NUM_RAMP_PACKETS_KEY = "app.num-ramp-packets";
    private static final String NUM_INIT_PACKETS_KEY = "app.num-init-packets";
    private static final String START_PORT_KEY = "app.start-port";
    private static final String NUM_PORT_KEY = "app.num-ports";
    private static final String OVER_WEIGHT_KEY = "app.overweight";
    private static final String IS_INITIATOR_KEY = "app.is-initiator";
    private static final String NUM_THREADS_KEY = "app.num-threads";

    @Value("${"+ TICK_RATES_KEY + "}")
    private int tickRates;

    @Value("${"+ HOST_NAME_KEY + "}")
    private String hostName;

    @Value("${"+ NUM_RAMP_PACKETS_KEY + "}")
    private int numRamPackets;

    @Value("${"+ NUM_INIT_PACKETS_KEY + "}")
    private int numInitPackets;

    @Value("${"+ START_PORT_KEY + "}")
    private int startPort;

    @Value("#{'${" + OVER_WEIGHT_KEY + "}'.split(',')}")
    private Boolean[] overweight;

    @Value("${"+ NUM_PORT_KEY + "}")
    private int numPorts;

    @Value("${"+ IS_INITIATOR_KEY + "}")
    private boolean isInitiator;

    @Value("${"+ NUM_THREADS_KEY + "}")
    private int numThreads;

    @Value("${overrideConfigFile}")
    private String overrideConfigFile;

    public int getStartPort()
    {
        return startPort;
    }

    public void setStartPort(int startPort)
    {
        this.startPort = startPort;
    }

    public int getTickRates()
    {
        return tickRates;
    }

    public void setTickRates(int tickRates)
    {
        this.tickRates = tickRates;
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public int getNumRamPackets()
    {
        return numRamPackets;
    }

    public void setNumRamPackets(int numRamPackets)
    {
        this.numRamPackets = numRamPackets;
    }

    public int getNumInitPackets()
    {
        return numInitPackets;
    }

    public void setNumInitPackets(int numInitPackets)
    {
        this.numInitPackets = numInitPackets;
    }

    public Boolean[] getOverweight()
    {
        return overweight;
    }

    public void setOverweight(Boolean[] overweight)
    {
        this.overweight = overweight;
    }

    public int getNumPorts()
    {
        return numPorts;
    }

    public void setNumPorts(int numPorts)
    {
        this.numPorts = numPorts;
    }

    public boolean isInitiator()
    {
        return isInitiator;
    }

    public void setInitiator(boolean initiator)
    {
        isInitiator = initiator;
    }

    public int getNumThreads()
    {
        return numThreads;
    }

    public void setNumThreads(int numThreads)
    {
        this.numThreads = numThreads;
    }

    @PreDestroy
    public void save()
    {
        if (overrideConfigFile != null)
        {
            loggerWrapper.debug("Writing properties to file");
            PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(overrideConfigFile);
                Properties properties = new Properties();
                properties.setProperty(NUM_PORT_KEY, String.valueOf(numPorts));
                properties.setProperty(NUM_THREADS_KEY, String.valueOf(numThreads));
                properties.setProperty(NUM_INIT_PACKETS_KEY, String.valueOf(numInitPackets));
                properties.setProperty(NUM_RAMP_PACKETS_KEY, String.valueOf(numRamPackets));
                properties.setProperty(TICK_RATES_KEY, String.valueOf(tickRates));
                properties.setProperty(START_PORT_KEY, String.valueOf(startPort));
                properties.setProperty(IS_INITIATOR_KEY, String.valueOf(isInitiator));
                properties.setProperty(OVER_WEIGHT_KEY, StringUtils.arrayToCommaDelimitedString(overweight));
                properties.setProperty(HOST_NAME_KEY, hostName);
                propertiesPersister.store(properties, fileWriter, "");
            }
            catch (Exception e)
            {
                loggerWrapper.error("Failed to persist config properties", e);
            }
            finally{
                if (fileWriter == null) {
                    try
                    {
                        fileWriter.close();
                    }
                    catch (IOException e)
                    {
                        loggerWrapper.error("Failed to close properties file", e);
                    }
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "SimConfig{" +
                "loggerWrapper=" + loggerWrapper +
                ", tickRates=" + tickRates +
                ", hostName='" + hostName + '\'' +
                ", numRamPackets=" + numRamPackets +
                ", numInitPackets=" + numInitPackets +
                ", startPort=" + startPort +
                ", overweight=" + Arrays.toString(overweight) +
                ", numPorts=" + numPorts +
                ", isInitiator=" + isInitiator +
                ", numThreads=" + numThreads +
                ", overrideConfigFile='" + overrideConfigFile + '\'' +
                '}';
    }
}