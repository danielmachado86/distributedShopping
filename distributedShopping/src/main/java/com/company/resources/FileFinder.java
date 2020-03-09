package com.company.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileFinder {

    private List<Path> fileList;
    private Logger log;

    public FileFinder(String path, String fileType){

        fileList = new ArrayList<Path>();
        log = new TestLogger();

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths
                .filter(p -> p.toString().endsWith("." + fileType))
                .forEach(this::addPathToList);
        } catch (IOException e) {
            log.newEntry(e.getMessage());
        }

    }

    private void addPathToList(Path path){
        fileList.add(path);
        log.newEntry(path);
    }

	public List<Path> getListOfPaths() {
		return fileList;
	}



    
}