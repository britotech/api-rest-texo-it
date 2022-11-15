package tech.brito.apiresttexoit.core;

public class Constants {
    public static final String BASE_DIRECTORY;

    static {
        var absolutePath = new java.io.File("").getAbsolutePath();
        BASE_DIRECTORY = absolutePath;
    }

    public static String getMovieFilePath(String fileName) {
        return BASE_DIRECTORY + "/" + fileName;
    }
}
