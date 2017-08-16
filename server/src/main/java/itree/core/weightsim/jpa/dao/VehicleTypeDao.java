package itree.core.weightsim.jpa.dao;

import itree.core.weightsim.jpa.entity.VehicleType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VehicleTypeDao extends BaseDao
{
    public List<VehicleType> findAll() throws SQLException
    {
        List<VehicleType> result = null;
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try
        {
            transaction.begin();
            result = entityManager.createQuery("FROM VehicleType ve ORDER BY ve.code", VehicleType.class).getResultList();
            transaction.commit();
        }
        catch (RuntimeException e)
        {
            handleException(e, transaction);
        }
        finally
        {
            close(entityManager);
        }
        return result;
    }

    public VehicleType findOne(String typeCode) throws SQLException
    {
        VehicleType result = null;
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try
        {
            transaction.begin();
            List<VehicleType> vehicleTypes = entityManager.createQuery("FROM VehicleType  WHERE code = :code", VehicleType.class)
                    .setParameter("code", typeCode).getResultList();
            if (vehicleTypes != null && vehicleTypes.isEmpty())
            {
                result = vehicleTypes.get(0);
            }
            transaction.commit();
        }
        catch (RuntimeException e)
        {
            handleException(e, transaction);
        }
        finally
        {
            close(entityManager);
        }
        return result;
    }

    public List<VehicleType> findAllForPlate(int plateNum) throws SQLException
    {
        List<VehicleType> result = null;
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try
        {
            transaction.begin();
            result = entityManager.createQuery("SELECT distinct ve FROM VehicleType ve JOIN ve.weightInstructions vw WHERE vw.plateNum = :plateNum ORDER BY ve.code", VehicleType.class).setParameter("plateNum", plateNum).getResultList();
            transaction.commit();
        }
        catch (RuntimeException e)
        {
            handleException(e, transaction);
        }
        finally
        {
            close(entityManager);
        }
        return result;
    }
}
