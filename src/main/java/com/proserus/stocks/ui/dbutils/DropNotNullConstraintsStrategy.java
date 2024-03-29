package com.proserus.stocks.ui.dbutils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proserus.stocks.bp.dao.PersistenceManager;

public class DropNotNullConstraintsStrategy implements DatabaseStrategy {
	private static Logger log = LoggerFactory.getLogger(DropNotNullConstraintsStrategy.class.getName());

	private enum CONSTRAINT_TYPES {
		CHECK("CHECK");
		private String name;

		private CONSTRAINT_TYPES(String name) {
			this.name = name;
		}
	}

	@Override
	public void applyUpgrade(PersistenceManager pm) {
		@SuppressWarnings("unchecked")
		List<Object> list = pm.createNativeQuery("select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS").getResultList();

		for (CONSTRAINT_TYPES constraintType : CONSTRAINT_TYPES.values()) {
			for (Object o : list) {
				String type = (String) ((Object[]) o)[3];
				String schema = (String) ((Object[]) o)[1];
				String constraintName = (String) ((Object[]) o)[2];
				if (type.equals(constraintType.name) && schema.equals("PUBLIC") && constraintName.startsWith("NN")) {

					String tableName = (String) ((Object[]) o)[6];
					String command = "alter table PUBLIC." + tableName + " drop constraint " + constraintName;
					pm.createNativeQuery(command).executeUpdate();
				}
			}
		}
		if (log.isDebugEnabled() && pm.getTransaction().getRollbackOnly()) {
			log.debug("Upgrade is rolling back");
		}
	}

}
