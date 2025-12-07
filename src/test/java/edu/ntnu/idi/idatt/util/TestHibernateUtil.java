package edu.ntnu.idi.idatt.util;

import java.net.URL;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Provides an in-memory SessionFactory for integration tests.
 */
public class TestHibernateUtil {

  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      URL configUrl = TestHibernateUtil.class.getClassLoader()
          .getResource("hibernate-test.cfg.xml");
      if (configUrl == null) {
        throw new RuntimeException(
            "hibernate-test.cfg.xml not found on classpath. "
            + "Ensure src/test/resources is marked as Test Resources Root.");
      }
      sessionFactory = new Configuration()
          .configure(configUrl)
          .buildSessionFactory();
    }
    return sessionFactory;
  }

  public static void shutdown() {
    if (sessionFactory != null) {
      sessionFactory.close();
      sessionFactory = null;
    }
  }
}