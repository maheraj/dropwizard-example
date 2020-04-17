package com.example.bookingwallet.db.jdbi;

import com.example.bookingwallet.core.TransactionPart;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface TransactionPartDao {
    @SqlUpdate("insert into TransactionPart (transactionId, walletId, direction, amount, currencyCode) values (:transactionId, :walletId, :direction, :amount, :currencyCode)")
    @GetGeneratedKeys
    long insert(@BindBean TransactionPart transactionPart);

    @SqlQuery("SELECT * FROM TransactionPart where transactionId = ? ORDER BY id")
    @RegisterBeanMapper(TransactionPart.class)
    List<TransactionPart> getByTransactionId(long id);

    @SqlQuery("SELECT SUM(COALESCE(CASE WHEN direction = 'CREDIT' THEN amount END, 0)) - SUM(COALESCE(CASE WHEN direction = 'DEBIT' THEN amount END, 0)) balance FROM TransactionPart WHERE walletId = ?")
    double getWalletBalance(long walletId);
}
