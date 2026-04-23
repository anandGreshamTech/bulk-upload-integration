package com.gresham.bulk.upload.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    @Query(
            value = """
                    select acc.accountnumber
                    from ccm.account acc
                    join ccm.accountbalance accbal
                    on acc.id = accbal.accountid
                    where accbal.currentbalance > 100
                    limit 2
                    """,
            nativeQuery = true
    )
    List<String> findAccountsWithBalance();

    
    @Query(value = """
            select authlink from ccm.customer where customertype = :type
            and authlink is not null limit 1
            """,nativeQuery = true)
    String findCustomer(String type);
    
}
