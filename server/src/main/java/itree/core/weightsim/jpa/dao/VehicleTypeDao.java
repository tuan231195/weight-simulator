package itree.core.weightsim.jpa.dao;

import itree.core.weightsim.jpa.entity.VehicleType;
import itree.core.weightsim.jpa.entity.VehicleType_;
import itree.core.weightsim.jpa.entity.WeightInstruction;
import itree.core.weightsim.jpa.entity.WeightInstruction_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
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
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<VehicleType> query = cb.createQuery(VehicleType.class);
            Root<VehicleType> root = query.from(VehicleType.class);
            Join<VehicleType, WeightInstruction> join = root.join(VehicleType_.weightInstructions);
            query.where(cb.greaterThan(join.get(WeightInstruction_.plateNum), 0));
            query.orderBy(cb.asc(root.get(VehicleType_.catCode)),
                    cb.asc(root.get(VehicleType_.code)),
                    cb.asc(join.get(WeightInstruction_.plateNum)),
                    cb.asc(join.get(WeightInstruction_.step)));
            root.fetch(VehicleType_.weightInstructions);
            query.select(root);
            result =  entityManager.createQuery(query).getResultList();
            transaction.commit();
            return result;
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
            List<VehicleType> vehicleTypes =  entityManager.createQuery("FROM VehicleType  WHERE code = :code", VehicleType.class)
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

}
