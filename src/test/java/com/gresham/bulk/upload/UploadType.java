package com.gresham.bulk.upload;

public enum UploadType {
    TRANSFER{
        @Override
        String getDataDir() {
            return "src/test/resources/bulkUpload/transfer";
        }

        @Override
        String getFilePrefix() {
            return "TFR";
        }
    },CLOSE_ACCOUNT{
        @Override
        String getDataDir() {
            return "src/test/resources/bulkUpload/closeAccount";
        }

        @Override
        String getFilePrefix() {
            return "CLOSE";
        }
    };
    
   abstract String getDataDir();
   abstract String getFilePrefix();
   
}
