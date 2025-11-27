package edu.ntnu.idi.idatt.util;

import java.io.InputStream;
import java.util.logging.LogManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for Hibernate SessionFactory management.
 */
public final class HibernateUtil {

  private static final SessionFactory SESSION_FACTORY;

  static {
    // Suppress Hibernate logging before initialization
    silenceLogging();
    
    try {
      SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
    } catch (Exception e) {
      System.err.println("SessionFactory creation failed: " + e.getMessage());
      throw new ExceptionInInitializerError(e);
    }
  }

  private HibernateUtil() {
    // Prevent instantiation
  }

  /**
   * Configures java.util.logging to suppress Hibernate output.
   */
  private static void silenceLogging() {
    try {
      InputStream stream = HibernateUtil.class.getClassLoader()
          .getResourceAsStream("logging.properties");
      if (stream != null) {
        LogManager.getLogManager().readConfiguration(stream);
        stream.close();
      }
    } catch (Exception e) {
      // Ignore logging config errors
    }
  }

  /**
   * Returns the singleton SessionFactory instance.
   *
   * @return the SessionFactory
   */
  public static SessionFactory getSessionFactory() {
    return SESSION_FACTORY;
  }

  /**
   * Closes the SessionFactory and releases all resources.
   */
  public static void shutdown() {
    if (SESSION_FACTORY != null) {
      SESSION_FACTORY.close();
    }
  }
}
