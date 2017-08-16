package itree.core.weightsim.jpa.dao;


import itree.core.weightsim.jpa.entity.Vehicle;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VehicleDao extends BaseDao
{
    public Vehicle create(Vehicle vehicle) throws Exception
    {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        long count;
        try
        {
            transaction.begin();
            count = entityManager.createQuery("SELECT COUNT(*) FROM Vehicle ve WHERE vehicleTypeCode = :vehicleTypeCode", Long.class).setParameter("vehicleTypeCode", vehicle.getVehicleTypeCode()).getSingleResult();
            vehicle.setVehicleCode(vehicle.getVehicleTypeCode() + "-" + count);
            entityManager.persist(vehicle);
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
        return vehicle;
    }

    public List<Vehicle> findForType(String typeCode) throws SQLException
    {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Vehicle> result = null;
        try
        {
            transaction.begin();
            result = entityManager.createQuery("FROM Vehicle ve WHERE vehicleTypeCode = :vehicleTypeCode", Vehicle.class).setParameter("vehicleTypeCode", typeCode).getResultList();
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

    public boolean delete(String vehicleCode) throws SQLException
    {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        boolean deleted = false;
        try
        {
            transaction.begin();
            deleted = entityManager.createQuery("DELETE FROM Vehicle ve WHERE vehicleCode = :vehicleCode").setParameter("vehicleCode", vehicleCode).executeUpdate() > 0;
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
        return deleted;
    }

    public Vehicle update(Vehicle vehicle) throws SQLException
    {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Vehicle updatedVehicle = null;
        try
        {
            transaction.begin();
            updatedVehicle = entityManager.merge(vehicle);
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
        return updatedVehicle;
    }
}
