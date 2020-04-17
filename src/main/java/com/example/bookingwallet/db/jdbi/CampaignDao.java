package com.example.bookingwallet.db.jdbi;

import com.example.bookingwallet.core.Campaign;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface CampaignDao {
    @SqlUpdate("insert into Campaign (name, balance, budget, walletId, lastTransactionId) values (:name, :balance, :budget, :walletId, :lastTransactionId)")
    @GetGeneratedKeys
    long insert(@BindBean Campaign campaign);

    @SqlQuery("select * from Campaign where id = ?")
    @RegisterBeanMapper(Campaign.class)
    Campaign getById(long id);
}
