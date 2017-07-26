package itree.core.weightsim.jpa.dao;


import itree.core.weightsim.jpa.entity.VehicleType;
import itree.core.weightsim.jpa.entity.WeightConfig;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeightConfigDao extends BaseDao
{
    public List<WeightConfig> findAll(int plateNum, String code) throws SQLException
    {
        List<WeightConfig> weightConfigList = null;
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try
        {
            transaction.begin();
            List result = entityManager.createQuery("SELECT " +
                    "v.code, v.typeName, " +
                    "v.gross, vw.plateNum, vw.step, v.axleGroupMass1 + 2 * v.grpMassTol1, " +
                    "v.axleGroupMass2 + 2 * v.grpMassTol2, v.axleGroupMass3 + 2 * v.grpMassTol3, " +
                    "v.axleGroupMass4 + 2 * v.grpMassTol4, v.axleGroupMass5 + 2 * v.grpMassTol5," +
                    "vw.scale1Active, vw.scale2Active, vw.scale3Active, vw.scale4Active, vw.scale5Active," +
                    "vw.scaleJoin12, vw.scaleJoin23, vw.scaleJoin34, vw.scaleJoin45 " +
                    "FROM VehicleType v JOIN v.weightInstructions vw " +
                    "WHERE v.code = :code AND vw.plateNum = :plateNum")
                    .setParameter("code", code).setParameter("plateNum", plateNum)
                    .getResultList();
            weightConfigList = toEntityList(result);
        }
        catch (RuntimeException e)
        {
            handleException(e, transaction);
        }
        finally
        {
            close(entityManager);
        }
        return weightConfigList;
    }

    private List<WeightConfig> toEntityList(List result) throws SQLException
    {
        List<WeightConfig> weightConfigList = new ArrayList<WeightConfig>();
        for (Object obj : result)
        {
            if (obj instanceof Object[])
            {
                Object[] tuple = (Object[]) obj;
                WeightConfig weightConfig =
                        new WeightConfig(
                        (String) tuple[0], (String) tuple[1], (Double) tuple[2],
                        toInt(tuple[3]), toInt(tuple[4]), toLong(tuple[5]),
                        toLong(tuple[6]), toLong(tuple[7]), toLong(tuple[8]),
                        toLong(tuple[9]), (Character) tuple[10], (Character) tuple[11],
                        (Character) tuple[12], (Character) tuple[13], (Character) tuple[14],
                        (Character) tuple[15], (Character) tuple[16], (Character) tuple[17],
                        (Character) tuple[18]);
                weightConfigList.add(weightConfig);
            }
        }
        return weightConfigList;
    }
}
