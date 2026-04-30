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
    };
    
   public abstract String getDataDir();
  public abstract String getFilePrefix();
   
}
