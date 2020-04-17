package com.example.bookingwallet.db.jdbi;

import com.example.bookingwallet.core.Wallet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface WalletDao {
    @SqlUpdate("insert into Wallet (currencyCode, walletType, customerId) values (:currencyCode, :walletType, :customerId)")
    @GetGeneratedKeys
    long insert(@BindBean Wallet wallet);

    @SqlQuery("SELECT * FROM Wallet ORDER BY id")
    @RegisterBeanMapper(Wallet.class)
    List<Wallet> listWallets();

    @SqlQuery("select * from Wallet where id = ?")
    @RegisterBeanMapper(Wallet.class)
    Wallet getById(long id);
}
