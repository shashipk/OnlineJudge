package com.infy.eta.utils;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Amit Joshi on 10/3/2015.
 */
public class HibernateUtil {

	private static final Logger logger = Logger.getLogger(HibernateUtil.class);

	private static SessionFactory sessionFactory;
	private static Configuration  configuration;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = getConfiguration().buildSessionFactory();
		}
		return sessionFactory;
	}

	public static Configuration getConfiguration() {
		if (configuration == null) {
			logger.info("Configuration was null. Setting new configuration. ");
			setConfiguration(new Configuration().configure());
		}
		return configuration;
	}

	public static void setConfiguration(Configuration config) {
		configuration = config;
	}

}
