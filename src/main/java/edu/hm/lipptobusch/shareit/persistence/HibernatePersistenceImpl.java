/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */
package edu.hm.lipptobusch.shareit.persistence;

import edu.hm.lipptobusch.shareit.models.Medium;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class HibernatePersistenceImpl implements HibernatePersistence {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public HibernatePersistenceImpl() {

    }

    @Override
    public void addMedium(Medium medium) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(medium);

        tx.commit();
        session.close();
    }

    @Override
    public void updateMedium(Medium medium) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        //... some something
        //session.merge(medium);
        session.update(medium);

        tx.commit();
        session.close();
    }

    @Override
    public List<Medium> getTable(Class className) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        //... some something

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Medium> query = builder.createQuery(className);
        query.from(className);
        List<Medium> resultList = session.createQuery(query).getResultList();


        //session.createQuery("FROM " + className.getSimpleName() + " ");

        tx.commit();
        session.close();

        return resultList;
    }

    @Override
    public Medium findMedium(Class className, Serializable id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        //... some something


        tx.commit();
        session.close();

        return null;
    }


}
