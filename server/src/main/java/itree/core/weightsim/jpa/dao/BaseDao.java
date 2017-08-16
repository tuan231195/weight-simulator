package itree.core.weightsim.jpa.dao;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;

public class BaseDao
{
    private static final String PROVIDER_NAME = "weightSim";
    private static EntityManagerFactory factory = null;

    private static synchronized void init()
    {
        if (factory == null)
        {
            factory = Persistence.createEntityManagerFactory(PROVIDER_NAME);
        }
    }

    protected EntityManager getEntityManager()
    {
        init();
        return factory.createEntityManager();
    }

    protected void close(EntityManager entityManager)
    {
        if (entityManager != null)
        {
            entityManager.close();
        }
    }

    protected void rethrow(RuntimeException e) throws SQLException
    {
        String msg = e.getMessage();
        Throwable cur = e;
        while (cur != null && !(cur instanceof SQLException))
        {
            cur = cur.getCause();
        }
        if (cur != null)
        {
            msg = cur.getMessage();
        }
        throw new SQLException(msg, e);
    }

    /**
     * Tries to rollback. If it gets a rollback exception, then the original issue is reported.
     * Any other runtime exceptions are wrapper in an SQLException
     *
     * @param exception   the runtime exception
     * @param transaction cannot be null
     * @throws SQLException
     */
    protected void handleException(RuntimeException exception, EntityTransaction transaction) throws SQLException
    {
        try
        {
            transaction.rollback();
        }
        catch (RollbackException e)
        {
            throw new SQLException("Rollback exception due to: " + exception.getMessage(), exception);
        }
        catch (IllegalStateException e)
        {
            throw new SQLException("Illegal State exception due to: " + exception.getMessage(), exception);
        }
        catch (RuntimeException e)
        {
            throw new SQLException(e);
        }
        throw new SQLException(exception);
    }

    /**
     * Perform a safe conversion from the column data type to int
     *
     * @param obj: the value to be converted
     * @return the int value
     */
    protected Integer toInt(Object obj) throws SQLException
    {
        Integer result = null;
        if (obj instanceof Integer)
        {
            result = (Integer) obj;
        }
        else if (obj instanceof Long)
        {
            result = ((Long) obj).intValue();
        }
        else if (obj instanceof BigDecimal)
        {
            result = ((BigDecimal) obj).intValue();
        }
        else if (obj instanceof BigInteger)
        {
            result = ((BigInteger) obj).intValue();
        }
        else if (obj != null)
        {
            throw new SQLException("Unknown return type " + obj.getClass());
        }
        return result;
    }

    protected Long toLong(Object obj) throws SQLException
    {
        Long result = null;
        if (obj instanceof Integer)
        {
            result = ((Integer) obj).longValue();
        }
        else if (obj instanceof Long)
        {
            result = (Long) obj;
        }
        else if (obj instanceof BigDecimal)
        {
            result = ((BigDecimal) obj).longValue();
        }
        else if (obj instanceof BigInteger)
        {
            result = ((BigInteger) obj).longValue();
        }
        else if (obj != null)
        {
            throw new SQLException("Unknown return type " + obj.getClass());
        }
        return result;
    }
}
