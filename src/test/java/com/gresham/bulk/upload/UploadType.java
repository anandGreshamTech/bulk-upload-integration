package com.gresham.bulk.upload;

public enum UploadType {
    TRANSFER{
        @Override
        public String getDataDir() {
            return "src/test/resources/bulkUpload/transfer";
        }

        @Override
        public String getFilePrefix() {
            return "TFR";
        }
    },CLOSE_ACCOUNT {
        @Override
        public String getDataDir() {
            return "src/test/resources/bulkUpload/closeAccount";
        }

        @Override
        public String getFilePrefix() {
            return "CLOSE";
        }
    },
    VAM_OPEN_ACCOUNT {
        @Override
        public String getDataDir() {
            return "src/test/resources/bulkUpload/vamOpenAccount";
        }

        @Override
        public String getFilePrefix() {
            return "VAMAccountOpen";
        }
    },
    PAYMENT{
        @Override
        public String getDataDir() {
            return "src/test/resources/bulkUpload/payment";
        }

        @Override
        public String getFilePrefix() {
            return "Pay";
        }
    },
    AGG_PAYMENT{
        @Override
        public String getDataDir() {
            return "src/test/resources/bulkUpload/aggPayment";
        }

        @Override
        public String getFilePrefix() {
            return "AggPay";
        }
    };
    
   public abstract String getDataDir();
  public abstract String getFilePrefix();
   
}
