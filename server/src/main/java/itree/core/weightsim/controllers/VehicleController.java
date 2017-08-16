package itree.core.weightsim.controllers;

import itree.core.weightsim.jpa.dao.VehicleDao;
import itree.core.weightsim.jpa.dao.VehicleTypeDao;
import itree.core.weightsim.jpa.entity.Vehicle;
import itree.core.weightsim.jpa.entity.VehicleType;
import itree.core.weightsim.util.LoggerWrapper;
import itree.core.weightsim.util.LoggerWrapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class VehicleController
{
    private LoggerWrapper logger = LoggerWrapperFactory.getLogger(this.getClass());
    private final VehicleDao vehicleDao;
    private final VehicleTypeDao vehicleTypeDao;

    @Autowired
    public VehicleController(VehicleDao vehicleDao, VehicleTypeDao vehicleTypeDao)
    {
        this.vehicleDao = vehicleDao;
        this.vehicleTypeDao = vehicleTypeDao;
    }

    @RequestMapping("/api/vtypes")
    public List<VehicleType> findAll() throws SQLException
    {
        logger.debug("Find all vehicle types");
        return vehicleTypeDao.findAll();
    }

    @RequestMapping("/api/plates/{plate}/vtypes")
    public List<VehicleType> findVehicleTypes(@PathVariable("plate") int plateVersion) throws SQLException
    {
        logger.debug(String.format("Find all vehicle types for plate: %d", plateVersion));
        return vehicleTypeDao.findAllForPlate(plateVersion);
    }

    @RequestMapping("/api/vehicle/{code}")
    public List<Vehicle> findVehicles(@PathVariable("code") String code) throws SQLException
    {
        logger.debug(String.format("Find all vehicle for type code: %s", code));
        return vehicleDao.findForType(code);
    }

    @RequestMapping(value = "/api/vehicle", method = RequestMethod.POST)
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) throws Exception
    {
        logger.debug(String.format("Creating new vehicle for code: %s", vehicle.getVehicleCode()));
        return vehicleDao.create(vehicle);
    }

    @RequestMapping(value = "/api/vehicle", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle vehicle) throws SQLException
    {
        logger.debug(String.format("Updating vehicle %s", vehicle.toString()));
        return vehicleDao.update(vehicle);
    }

    @RequestMapping(value = "/api/vehicle/{vehicleCode}", method = RequestMethod.DELETE)
    public void deleteVehicle(@PathVariable("vehicleCode") String vehicleCode) throws SQLException
    {
        logger.debug(String.format("Delete vehicle %s", vehicleCode));
        vehicleDao.delete(vehicleCode);
    }

}
