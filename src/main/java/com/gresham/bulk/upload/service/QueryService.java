package com.gresham.bulk.upload.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<String> findAccountNumberGreaterThan() {

        String sql = """
                select acc.accountnumber
                    from ccm.account acc
                    join ccm.accountbalance accbal
                    on acc.id = accbal.accountid
                    where accbal.currentbalance > 100
                    limit 2
                """;

        return entityManager
                .createNativeQuery(sql)
                .getResultList();


    }

    @Transactional
    public List<String> findAccountNumberWithBalanceForCustomer(String authLink) {
        var sql = """
                SELECT DISTINCT ACC.ACCOUNTNUMBER
                FROM
                CCM.CUSTOMER CUST JOIN
                CCM.ACCOUNT ACC ON CUST.ID = ACC.CUSTOMERID
                JOIN CCM.ACCOUNTBALANCE ACCBAL ON ACC.ID=ACCBAL.ACCOUNTID
                WHERE ACCBAL.CURRENTBALANCE >100
                AND ACCBAL.DATE =(SELECT MAX(DATE) FROM CCM.ACCOUNTBALANCE)
                AND CUST.AUTHLINK =:authLink
                AND NOT EXISTS (
                    SELECT 1
                    FROM CCM.PRODUCT PRD
                    WHERE PRD.VIRTUALINTERESTACCOUNT = ACC.ID
                )
                LIMIT 2;
                """;
        return entityManager.createNativeQuery(sql)
                .setParameter("authLink", authLink).getResultList();
    }

    @Transactional
    public List<String> findAccountNumberForClosure(String authLink) {
        var sql = """
                SELECT distinct acc.accountnumber\s
                        FROM CCM.CUSTOMER CUST
                        JOIN CCM.ACCOUNT ACC ON CUST.ID = ACC.CUSTOMERID
                        WHERE\s
                        CUST.authlink = :authLink
                        and acc.status =0
                        limit 1
                        
                """;
        return entityManager.createNativeQuery(sql)
                .setParameter("authLink", authLink).getResultList();
    }

    
    @Transactional
    public List<String> findCustomersOfType(String type) {

        var virtualAccounts = """
                SELECT
                    CUST.AUTHLINK
                FROM CCM.CUSTOMER CUST
                JOIN CCM.POOL P\s
                    ON P.CUSTOMERID = CUST.ID
                JOIN CCM.PRODUCTCUSTOMER PC
                    ON CUST.ID = PC.CUSTOMERID
                JOIN CCM.PRODUCT PRD
                    ON PRD.ID = PC.PRODUCTID
                JOIN CCM.ACCOUNT ACC
                    ON ACC.CUSTOMERID = CUST.ID
                WHERE CUST.CUSTOMERTYPE = :type
                  AND PRD.VIRTUALINTERESTACCOUNT IS NOT NULL
                  AND PRD.DEBITRATEID != ''
                  AND PRD.CLIENTRATEID != ''
                GROUP BY CUST.AUTHLINK
                HAVING COUNT(ACC.ID) >= 2;
                """;

        var clientMonies = """
                SELECT CUST.AUTHLINK
                                FROM CCM.CUSTOMER CUST
                                JOIN CCM.POOL P
                                    ON P.CUSTOMERID = CUST.ID
                                JOIN CCM.PRODUCTCUSTOMER PC
                                    ON CUST.ID = PC.CUSTOMERID
                                JOIN CCM.PRODUCT PRD
                                    ON PRD.ID = PC.PRODUCTID
                                JOIN CCM.ACCOUNT ACC
                                    ON ACC.CUSTOMERID = CUST.ID
                                WHERE\s
                                CUST.CUSTOMERTYPE = :type
                                  AND PRD.customerrateid != ''
                                  AND PRD.CLIENTRATEID != ''
                                GROUP BY CUST.AUTHLINK
                                HAVING COUNT(ACC.ID) >= 100;
                
                """;
        var sql = type.equalsIgnoreCase("VIRTUAL_ACCOUNTS") ? virtualAccounts : clientMonies;
        return entityManager.createNativeQuery(sql)
                .setParameter("type", type).getResultList();

    }
}

