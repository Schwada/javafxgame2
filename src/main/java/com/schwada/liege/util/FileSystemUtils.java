package com.schwada.liege.util;

import java.io.File;

/**
 * Class handles all file related utilities. It also includes methods
 * for working with local storage folder which stores saves, resources
 * and other user data.
 *
 * @author Schwada
 */
public class FileSystemUtils {

    public static final String NAMESPACE = "schwada";
    public static final String PROJECT_NAMESPACE = "liege";

    /**
     * If folder does not exist creates working directory
     * from namespace and returns it.
     *
     * @return path to working directory
     */
    public static String getWorkingDir() {
        String userDataDir;
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            userDataDir = System.getenv("AppData");
        } else {
            userDataDir = System.getProperty("user.home");
            userDataDir += "/Library/Application Support";
        }
        File workingDir = new File(userDataDir + "/" + NAMESPACE + "/" + PROJECT_NAMESPACE);
        // FIXME: handle could not create
        workingDir.mkdirs();
        return workingDir.getAbsolutePath();
    }
}
