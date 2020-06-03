package Parser;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * extract matrix from csv file
 */
public class ExtractSimilarityMatrix {

    /**
     * name of hitTable
     */
    String hitTableFileName;
    /**
     * name of alignment file
     */
    String alignmentFileName;
    /**
     * output file name
     */
    String resultFileName;
    /**
     * id-name map, checked
     */
    HashMap<String, String> map;

    /**
     * number of aligned sequences
     */
    int numOfSeq;

    String dir;

    public ExtractSimilarityMatrix(String hitTableFileName, String alignmentFileName, String resultFileName, String dir) {
        this.hitTableFileName = hitTableFileName;
        this.alignmentFileName = alignmentFileName;
        this.resultFileName = resultFileName;
        this.dir = dir;
    }

    /**
     * build one similarity matrix
     */
    public void extractOneSimMatrix() {
        try {
            map = ReplaceName.buildNameIDMap(dir + "/" + alignmentFileName);
            numOfSeq = map.size() + 1;
            File file = new File(dir + "/" + hitTableFileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFileName));
            String line = bufferedReader.readLine();
            String[][] matrix = new String[numOfSeq][numOfSeq];
            matrix[0][0] = "matrix name";
            // initialize matrix name
            int i = 1;
            HashSet<String> set = new HashSet<>();
            // read
            while (line != null) {
                if (line.length() == 0)
                    break;
                String[] split = line.split(",");
                // extract name from file
                String query_id = split[0];
                String subject_name = split[1];
                // similarity is always the last element in csv file
                String similarity = split[12];
                // update the set and leading entries on the x axis and y axis
                i = updateSet(set, query_id, matrix, i);
                i = updateSet(set, subject_name, matrix, i);
                // update matrix
                for (int p = 1; p < matrix[0].length; p++) {
                    // 第一行
                    if (matrix[0][p].equals(query_id)) {
                        // 列
                        for (int q = 1; q < matrix.length; q++) {
                            if (matrix[q][0].equals(subject_name)) {
                                matrix[q][p] = similarity;
                                break;
                            }
                        }
                        break;
                    }
                }
                line = bufferedReader.readLine();
            }
            // write to file
            for (int row = 0; row < matrix.length; row++) {
                for (int col = 0; col < matrix[0].length; col++) {
                    // first column, get name
                    if (col == 0) {
                        bufferedWriter.write(map.get(matrix[row][col]) + ",");
                    }
                    // columns in the middle
                    else if (col != matrix[0].length - 1) {
                        if (row == 0) {
                            bufferedWriter.write(map.get(matrix[row][col]) + ",");
                        } else {
                            if (matrix[row][col] == null)
                                bufferedWriter.write("null,");
                            else
                                bufferedWriter.write(matrix[row][col] + ",");
                        }
                    }
                    // last column
                    else {
                        if (row == 0) {
                            bufferedWriter.write(map.get(matrix[row][col]));
                        } else {
                            if (matrix[row][col] == null)
                                bufferedWriter.write("null");
                            else
                                bufferedWriter.write(matrix[row][col]);
                        }
                    }
                }
                bufferedWriter.flush();
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * update the hashSet
     *
     * @param set    set of all seen names
     * @param name   name
     * @param matrix matrix
     * @param idx    index pointing to an empty slot
     * @return new index pointing to the next empty leading entry
     */
    private int updateSet(HashSet<String> set, String name, String[][] matrix, int idx) {
        if (!set.contains(name)) {
            matrix[0][idx] = name;
            matrix[idx][0] = name;
            idx++;
            set.add(name);
        }
        return idx;
    }

}
