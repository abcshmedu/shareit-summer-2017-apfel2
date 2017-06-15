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

import java.io.Serializable;
import java.util.List;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class HibernatePersistenceImpl implements HibernatePersistence {
    private SessionFactory sessionFactory;

    public HibernatePersistenceImpl() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();;
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

        session.createQuery("FROM " + className.getSimpleName() + " ");

        tx.commit();
        session.close();

        return null;
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
