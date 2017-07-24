package itree.core.weightsim.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config.properties")
public class SimConfig
{
    @Value("${app.tick-rate}")
    private long tickRates;
    @Value("${app.steady-time}")
    private long steadyTime;
    @Value("${app.host-name}")
    private String hostName;
    @Value("${app.ramp-packets}")
    private int rampPackets;
    @Value("${app.num-init-packets}")
    private int numInitPackets;
    @Value("${app.start-port}")
    private int startPort;
    @Value("${app.num-threads}")
    private int numThreads;
    public int getStartPort()
    {
        return startPort;
    }

    public void setStartPort(int startPort)
    {
        this.startPort = startPort;
    }


    public long getTickRates()
    {
        return tickRates;
    }

    public void setTickRates(long tickRates)
    {
        this.tickRates = tickRates;
    }

    public long getSteadyTime()
    {
        return steadyTime;
    }

    public void setSteadyTime(long steadyTime)
    {
        this.steadyTime = steadyTime;
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public int getRampPackets()
    {
        return rampPackets;
    }

    public void setRampPackets(int rampPackets)
    {
        this.rampPackets = rampPackets;
    }

    public int getNumInitPackets()
    {
        return numInitPackets;
    }

    public void setNumInitPackets(int numInitPackets)
    {
        this.numInitPackets = numInitPackets;
    }

    public int getNumThreads()
    {
        return numThreads;
    }

    public void setNumThreads(int numThreads)
    {
        this.numThreads = numThreads;
    }
}