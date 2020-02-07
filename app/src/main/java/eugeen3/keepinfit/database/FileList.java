package eugeen3.keepinfit.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileList<T> {

    private String name;
    //private String path;
    private List<T> list;

    public FileList(String fileName, List<T> list) {
        this.name = fileName;
        //this.path = filePath;
        this.list = list;
    }

    public FileList() { }

    public void saveList() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(name)));
            for(T foodItem : list){
                pw.println(foodItem.toString());
                pw.flush();
            }
            pw.close();
        }
        catch(IOException ex){
            System.err.println(ex);
        }
    }

    public List<String> loadList() throws IOException {
        List<String> list = new LinkedList<String>();
        BufferedReader bufferedReader = null;
        try {
            String sCurrentLine;
            bufferedReader = new BufferedReader(new FileReader(name));
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                list.add(sCurrentLine);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            bufferedReader.close();
        }
        return list;
    }

    public String[] stringParser(String line) {
        String delimeter = " ";
        String subStr[] = line.split(delimeter);
        return subStr;
    }
}
