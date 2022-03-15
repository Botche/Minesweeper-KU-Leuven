package utilities;

import com.google.gson.Gson;
import utilities.constants.Common;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class FileHelper {
    private static final String ERROR_MESSAGE = "An error occurred.";
    private static Gson gson = new Gson();

    public static void createFile(String filePath) {
        try {
            File leaderboard = new File(Common.LEADERBOARD_FILE_NAME);
            leaderboard.createNewFile();
        } catch (IOException ioe) {
            System.out.println(ERROR_MESSAGE);
            ioe.printStackTrace();
        }
    }

    public static <T> T readFileFromJson(String filePath, Class objectClass) {
        T data = null;

        try {
            FileReader reader = new FileReader(filePath);
            data = (T)gson.fromJson(reader, objectClass);
            reader.close();

            return data;
        } catch (IOException ioe) {
            System.out.println(ERROR_MESSAGE);
            ioe.printStackTrace();
        }

        return data;
    }

    public static <T> void writeFileToJson(T data) {
        try {
            FileWriter writer = new FileWriter(Common.LEADERBOARD_FILE_NAME);
            writer.append(gson.toJson(data));
            writer.close();
        } catch (IOException ioe) {
            System.out.println(ERROR_MESSAGE);
            ioe.printStackTrace();
        }
    }
}
