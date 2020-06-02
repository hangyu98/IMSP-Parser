import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindInsertion {
    public static void main(String[] args) {
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader("Find-PRRA/HIV-GP160.txt"));
            StringBuilder sb1 = new StringBuilder();
            String str1 = GlobalAlignment.getString(reader1, sb1);
            // Create a pattern to be searched
            System.out.println("Pattern to search for: " + "RXRR | RRXR | PRRA");
            System.out.println("Total range: 0 - " + str1.length());
            Pattern pattern = Pattern.compile("R.RR|RR.R|prra|rr.r|r.rr|RR");
            Matcher m = pattern.matcher(str1);

            // Print starting and ending indexes of the pattern
            // in text
            int i = 1;
            while (m.find()) {
                System.out.println("#" + i);
                int start = m.start();
                int end = m.end();
                System.out.println("Pattern found from " + start+
                        " to " + (end - 1));
                System.out.println("Pattern is: " + str1.substring(start, end));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}