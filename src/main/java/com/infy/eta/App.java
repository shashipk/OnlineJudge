package com.infy.eta;

import com.infy.eta.databeans.JudgeProblemsEntity;
import com.infy.eta.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Amit Joshi on 10/3/2015.
 */
public class App {
	public static void main(String[] args) {
		/*Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		transaction.begin();
		Criteria criteria = session.createCriteria(JudgeProblemsEntity.class);
		criteria.add(Restrictions.eq("id",1));
		List<JudgeProblemsEntity> list = criteria.list();
		System.out.println(list.size());
		transaction.commit();*/

		/*JSONObject object = new JSONObject("{success: 'true'}");
		System.out.println(object);*/
		String str = "1\\u00002u0000";
		System.out.println(str);
		System.out.println(str.replaceAll("\\u0000",""));
	}
}
