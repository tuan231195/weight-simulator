package itree.core.weightsim.jpa.dao;

import itree.core.weightsim.jpa.entity.VehicleType;
import itree.core.weightsim.jpa.entity.WeightConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class WeightConfigDaoTest
{
    private WeightConfigDao weightConfigDao;
    @Before
    public void setup()
    {
        weightConfigDao = new WeightConfigDao();
    }

    @Test
    public void testFindAll() throws SQLException
    {
        List<WeightConfig> weightConfigList = weightConfigDao.findAll(1, "06AA");
        System.out.println(weightConfigList);
    }
}
