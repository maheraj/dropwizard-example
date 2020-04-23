package com.example.bookingwallet.db.jdbi;

import com.example.bookingwallet.core.Transaction;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TransactionDao {
    @SqlUpdate("insert into Transaction (date, originalCurrencyCode, originalAmount, parentId, operation, notes) values (:date, :originalCurrencyCode, :originalAmount, :parentId, :operation, :notes)")
    @GetGeneratedKeys
    long insert(@BindBean Transaction transaction);

    @SqlQuery("select * from Transaction where id = ?")
    @RegisterBeanMapper(Transaction.class)
    Transaction getById(long id);
}
