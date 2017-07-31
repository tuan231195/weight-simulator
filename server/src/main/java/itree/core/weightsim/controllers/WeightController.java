package itree.core.weightsim.controllers;

import itree.core.weightsim.jpa.dao.VehicleTypeDao;
import itree.core.weightsim.jpa.entity.VehicleType;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.model.SimRequest;
import itree.core.weightsim.model.SimState;
import itree.core.weightsim.service.sim.SimService;
import itree.core.weightsim.util.LoggerWrapper;
import itree.core.weightsim.util.LoggerWrapperFactory;
import itree.core.weightsim.web.PlateNotFoundException;
import itree.core.weightsim.web.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeightController
{
    public static final String RECEIVED_MESSAGE = "Received request: %s";
    private final VehicleTypeDao vehicleTypeDao;
    private final SimConfig simConfig;
    private final SimService simService;
    private List<PlateConfig> plateConfigs;
    private LoggerWrapper logger = LoggerWrapperFactory.getLogger(this.getClass());

    @Autowired
    public WeightController(VehicleTypeDao vehicleTypeDao, SimConfig simConfig, SimService simService)
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


    @RequestMapping(value = "/api/simulate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void simulate(@RequestBody SimRequest simRequest) throws PlateNotFoundException, SQLException
    {
        logger.debug(String.format(RECEIVED_MESSAGE, simRequest));
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
            logger.error(String.format("Cannot find plate version: %d", simRequest.getVersion()));
            throw new PlateNotFoundException(simRequest.getVersion());
        }
        simService.simulate(simRequest.getSessionId(), foundPlateConfig, simRequest.getCode());
    }

    @RequestMapping(value = "/api/stop", method = RequestMethod.POST)
    public void stop(@RequestBody SimRequest simRequest) throws SessionNotFoundException
    {
        logger.debug(String.format(RECEIVED_MESSAGE, simRequest));
        simService.stop(simRequest.getSessionId());
    }

    @RequestMapping("/api/state/{sessionId}")
    public SimState getState(@PathVariable("sessionId") String sessionId) throws SessionNotFoundException
    {

        logger.debug("Getting state for session: " + sessionId);
        return simService.getState(sessionId);
    }

    @RequestMapping(value = "/api/next", method = RequestMethod.POST)
    public void next(@RequestBody SimRequest simRequest) throws SessionNotFoundException
    {
        logger.debug(String.format(RECEIVED_MESSAGE, simRequest));
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
        logger.debug(String.format("Find all vehicle types for plate: %d", plateVersion));
        return vehicleTypeDao.findAllForPlate(plateVersion);
    }
}
