package itree.core.weightsim.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({@PropertySource("classpath:config.properties"), @PropertySource(value = "${override-config-file}", ignoreResourceNotFound = true)})
public class SimConfig
{
    @Value("${app.tick-rate}")
    private int tickRates;
    @Value("${app.host-name}")
    private String hostName;
    @Value("${app.ramp-packets}")
    private int rampPackets;
    @Value("${app.num-init-packets}")
    private int numInitPackets;
    @Value("${app.start-port}")
    private int startPort;
    @Value("#{'${app.overweight}'.split(',')}")
    private boolean[] overweight;
    @Value("${app.num-ports}")
    private int numPorts;

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

    public boolean[] getOverweight()
    {
        return overweight;
    }

    public void setOverweight(boolean[] overweight)
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
}