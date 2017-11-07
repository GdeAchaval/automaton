import java.io.*;

/**
 * @author Gonzalo De Achaval
 * @author Marcos Khabie
 * @author Agustin Bettati
 * @version 1.0
 */
class FileManager {

    File[] getHtmlFilesInDirectory(String path){
        FilenameFilter filter = (dir, name) -> name.endsWith(".html");

        File folder = new File(path);
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
