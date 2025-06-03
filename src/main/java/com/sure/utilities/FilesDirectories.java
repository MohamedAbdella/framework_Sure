package com.sure.utilities;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
public class FilesDirectories {
    private FilesDirectories() {
    }

    public static final String USER_DIR = System.getProperty("user.dir");
    private static final String DOWNLOAD_DIR = USER_DIR + File.separator + "attachments/downloadedFiles" + File.separator + "Download";

    @Step("Create directory: {path}")
    public static Path createDir(String path) {
        Path dirPath = Paths.get(USER_DIR + path);
        if (Files.notExists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
                log.info("Directory has been created successfully");
            } catch (IOException e) {
                log.error("Failed to create directory", e);
            }
        } else {
            log.info("Directory already exists");
        }
        return dirPath;
    }

    @Step("Create file: {fileNameWithPath} with content")
    public static void createFile(String fileNameWithPath, String content) {
        Path path = Paths.get(USER_DIR + fileNameWithPath);
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
                Files.writeString(path, content);
                log.info("File created");
            } catch (IOException e) {
                log.error("Failed to create file", e);
            }
        } else {
            log.info("File already exists");
        }
    }

    @Step("Read data from file: {filePath}")
    public static String readFileData(String filePath) {
        Path path = Paths.get(USER_DIR + filePath);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            log.error("Failed to read file", e);
            return null;
        }
    }

    @Step("Read CSV data from file: {filePath}")
    public static List<List<String>> getCsvData(String filePath) {
        List<List<String>> lines = new ArrayList<>();
        Path path = Paths.get(filePath);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            lines = bufferedReader.lines()
                    .map(line -> Arrays.asList(line.split(",")))
                    .toList(); // Replace Collectors.toList() with toList()
        } catch (IOException e) {
            log.error("Failed to read CSV file", e);
        }
        return lines;
    }

    @Step("Get CSV value for header: {headerName}")
    public static String getCsvValue(List<List<String>> list, String headerName) {
        headerName = "\"" + headerName + "\"";
        int index = list.get(0).indexOf(headerName);
        if (index != -1) {
            return list.get(list.size() - 1).get(index).replace("\"", "");
        }
        return null;
    }

    @Step("Check if file: {expectedFileName} with extension: {fileExtension} is downloaded within timeout: {timeOut}")
    public static boolean isFileDownloaded(String expectedFileName, String fileExtension, int timeOut, long startTime) {
        createDir(DOWNLOAD_DIR);

        File[] listOfFiles;
        boolean fileDownloaded = false;
        long waitTime = startTime + timeOut;

        while (Instant.now().toEpochMilli() < waitTime) {
            listOfFiles = new File(DOWNLOAD_DIR).listFiles();
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    String fileName = file.getName();
                    if (file.lastModified() > startTime &&
                            fileName.contains(fileExtension.toLowerCase()) &&
                            fileName.contains(expectedFileName)) {
                        fileDownloaded = true;
                        break;
                    }
                }
            }
            if (fileDownloaded) break;
        }
        return fileDownloaded;
    }

    @Step("Rename file from: {oldFileName} to: {newFileName}.{fileExtension} in directory: {dirPath}")
    public static boolean renameFile(String dirPath, String oldFileName, String newFileName, String fileExtension) {
        Path oldFilePath = Paths.get(dirPath, oldFileName);
        Path newFilePath = Paths.get(dirPath, newFileName + "." + fileExtension);
        try {
            Files.move(oldFilePath, newFilePath);
            log.info("File successfully renamed");
            return true;
        } catch (IOException e) {
            log.error("Failed to rename file", e);
            return false;
        }
    }

    @Step("Clean directory: {dirPath}")
    public static void cleanDirectory(String dirPath) {
        try {
            FileUtils.cleanDirectory(new File(dirPath));
            log.info("Directory cleaned");
        } catch (IOException e) {
            log.error("Failed to clean directory", e);
        }
    }

    @Step("Check if directory: {dirPath} is empty")
    public static boolean isDirectoryEmpty(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (Files.isDirectory(path)) {
                try (Stream<Path> stream = Files.list(path)) {
                    boolean isEmpty = stream.findAny().isEmpty();
                    if (isEmpty) {
                        log.info("Directory is empty: {}", dirPath);
                    } else {
                        log.info("Directory is not empty: {}", dirPath);
                    }
                    return isEmpty;
                }
            } else {
                log.info("Not a directory: {}", dirPath);
                return false;
            }
        } catch (IOException e) {
            log.error("Failed to check if directory is empty", e);
            return false;
        }
    }

    @Step("Get file names from download directory")
    public static List<String> getFileNames() {
        return getFileNames(DOWNLOAD_DIR);
    }

    @Step("Get file names from directory: {dirPath}")
    public static List<String> getFileNames(String dirPath) {
        Path path = Paths.get(dirPath);
        if (isDirectoryEmpty(dirPath)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.list(path)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .toList();
        } catch (IOException e) {
            log.error("Failed to get file names", e);
            return new ArrayList<>();
        }
    }

    @Step("Get file name containing: {fileName} with extension: {extension} from folder: {folderPath}")
    public static String getFileName(String folderPath, String fileName, String extension) {
        try (Stream<Path> stream = Files.list(Paths.get(folderPath))) {
            return stream
                    .filter(p -> p.getFileName().toString().contains(fileName) && p.getFileName().toString().contains(extension))
                    .map(p -> p.getFileName().toString())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            log.error("Failed to get file name", e);
            return null;
        }
    }

    @Step("Check if directory exists: {directoryPath}")
    public static boolean isDirectoryExist(String directoryPath) {
        return Files.exists(Paths.get(directoryPath));
    }

    @Step("Delete directory: {directoryPath}")
    public static void deleteDirectory(String directoryPath) {
        try {
            FileUtils.forceDelete(new File(directoryPath));
            log.info("Directory deleted: {}", directoryPath);
        } catch (IOException e) {
            log.error("Failed to delete directory", e);
        }
    }

    @Step("Move directory from: {sourceDir} to: {destinationDir}")
    public static void moveDir(String sourceDir, String destinationDir) {
        Path srcPath = Paths.get(USER_DIR, sourceDir);
        Path destPath = Paths.get(USER_DIR, destinationDir);
        try {
            FileUtils.moveDirectory(srcPath.toFile(), destPath.toFile());
            log.info("Directory moved from {} to {}", sourceDir, destinationDir);
        } catch (IOException e) {
            log.error("Failed to move directory", e);
        }
    }
}
