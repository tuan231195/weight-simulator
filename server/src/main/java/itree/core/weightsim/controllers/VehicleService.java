package itree.core.weightsim.controllers;

import itree.core.weightsim.jpa.dao.VehicleTypeDao;
import itree.core.weightsim.jpa.entity.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController("/api")
public class VehicleService
{
    private VehicleTypeDao vehicleTypeDao;

    @Autowired
    public VehicleService(VehicleTypeDao vehicleTypeDao)
    {
        this.vehicleTypeDao = vehicleTypeDao;
    }

    @RequestMapping("/vrtypes")
    public List<VehicleType> findAllVehicleTypes() throws SQLException
    {
        return vehicleTypeDao.findAll();
    }
}
