package com.sure.helper;

import com.github.javafaker.Faker;

public class Investigator {


    public static void main(String[] args) {
        String number = String.valueOf(new Faker().number().randomDigitNotZero());
        System.out.println(" Number: " + number);


//        String testDir = "/downloadedFiles/Download";
//        String testFile = "testFile.txt";
//        String testFileContent = "Hello, world!";
//        String csvFilePath = "testData.csv";
//        String newFileName = "renamedFile";
//        String fileExtension = "txt";
//
//        // Create a directory
//        FilesDirectories.createDir(testDir);
//
//        // Create a file with content
//        FilesDirectories.createFile(testDir + "/" + testFile, testFileContent);
//
//        // Read file data
//        String content = FilesDirectories.readFileData(testDir + "/" + testFile);
//        System.out.println("File content: " + content);
//
//        // Write some dummy CSV data
//        try {
//            var path = Paths.get(csvFilePath);
//            Files.write(path, "name,age\nJohn,30\nJane,25".getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Get CSV data
//        List<List<String>> csvData = FilesDirectories.getCsvData(csvFilePath);
//        System.out.println("CSV Data: " + csvData);
//
//        // Get CSV value
//        String value = FilesDirectories.getCsvValue(csvData, "age");
//        System.out.println("CSV Value for age: " + value);
//
//        // Check if file is downloaded (assuming the file is created)
//        boolean fileExists = FilesDirectories.isFileDownloaded(testFile, fileExtension, 5000, Instant.now().toEpochMilli());
//        System.out.println("File downloaded: " + fileExists);
//
//        // Rename file
//        boolean renamed = FilesDirectories.renameFile(testDir, testFile, newFileName, fileExtension);
//        System.out.println("File renamed: " + renamed);
//
//        // Get file names
//        List<String> fileNames = FilesDirectories.getFileNames(testDir);
//        System.out.println("File names: " + fileNames);
//
//        // Delete directory
//        FilesDirectories.deleteDirectory(testDir);
//
//        // Clean up CSV file
//        try {
//            Files.delete(Paths.get(csvFilePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}


