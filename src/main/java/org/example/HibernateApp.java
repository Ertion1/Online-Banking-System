package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateApp {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory()
        ){
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            User user = new User(8, "emer", "pass");

            session.save(user);

            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}