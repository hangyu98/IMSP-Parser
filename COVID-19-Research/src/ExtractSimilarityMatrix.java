import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * extract matrix from csv file
 */
public class ExtractSimilarityMatrix {

    /**
     * name of hitTable
     */
    static String hitTableFileName = "ACE2 alignment related/Test/D5J28CM911N-Alignment-HitTable.csv";
    /**
     * name of hitTable
     */
    static String alignmentFileName = "ACE2 alignment related/Test/D5J28CM911N-Alignment.txt";
    /**
     * output file name
     */
    static String resultFile = "ACE2 alignment related/Result/" + hitTableFileName.split("/")[2].split("-")[0] + "-Result.csv";
    /**
     * id-name map, checked
     */
    static HashMap<String, String> map = ReplaceName.buildNameIDMap(alignmentFileName);

    /**
     * number of aligned sequences
     */
    static int numOfSeq = map.size() + 1;

    /**
     * driver code
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            File file = new File(hitTableFileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile));
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
                            bufferedWriter.write(matrix[row][col] + ",");
                        }
                    }
                    // last column
                    else {
                        if (row == 0) {
                            bufferedWriter.write(map.get(matrix[row][col]));
                        } else {
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
    private static int updateSet(HashSet<String> set, String name, String[][] matrix, int idx) {
        if (!set.contains(name)) {
            matrix[0][idx] = name;
            matrix[idx][0] = name;
            idx++;
            set.add(name);
        }
        return idx;
    }

}
