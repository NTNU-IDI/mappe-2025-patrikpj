package edu.ntnu.idi.idatt.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for managing the Hibernate SessionFactory.
 * Provides a singleton SessionFactory instance for the application.
 */
public class HibernateUtil {

  private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    try {
      // Create SessionFactory from hibernate.cfg.xml
      return new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      System.err.println("Initial SessionFactory creation failed: " + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  /**
   * Returns the singleton SessionFactory instance.
   *
   * @return the SessionFactory
   */
  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  /**
   * Closes the SessionFactory and releases all resources.
   */
  public static void shutdown() {
    getSessionFactory().close();
  }
}

