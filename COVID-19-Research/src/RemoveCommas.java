import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class RemoveCommas {
    /**
     * name of hitTable
     */
    static String fileName = "seqdump copy 2.txt";

    /**
     * driver code
     */
    public static void removeCommas() {
        try {
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("seqdata.txt")));
            String line = bufferedReader.readLine();
            while (line != null) {
//                System.out.println("line is: " + line);
                if (line.length() == 0)
                    break;
                if (line.contains(",")) {
                    System.out.println("before: " + line);
                    String toWrite = line.replace(",", "");
                    System.out.println("after: " + toWrite);
                    bufferedWriter.write(toWrite);
                } else {
                    bufferedWriter.write(line);
                }
                bufferedWriter.newLine();
                line = bufferedReader.readLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        removeCommas();
    }
}