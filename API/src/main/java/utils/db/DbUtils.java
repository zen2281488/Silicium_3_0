package utils.db;

import lombok.experimental.UtilityClass;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@UtilityClass
public class DbUtils {

    public void save(Object entity) {
        Transaction tx = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при сохранении записи: " + entity.getClass().getSimpleName(), e);
        }
    }

    public void update(Object entity) {
        Transaction tx = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при обновлении записи: " + entity.getClass().getSimpleName(), e);
        }
    }

    public void delete(Object entity) {
        Transaction tx = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при удалении записи: " + entity.getClass().getSimpleName(), e);
        }
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при получении всех записей для: " + entityClass.getSimpleName(), e);
        }
    }

    public <T> List<T> findAll(Class<T> entityClass, Integer parameter, String parameterName) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM " + entityClass.getSimpleName() + " WHERE " + parameterName + " = :value", entityClass)
                    .setParameter("value", parameter)
                    .list();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при поиске по параметру '" + parameterName +
                    "' для записи: " + entityClass.getSimpleName(), e);
        }
    }

    public <T> T findById(Class<T> entityClass, int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityClass.getSimpleName() + " WHERE id = :id", entityClass)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при поиске по ID (" + id + ") для: " + entityClass.getSimpleName(), e);
        }
    }
}
