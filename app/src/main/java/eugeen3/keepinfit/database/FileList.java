package eugeen3.keepinfit.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class FileList<T> {

    private String name;
    private List<T> list;
    public static final char CHAR_SPACE = ' ';
    public static final char CHAR_UNDERSCORE = '_';

    public FileList(String fileName, List<T> list) {
        this.name = fileName;
        this.list = list;
    }

    public FileList() { }

    public void saveList() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(name)));
            for(T foodItem : list){
                pw.println(underscores(foodItem.toString()));
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

    public String underscores(String str) {
        int cnt = 0;
        int i = str.length() - 1;
        while(true) {
            if (str.charAt(i) == CHAR_SPACE) cnt++;
            if (cnt == 5) break;
            i--;
        }
        String newStr = str.substring(0, i);
        str = str.substring(i + 1);
        str = newStr.replace(CHAR_SPACE, CHAR_UNDERSCORE) + " " + str;
        return str;
    }
}