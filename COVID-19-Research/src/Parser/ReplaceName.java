package Parser;

import java.io.*;
import java.util.HashMap;

public class ReplaceName {
    /**
     * build name-id map
     *
     * @param fileName name of the file
     * @return a hashmap of such
     */
    public static HashMap<String, String> buildNameIDMap(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            HashMap<String, String> map = new HashMap<>();
            String line = bufferedReader.readLine();
            // if FASTA file exists
            if (fileName.endsWith("FASTA.txt")) {
                while (line != null) {
                    if (line.length() == 0)
                        break;
                    line = bufferedReader.readLine();
                    if (line.startsWith("Subject")) {
                        extractPair2(map, line);
                    }
                }
            }
            // read from Alignment file
            else {
                while (line != null) {
                    if (line.startsWith(">")) {
                        extractPair(map, line);
                    }
                    line = bufferedReader.readLine();
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null; //indicate error
        }
    }

    private static void extractPair(HashMap<String, String> map, String line) {
        String name = line.split("\\[")[1];
        name = name.substring(0, name.length() - 1);
        String code = line.substring(1).split(" ", 2)[0];
        System.out.println("code: " + code);
        map.put(code, name);
    }

    private static void extractPair2(HashMap<String, String> map, String line) {
        String res = line.split("]")[0].split(":", 2)[1];
        String[] pair = res.split(" ", 2);
        map.put(pair[0], (pair[1] + "]").replace(",", ""));
    }

}