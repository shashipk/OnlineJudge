package com.infy.eta.utils;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by Amit Joshi on 10/7/2015.
 */
public abstract class DoInTransaction<T> {

	protected static final Logger logger = Logger.getLogger(DoInTransaction.class);

	protected final Session session;

	public DoInTransaction() {
		this.session = HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public final T execute() {
		Transaction transaction = session.beginTransaction();

		T result;

		try {
			result = doWork();
			transaction.commit();
		} catch (RuntimeException e) {
			logger.fatal("Rolling back transaction due to exception: " + e.getMessage(), e);
			try {
				transaction.rollback();
			} catch (RuntimeException nested) {
				logger.fatal("Error while rolling back" + nested.getMessage(), nested);
			}
			throw e;
		}
		return result;
	}

	protected abstract T doWork();
}
