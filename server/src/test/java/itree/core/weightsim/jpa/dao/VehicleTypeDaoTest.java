package itree.core.weightsim.jpa.dao;
import itree.core.weightsim.jpa.entity.VehicleType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;


public class VehicleTypeDaoTest
{
    private VehicleTypeDao vehicleTypeDao;
    @Before
    public void setup()
    {
        vehicleTypeDao = new VehicleTypeDao();
    }

    @Test
    public void testFindAll() throws SQLException
    {
        List<VehicleType> vehicleTypeList = vehicleTypeDao.findAll();

    }

    @Test
    public void testFindOne() throws SQLException
    {
        System.out.println(vehicleTypeDao.findOne("03BA"));
    }


}
