package com.example.fire.utils;

/**
 * Created by GIOI on 4/6/2016.
 */
public class AppConstCamera {
    //Luu file
    public static String FOLDER_Pictures = "Pictures/";
    public static String FOLDER_SAVE_PHOTO = "BLUESTUDIO_FRAME";
    public static String PATH_FILE_SAVE_PHOTO = "mnt/sdcard/" + FOLDER_Pictures + FOLDER_SAVE_PHOTO + "/";

    public static void setFolderName(String nameFolder) {
        FOLDER_SAVE_PHOTO = nameFolder;
    }

    public static final String FORMAT_TYPE_JPG = "image/jpeg";
    public static final String FORMAT_TYPE_PNG = "image/png";


    // BUNDLE KEYs
    public static final String BUNDLE_KEY_IMAGE_SELECTED = "IMAGE_SELECTED";

    public static final String DOT = ".";
    public static final String FORMAT_FILE_SAVE = ".jpg";

    public static String PATH_FILE_SAVE_SHARE_PHOTO = "mnt/sdcard/" + DOT + "Share/";

    //Format photo
    public static String[] IMAGE_ACCEPT_EXTENSIONS = new String[]{"jpg", "png", "gif", "jpeg"};
}
