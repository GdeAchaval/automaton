import java.io.*;

/**
 * @author Gonzalo De Achaval
 * @author Marcos Khabie
 * @author Agustin Bettati
 * @version 1.0
 */
class FileManager {

    void writeToTextFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File[] getHtmlFilesInDirectory(){
        FilenameFilter filter = (dir, name) -> name.endsWith(".html");

        String pathName = System.getProperty("user.dir");
        File folder = new File(pathName);
        return folder.listFiles(filter);
    }

    String getContentOfFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();

        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader= new BufferedReader(fileReader);

        String line = bufferedReader.readLine();
        while (line!= null) {
            content.append(line).append("\n");
            line  = bufferedReader.readLine();
        }
        fileReader.close();

        return content.toString();
    }
}
