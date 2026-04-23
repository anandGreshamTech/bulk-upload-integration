package com.gresham.bulk.upload;

import com.gresham.bulk.upload.service.ResourceReader;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public interface BulkUploadTestProcessor {
    String RESULT_DIR = "/home/ccmsim/bulkupload/response";
    String CONTAINER = "simulator-sftp-server";

    default List<Path> getDataDirForUpload(boolean devMode, String dataDir) {
        String drRegex = devMode ? "^(sc-.*|.*-sc)$" : ".*";
        Pattern pattern = Pattern.compile(drRegex);
        return Optional.ofNullable(ResourceReader.getTempDir(dataDir))
                .orElse(List.of())
                .stream()
                .filter(path ->
                        pattern.matcher(
                                path.getFileName().toString()
                        ).find()
                )
                .toList();
    }
}
