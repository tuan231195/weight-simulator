package itree.core.weightsim.controllers;

import itree.core.weightsim.jpa.dao.VehicleTypeDao;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.jpa.entity.VehicleType;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.model.SimState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeightService
{
    private SimpMessagingTemplate template;
    private VehicleTypeDao vehicleTypeDao;
    private SimConfig simConfig;

    @Autowired
    public WeightService(SimpMessagingTemplate simpMessagingTemplate, VehicleTypeDao vehicleTypeDao, SimConfig simConfig)
    {
        this.template = simpMessagingTemplate;
        this.vehicleTypeDao = vehicleTypeDao;
        this.simConfig = simConfig;
    }

    @MessageMapping("/state")
    public void simulate()
    {

    }

    @RequestMapping("/api/plates")
    public List<PlateConfig> findAllPlates()
    {
        List<PlateConfig> plateConfigs = new ArrayList<PlateConfig>();

        PlateConfig marulanPlate = new PlateConfig("Marulan Plate", simConfig.getStartPort(), 4, 1);
        plateConfigs.add(marulanPlate);

        PlateConfig mickPlate = new PlateConfig("Mick Plate", simConfig.getStartPort(), 1, 2);
        plateConfigs.add(mickPlate);

        PlateConfig rangerPlate = new PlateConfig("Ranger Plate",simConfig.getStartPort(), 5, 3);
        plateConfigs.add(rangerPlate);

        PlateConfig fourGedgePlate = new PlateConfig("Four Gedge Plate", simConfig.getStartPort(), 4, 4);
        plateConfigs.add(fourGedgePlate);

        return plateConfigs;
    }

    @RequestMapping("/api/plates/{plate}/vtypes")
    public List<VehicleType> findVehicleTypes(@PathVariable("plate") int plateVersion) throws SQLException
    {
        return vehicleTypeDao.findAllForPlate(plateVersion);
    }
}
