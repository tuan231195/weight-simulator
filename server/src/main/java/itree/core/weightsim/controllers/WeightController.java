package itree.core.weightsim.controllers;

import itree.core.weightsim.jpa.dao.VehicleTypeDao;
import itree.core.weightsim.jpa.entity.VehicleType;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.model.SimRequest;
import itree.core.weightsim.service.sim.SimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeightService
{
    private final VehicleTypeDao vehicleTypeDao;
    private final SimConfig simConfig;
    private final SimService simService;
    private List<PlateConfig> plateConfigs;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public WeightService(VehicleTypeDao vehicleTypeDao, SimConfig simConfig, SimService simService)
    {
        this.vehicleTypeDao = vehicleTypeDao;
        this.simConfig = simConfig;
        this.simService = simService;
    }

    @PostConstruct
    public void init()
    {
        plateConfigs = new ArrayList<PlateConfig>();

        PlateConfig marulanPlate = new PlateConfig("Marulan Plate", simConfig.getStartPort(), 4, 1);
        plateConfigs.add(marulanPlate);

        PlateConfig mickPlate = new PlateConfig("Mick Plate", simConfig.getStartPort(), 1, 2);
        plateConfigs.add(mickPlate);

        PlateConfig rangerPlate = new PlateConfig("Ranger Plate", simConfig.getStartPort(), 5, 3);
        plateConfigs.add(rangerPlate);

        PlateConfig fourGedgePlate = new PlateConfig("Four Gedge Plate", simConfig.getStartPort(), 4, 4);
        plateConfigs.add(fourGedgePlate);

    }


    @RequestMapping("/simulate")
    public void simulate(@RequestBody SimRequest simRequest) throws PlateNotFoundException, SQLException
    {
        logger.debug("Received request: " + simRequest);
        PlateConfig foundPlateConfig = null;
        for (PlateConfig plateConfig : plateConfigs)
        {
            if (plateConfig.getPlateVersion() == simRequest.getVersion())
            {
                foundPlateConfig = plateConfig;
            }
        }
        if (foundPlateConfig == null)
        {
            logger.error("Cannot find plate version: " + simRequest.getVersion());
            throw new PlateNotFoundException(simRequest.getVersion());

        }
        simService.simulate(simRequest.getSessionId(), foundPlateConfig, simRequest.getCode());
    }

    @RequestMapping("/stop")
    public void stop(@RequestBody SimRequest simRequest)
    {
        logger.debug("Received request: " + simRequest);
        simService.stop(simRequest.getSessionId());
    }

    @RequestMapping("/state")
    public void getState(@RequestBody SimRequest simRequest)
    {
        logger.debug("Received request: " + simRequest);
        simService.getState(simRequest.getSessionId());
    }

    @RequestMapping("/next")
    public void next(@RequestBody SimRequest simRequest)
    {
        logger.debug("Received request: " + simRequest);
        simService.nextStep(simRequest.getSessionId());
    }

    @RequestMapping("/api/plates")
    public List<PlateConfig> findAllPlates()
    {
        logger.debug("Find all plate configurations");
        return plateConfigs;
    }

    @RequestMapping("/api/plates/{plate}/vtypes")
    public List<VehicleType> findVehicleTypes(@PathVariable("plate") int plateVersion) throws SQLException
    {
        logger.debug("Find all vehicle types for plate: " + plateVersion);
        return vehicleTypeDao.findAllForPlate(plateVersion);
    }
}