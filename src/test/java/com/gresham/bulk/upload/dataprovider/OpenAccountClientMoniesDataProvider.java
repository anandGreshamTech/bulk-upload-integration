package com.gresham.bulk.upload.dataprovider;


import org.apache.groovy.util.Maps;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OpenAccountClientMoniesDataProvider {

    static Stream<Arguments> fullSet() {
        return Stream.of(
                Arguments.of("openCompanyAccount", "RESPONSE", false, Map.of(3, List.of(4, 5), 4, List.of(4, 5)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"


                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,0",
                                "3,A,SUCCESS,QWNjb3VudDowMjBmZTUwNjA1NjQ1N2ZkNjBkNzU5NDMwZDYzMmI5ODZmMjgyNGNhNzdmYjRlNzcwYjIyMDQ0MDY4NzY0ZGQ5,000010229,0",
                                "4,C,SUCCESS,UGFydHlDb21wYW55OjA5MWZjMDFhZjFhYzc3ZDU0YmUwMDJmYjg5ZjQ1ZTUyMmFjMmI1YWU4ZTQ0YTI3Y2JmOTQxYmM3ODgzODgxM2U=,7666702983,0"
                        )
                ), Arguments.of("openAccountNonIndividualBeneficiaries", "RESPONSE", false, Map.of(3, List.of(4, 5), 4, List.of(4, 5)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,1,1,1,1,1",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},6",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,",
                                "6,S,,BENEFICIARY,${fullName},incorporated,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "7,X,AU,450000002,",
                                "8,P,,BENEFICIARY,${fullName},unregulated,${beneficiaryRef},51824753556,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "9,X,AU,450001373,",
                                "10,O,,BENEFICIARY,${fullName},${beneficiaryRef},51824753556,99824753559,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "11,X,AU,450002748,",
                                "12,G,,BENEFICIARY,${fullName},domestic,${beneficiaryRef},51824753556,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "13,X,AU,450004115,",
                                "14,T,,BENEFICIARY,${fullName},REGULATED_GOVERNMENT_SUPER_FUND,4267358176,2000-10-10,${beneficiaryRef},51824753556,AU,AccountTrust-fullNamegDlh@gmail.com,+612323454544,Other,1 Anfield Road,,North Sydney,NSW,2060,AU,,,,,,,1",
                                "15,X,AU,450006850,"


                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,0",
                                "3,A,SUCCESS,QWNjb3VudDowMjBmZTUwNjA1NjQ1N2ZkNjBkNzU5NDMwZDYzMmI5ODZmMjgyNGNhNzdmYjRlNzcwYjIyMDQ0MDY4NzY0ZGQ5,000010229,0",
                                "4,C,SUCCESS,UGFydHlDb21wYW55OjA5MWZjMDFhZjFhYzc3ZDU0YmUwMDJmYjg5ZjQ1ZTUyMmFjMmI1YWU4ZTQ0YTI3Y2JmOTQxYmM3ODgzODgxM2U=,7666702983,0"
                        )
                ), Arguments.of("headerRecordNumberIsInValidNumber", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "12,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Invalid record number 12"
                        )
                ), Arguments.of("headerRecordNumberIsString", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "two,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Invalid record number two"
                        )
                ), Arguments.of("headerRecordNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                ",H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Invalid record number "
                        )
                ), Arguments.of("headerRecordTypeIsNotH", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,HH,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type"
                        )
                ), Arguments.of("headerRecordNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type"
                        )
                ), Arguments.of("headerRecordCustomerIdIsNotMatchingWithFileAuthLink", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,BULK,1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK,1,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: The Authlink in the file name does not match the Customer Code in the header",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Account unable to be created due to file-level errors",
                                "4,C,FAILED,,,1", "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ), Arguments.of("headerRecordCustomerIdIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,,1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,,1,0,1,0,0,0,0,0,2",
                                "2.01,E,0002,Row 2: missing mandatory field in column 3",
                                "2.02,E,1000,Row 1: The Authlink in the file name does not match the Customer Code in the header",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Account unable to be created due to file-level errors",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ), Arguments.of("headerRecordAccountCountIsNotMatchingWithAccountsInFile", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},2,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,2,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: Number of Accounts defined in Header: 2 does not match number of Accounts in file: 1",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Account unable to be created due to file-level errors",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ), Arguments.of("headerRecordAccountCountIsString", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},One,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,0,0,1,0,0,0,0,0,2",
                                "2.01,E,0003,Row 2: invalid field format 'One' in column 4",
                                "2.02,E,1000,Row 1: An Account Open file without any Accounts is invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Account unable to be created due to file-level errors",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ), Arguments.of("headerRecordAccountCountIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,0,0,1,0,0,0,0,0,2",
                                "2.01,E,0002,Row 2: missing mandatory field in column 4",
                                "2.02,E,1000,Row 1: An Account Open file without any Accounts is invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Account unable to be created due to file-level errors",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ),Arguments.of("headerRecordAllBeneficiaryCountIsNotMatching", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,1,2,1,1,1,1,1",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,1,2,1,1,1,1,1,7",
                                "2.01,E,1000,Row 1: Number of Individuals defined in Header: 1 does not match number of Individuals in file: 0",
                                "2.02,E,1000,Row 1: Number of Companies defined in Header: 2 does not match number of Companies in file: 1",
                                "2.03,E,1000,Row 1: Number of Trusts defined in Header: 1 does not match number of Trusts in file: 0",
                                "2.04,E,1000,Row 1: Number of Associations defined in Header: 1 does not match number of Associations in file: 0",
                                "2.05,E,1000,Row 1: Number of Partnerships defined in Header: 1 does not match number of Partnerships in file: 0",
                                "2.06,E,1000,Row 1: Number of Cooperatives defined in Header: 1 does not match number of Cooperatives in file: 0",
                                "2.07,E,1000,Row 1: Number of Government Bodies defined in Header: 1 does not match number of Government Bodies in file: 0",
                                "3,A,FAILED,,,1", 
                                "3.01,E,1001,Account unable to be created due to file-level errors",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ), Arguments.of("headerRecordAllBeneficiaryCountStringValues", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,One,Two,One,One,One,One,One",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,0,0,0,0,0,0,8",
                                "2.01,E,0003,Row 2: invalid field format 'One' in column 5",
                                "2.02,E,0003,Row 2: invalid field format 'Two' in column 6",
                                "2.03,E,0003,Row 2: invalid field format 'One' in column 7",
                                "2.04,E,0003,Row 2: invalid field format 'One' in column 8",
                                "2.05,E,0003,Row 2: invalid field format 'One' in column 9",
                                "2.06,E,0003,Row 2: invalid field format 'One' in column 10",
                                "2.07,E,0003,Row 2: invalid field format 'One' in column 11",
                                "2.08,E,1000,Row 1: Number of Companies defined in Header: 0 does not match number of Companies in file: 1",
                                "3,A,FAILED,,,1", 
                                "3.01,E,1001,Account unable to be created due to file-level errors",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ),Arguments.of("AccountRecordNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                ",A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 3: Invalid record number \""
                        )
                ),                Arguments.of("AccountRecordNumberIsString", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "Three,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number Three"
                        )
                ),                Arguments.of("AccountRecordNumberOutOfOrder", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "99,A,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number 99"
                        )
                ), Arguments.of("AccountRecordTypeIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text","E,Row 3: Unknown record type"

                        )
                ),Arguments.of("AccountRecordTypeNotA", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,B,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text","E,Row 3: Unknown record type"

                        )
                ),Arguments.of("AccountRecordTypeIsNumber", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,3,${accountName},,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "Record Type,Error Text","E,Row 3: Unknown record type"

                        )
                ),Arguments.of("AccountNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,,,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: All accounts in the file are invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,0002,Row 3: missing mandatory field in column 3",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"

                        )
                ),Arguments.of("AccountNumberIsTooShort", "RESPONSE", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,ON,,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,0",
                                "3,A,FAILED,,,1",
                                "3.01,E,invalid-length-account-name,Name must be between 3 and 250 characters",
                                "4,C,SUCCESS,UGFydHlDb21wYW55OmNlNGJhYzMxMjg2NWM0ZGNlOGU5Y2Q2YTI4MjU5NjZkZWJkOWNhZDdjNmE4MGEwNzUxZTNjYTAwNzc2OTc0MDA=,1000036300,0"

                        )
                ),Arguments.of("AccountProductCodeAndProductIdAreBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,,${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: All accounts in the file are invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Product Id or Product Code is required",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"
                        )
                ),Arguments.of("AccountProductCodeAndProductIdNotMatching", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},id,${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: All accounts in the file are invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Invalid Product Code provided",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"

                        )
                ),Arguments.of("AccountProductIdCorrectButCodeIsWrong", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},${productId},productCode,${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: All accounts in the file are invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Invalid Product Code provided",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"

                        )
                ),Arguments.of("AccountProductIdAndCodeCorrect", "RESPONSE", false, Maps.of(3,List.of(4,5),4,List.of(4,5)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},${productId},${productCode},${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,0",
                                "3,A,SUCCESS,QWNjb3VudDo2YzUwY2RlMWE4OGVlYmM4MmZiYmI2NjM3MGFkZDAzMzBkMmExN2IzODBlYzY1MjFmYTNiOGM1MzQ1ZTJjOTdh,847144383,0",
                                "4,C,SUCCESS,UGFydHlDb21wYW55OjA2YzY2Njg0NmQwNzUyMjgwNzJlNGQ0ZjA4NDdmMDA2YjE4NWRiN2JkZWU4ODQ0ZGI2MTgwZDU1ZTgyNDgxMDM=,7666702984,0"

                        )
                ),Arguments.of("AccountProductCodeAndProductIdBothWrong", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},id,productCode,${reference1},${reference2},1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: All accounts in the file are invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,1001,Invalid Product Code provided",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"

                        )
                ),Arguments.of("AccountReferencesAreBlank", "RESPONSE", false, Maps.of(3,List.of(4,5),4,List.of(4,5)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},,,1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,0",
                                "3,A,SUCCESS,QWNjb3VudDo0ZTE4YmFlNTU4NGYxMWUxYzRhMzdhZmI1OWYyOGQ4MmZiNDI2OTM0ZGUyNmIxMTdiMzNmNDc1ODAzOTAwODA0,000010231,0",
                                "4,C,SUCCESS,UGFydHlDb21wYW55OmMzNjY4OWVhZjAwNjc2ZjcxMDk5ODE3MTRjNzFlYjBmMjM1ZjUyMzA0NjRhZGE4OGMyMzdmZTljODEwMTZhZWQ=,1000036302,0"
                        )
                ),Arguments.of("AccountReferencesAreTooLong", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,1,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},foeiprauepewiruewjrd1,foeiprauepewiruewjrd1,1",
                                "4,C,,BENEFICIARY,${fullName},Private,,${beneficiaryRef},319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                        ), List.of(
                                "1,V,2.0",
                                "2,H,BULK2,1,0,1,0,0,0,0,0,1",
                                "2.01,E,1000,Row 1: All accounts in the file are invalid",
                                "3,A,FAILED,,,1",
                                "3.01,E,0003,Row 3: field 5 exceeds the maximum size (20 characters)",
                                "4,C,FAILED,,,1",
                                "4.01,E,1001,Party unable to be created due to errors with Account"

                        )
                )
        );
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("beneficiary-CompanyValidations", "RESPONSE", false, Maps.of(3,List.of(4,5),4,List.of(4,5)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,0,7,0,0,0,0,0",
                                "3,A,${accountName},,${productCode},${reference1},${reference2},7",
                                ",,,,,,,,319477971,51824753556,AU,1892-06-03,ASX,greshamtech,greshamtech@gmail.com,+612323454544,Other,level 18,100 Queen street,Melbourne,VIC,3000,AU,6 wood st,,Chatswood West,NSW,2067,AU,1,AU",
                                "5,X,AU,183772436,"
                               
                        ), List.of(
                                "1,V,2.0"
                                

                        )
                )
        );
    }


}
