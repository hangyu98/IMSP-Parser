import java.io.*;

public class Top100LongestPattern {

    static final int TOP_NUM = 100;
    static int totalLength = 0;
    static BufferedWriter numericData;
    static String name1;
    static String name2;
    static {
        try {
            numericData = new BufferedWriter(new FileWriter("longest_seq.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String[] longestTop100 = new String[TOP_NUM];

    public static void main(String[] args) {
        try {
            File file1 = new File("Spike/covid-19-spike.txt");
            name1 = file1.getName().substring(0, file1.getName().length() - 4);
            File file2 = new File("HIV_E_Protein.txt");
            name2 = file2.getName().substring(0, file2.getName().length() - 4);
            BufferedReader covidReader = new BufferedReader(new FileReader(file1));
            BufferedReader sarsReader = new BufferedReader(new FileReader(file2));
            StringBuilder covidSB = new StringBuilder();
            StringBuilder sarsSB = new StringBuilder();
            String covid = getString(covidReader, covidSB);
            String sars = getString(sarsReader, sarsSB);
            LCS(covid, sars, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(BufferedReader reader, StringBuilder sb) throws IOException {
        String line = reader.readLine();
        while (line != null) {
            line = line.trim();
            // skip white spaces
            sb.append(line);
            line = reader.readLine();
        }
        return sb.toString();
    }

    private static void LCS(String X, String Y, int count) throws IOException {
        if (count == TOP_NUM) {
            return;
        }
        int m = X.length();
        int n = Y.length();
        int maxLen = 0;            // stores the max length of LCS
        int endingIndexInX = m;        // stores the ending index of LCS in X
        int endingIndexInY = n;

        // lookup[i][j] stores the length of LCS of substring
        // X[0..i-1], Y[0..j-1]
        int[][] lookup = new int[m + 1][n + 1];

        // fill the lookup table in bottom-up manner
        for (int i = 1; i <= m; i++) {
            if (X.charAt(i - 1) == '0')
                continue;
            for (int j = 1; j <= n; j++) {
                if (Y.charAt(j - 1) == '0')
                    continue;
                // if current character of X and Y matches
                if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                    lookup[i][j] = lookup[i - 1][j - 1] + 1;
                    // update the maximum length and ending index
                    if (lookup[i][j] > maxLen) {
                        maxLen = lookup[i][j];
                        endingIndexInX = i;
                        endingIndexInY = j;
                    }
                }
            }
        }
        longestTop100[count] = X.substring(endingIndexInX - maxLen, endingIndexInX);
        writeToFile(X, Y, maxLen, endingIndexInX, endingIndexInY, numericData);
        totalLength += longestTop100[count].length();
        StringBuilder sb = new StringBuilder();
        int length = maxLen;
        while (length > 0) {
            sb.append(0);
            length--;
        }
        String marked = sb.toString();
        X = X.substring(0, endingIndexInX - maxLen).concat(marked).concat(X.substring(endingIndexInX));
        Y = Y.substring(0, endingIndexInY - maxLen).concat(marked).concat(Y.substring(endingIndexInY));
        LCS(X, Y, ++count);
    }

    private static void writeToFile(String X, String Y, int maxLen, int endingIndexInX, int endingIndexInY, BufferedWriter numericData) throws IOException {
        numericData.write("start index in " + name1 + " : " + (endingIndexInX - maxLen));
        numericData.write("; end index in " + name1 + " : " + endingIndexInX);
        numericData.write("; sequence is: " + X.substring((endingIndexInX - maxLen), endingIndexInX));
        numericData.write("\n");
        numericData.write("start index in " + name2 + " : " + (endingIndexInY - maxLen));
        numericData.write("; end index in " + name2 + " : " + endingIndexInY);
        numericData.write("; sequence is: " + Y.substring((endingIndexInY - maxLen), endingIndexInY));
        numericData.newLine();
        numericData.newLine();
        numericData.flush();
    }
}