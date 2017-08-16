package itree.core.weightsim.jpa.dao;

import itree.core.weightsim.jpa.entity.Vehicle;
import itree.core.weightsim.jpa.entity.VehicleType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class VehicleDaoTest
{
    private VehicleDao vehicleDao;
    private Vehicle vehicle;

    @Before
    public void setup() throws Exception
    {
        vehicleDao = new VehicleDao();
        vehicle = new Vehicle();
        vehicle.setVehicleTypeCode("10DJ");
        vehicle.setVehicleName("Test Vehicle");
        vehicle = vehicleDao.create(vehicle);
    }

    @Test
    public void testFindForType() throws SQLException
    {
        List<Vehicle> vehicleList = vehicleDao.findForType("10DJ");
        Assert.assertTrue(vehicleList.size() > 0);
    }

    @Test
    public void testUpdate() throws SQLException
    {
        String name = "New updated";
        vehicle.setVehicleName(name);
        Vehicle updatedVehicle = vehicleDao.update(vehicle);
        Assert.assertEquals(updatedVehicle.getVehicleName(), name);
    }

    @After
    public void tearDown() throws SQLException
    {
        vehicleDao.delete(vehicle.getVehicleCode());
    }
}
