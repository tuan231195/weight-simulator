package itree.core.weightsim.controllers;


import itree.core.weightsim.model.SimConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController
{
    private final SimConfig simConfig;

    @Autowired
    public SettingsController(SimConfig simConfig)
    {
        this.simConfig = simConfig;
    }

    @RequestMapping("/api/settings")
    public SimConfig getSimConfig()
    {
        return simConfig;
    }
}
