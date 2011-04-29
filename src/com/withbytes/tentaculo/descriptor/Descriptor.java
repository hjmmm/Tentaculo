package com.withbytes.tentaculo.descriptor;

import java.util.ArrayList;

/**
 * POJO representation of a descriptor file
 * @author Javier Morales <moralesjm at gmail.com>
 */
public class Descriptor {

    private String folderName;
    private ArrayList<String> windowsPaths;

    Descriptor(String folderName, ArrayList<String> windowsFiles) {
        this.folderName = folderName;
        this.windowsPaths = windowsFiles;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<String> getWindowsPaths() {
        return windowsPaths;
    }

    public void setWindowsPaths(ArrayList<String> windowsPaths) {
        this.windowsPaths = windowsPaths;
    }
    
}
