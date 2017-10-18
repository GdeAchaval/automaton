import java.io.*;

/**
 * @author Gonzalo De Achaval
 * @author Marcos Khabie
 * @author Agustin Bettati
 * @version 1.0
 */
public class FileManager {

    public void writeToTextFile(String fileName,String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File[] getHtmlFilesInDirectory(){
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".html");
            }
        };

        String pathName = System.getProperty("user.dir");
        File folder = new File(pathName);
        return folder.listFiles(filter);
    }

    public String getContentOfFile(File file) throws IOException {
        String content = "";

        FileReader fileReader = new FileReader(file);

        BufferedReader bufferedReader= new BufferedReader(fileReader);

        String line = bufferedReader.readLine();
        while (line!= null) {
            content += line + "\n";
            line  = bufferedReader.readLine();
        }
        fileReader.close();

        return content;
    }
}
