package itree.core.weightsim.controllers;

import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.util.LoggerWrapper;
import itree.core.weightsim.util.LoggerWrapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController
{
    private final SimConfig simConfig;
    private LoggerWrapper loggerWrapper = LoggerWrapperFactory.getLogger(SettingsController.class);

    @Autowired
    public SettingsController(SimConfig simConfig)
    {
        this.simConfig = simConfig;
    }

    @RequestMapping(value = "/api/settings", method = RequestMethod.GET)
    public SimConfig getSimConfig()
    {
        loggerWrapper.debug("Getting simulator settings");
        return simConfig;
    }

    @RequestMapping(value = "/api/settings", method = RequestMethod.POST)
    public void saveSimConfig(@RequestBody SimConfig simConfig)
    {
        loggerWrapper.debug("Saving simulator settings: " + simConfig);
        this.simConfig.setHostName(simConfig.getHostName());
        this.simConfig.setNumInitPackets(simConfig.getNumInitPackets());
        this.simConfig.setNumThreads(simConfig.getNumThreads());
        this.simConfig.setNumRamPackets(simConfig.getNumRamPackets());
        this.simConfig.setOverweight(simConfig.getOverweight());
        this.simConfig.setTickRates(simConfig.getTickRates());
        this.simConfig.setStartPort(simConfig.getStartPort());
    }
}
