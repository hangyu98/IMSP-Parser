import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class GlobalAlignment {

    /**
     * Driver code
     *
     * @param args args
     */
    public static void main(String[] args) {
        File[] allFiles = getAllFilesInCurrentDir();
        Arrays.sort(allFiles);
        for (int j = 1; j < allFiles.length; j++) {
            compare(allFiles[0], allFiles[j]);
        }
    }

    // function to find out
    // the minimum penalty
    private static void getMinimumPenalty(String fileName1, String fileName2, String x, String y, int pxy, int pgap) {

        int m = x.length(); // length of gene1
        int n = y.length(); // length of gene2

        // table for storing optimal
        // substructure answers
        int[][] dp = new int[n + m + 1][n + m + 1];

        // initializing the table
        for (int i = 0; i <= (n + m); i++) {
            dp[i][0] = i * pgap;
            dp[0][i] = i * pgap;
        }

        // calculating the
        // minimum penalty
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + pxy,
                            dp[i - 1][j] + pgap),
                            dp[i][j - 1] + pgap);
                }
            }
        }

        // Reconstructing the solution
        int l = n + m; // maximum possible length

        int i = m;
        int j = n;

        int xpos = l;
        int ypos = l;

        // Final answers for
        // the respective strings
        int xans[] = new int[l + 1];
        int yans[] = new int[l + 1];

        while (!(i == 0 || j == 0)) {
            if (x.charAt(i - 1) == y.charAt(j - 1)) {
                xans[xpos--] = x.charAt(i - 1);
                yans[ypos--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j - 1] + pxy == dp[i][j]) {
                xans[xpos--] = x.charAt(i - 1);
                yans[ypos--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j] + pgap == dp[i][j]) {
                xans[xpos--] = x.charAt(i - 1);
                yans[ypos--] = '_';
                i--;
            } else if (dp[i][j - 1] + pgap == dp[i][j]) {
                xans[xpos--] = '_';
                yans[ypos--] = y.charAt(j - 1);
                j--;
            }
        }
        while (xpos > 0) {
            if (i > 0) xans[xpos--] = x.charAt(--i);
            else xans[xpos--] = '_';
        }
        while (ypos > 0) {
            if (j > 0) yans[ypos--] = y.charAt(--j);
            else yans[ypos--] = '_';
        }
        int id = 1;
        for (i = l; i >= 1; i--) {
            if ((char) yans[i] == '_' &&
                    (char) xans[i] == '_') {
                id = i + 1;
                break;
            }
        }

        // Printing the final answer
        String original = printEachElement(fileName1, fileName2, l, xans, yans, id);
        System.out.println(original);
    }

    private static String printEachElement(String fileName1, String fileName2, int l, int[] original, int[] aligned, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        int gap = 0;
        int unlikely_mutation = 0;
        if (fileName1.startsWith("1")) {
            stringBuilder.append(String.format("%s", fileName1.substring(1, fileName1.length() - 4)))
                    .append(" with ")
                    .append(fileName2, 0, fileName2.length() - 4)
                    .append("\n");
        } else {
            stringBuilder.append(String.format("%s", fileName1.substring(0, fileName1.length() - 4)))
                    .append(" with ")
                    .append(fileName2, 0, fileName2.length() - 4)
                    .append("\n");
        }
        char[] ori = new char[5];
        char[] ali = new char[5];
        for (int i = id; i <= l; i++) {
            char originalChar = (char) original[i];
            char alignedChar = (char) aligned[i];
            if (originalChar == '_') gap++;
            if (i == (30 + id + gap) || i == (34 + id + gap) || i == (81 + id + gap) || i == (37 + id + gap) || i == (352 + id + gap)) {
                if (i == 30 + id + gap) {
                    ori[0] = originalChar;
                    ali[0] = alignedChar;
                }
                if (i == 34 + id + gap) {
                    ori[1] = originalChar;
                    ali[1] = alignedChar;
                }
                if (i == 37 + id + gap) {
                    ori[2] = originalChar;
                    ali[2] = alignedChar;
                }
                if (i == 81 + id + gap) {
                    ori[3] = originalChar;
                    ali[3] = alignedChar;
                }
                if (i == 352 + id + gap) {
                    ori[4] = originalChar;
                    ali[4] = alignedChar;
                }
                int distance = Blosum.getDistance(originalChar, alignedChar);
                if (distance < 0)
                    unlikely_mutation++;
                stringBuilder.append("hotspot ")
                        .append(String.format("%3d", i - (id + gap - 1)))
                        .append(String.format("%2s", " is:    "))
                        .append(originalChar)
                        .append(String.format("%2s%1s", "-", ""))
                        .append(alignedChar);
                stringBuilder.append(original[i] == aligned[i] ? String.format("%13s", "is match!") : String.format("%13s", "no match!"))
                        .append(String.format("%15s", "BLOSUM score:"))
                        .append(String.format("%3d", distance))
                        .append("  ")
                        .append(distance < 0 ? "unlikely" : "likely")
                        .append("\n");
            }
        }
        if (ori[0] == ali[0] && ori[1] == ali[1] && ori[0] == 'K' && ori[1] == 'E') {
            stringBuilder.append(String.format("Hot spot:%7s is matched", " 31K (form salt bridge with 35D/E)")).append("\n");
        }
        if ((ori[2] == 'D' && ali[2] == 'E' || ori[2] == ali[2]) && ori[4] == ali[4] && ori[2] == 'D' && ori[4] == 'K') {
            stringBuilder.append(String.format("Hot spot:%7s is matched", " 353K (form salt bridge with 38D/E)")).append("\n");
        }
        if (ori[3] == ali[3]) {
            stringBuilder.append(String.format("Hot spot:%7s is matched", " 82")).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * converts txt file in different format to string
     *
     * @param reader reader
     * @param sb     string builder
     * @return seq in string formatting
     * @throws IOException io
     */
    public static String getString(BufferedReader reader, StringBuilder sb) throws IOException {
        String line = reader.readLine();
        if (line.startsWith(">"))
            line = reader.readLine();
        while (line != null) {
            line = line.trim();
            int idx = 0;
            // skip digits
            while (Character.isDigit(line.charAt(idx))) {
                idx++;
            }
            // skip white spaces
            line = line.substring(idx).replaceAll("\\s", "");
            sb.append(line);
            line = reader.readLine();
        }
        return sb.toString();
    }

    public static File[] getAllFilesInCurrentDir() {
        File folder = new File("ACE2 alignment related/ACE2 sequence");
        return folder.listFiles();
    }

    public static void compare(File file1, File file2) {
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader(file1));
            BufferedReader reader2 = new BufferedReader(new FileReader(file2));
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            String str1 = getString(reader1, sb1).toUpperCase();
            String str2 = getString(reader2, sb2).toUpperCase();
            // initializing penalties of different types
            int misMatchPenalty = 1;
            int gapPenalty = 1;
            String fileName1 = file1.getName();
            String fileName2 = file2.getName();
            getMinimumPenalty(fileName1, fileName2, str1, str2, misMatchPenalty, gapPenalty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}