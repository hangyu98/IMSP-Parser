package Parser;

import java.io.*;
import java.util.HashMap;

public class ReplaceName {
    /**
     * driver code
     */
    public static HashMap<String, String> buildNameIDMap(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            HashMap<String, String> map = new HashMap<>();
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.length() == 0)
                    break;
                line = bufferedReader.readLine();
                if (line.startsWith("Subject")) {
                    extractPair(map, line);
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null; //indicate error
        }
    }

    private static void extractPair(HashMap<String, String> map, String line) {
        String res = line.split("]")[0].split(":", 2)[1];
        String[] pair = res.split(" ", 2);
        map.put(pair[0], (pair[1] + "]").replace(",", ""));
    }

}