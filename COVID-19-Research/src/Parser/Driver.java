package Parser;

import java.io.File;

public class Driver {

    /**
     * folder containing all data
     */
    static String folderPath = "Parser/protein data";

    /**
     * main driver
     *
     * @param args
     */
    public static void main(String[] args) {
        // get the folder
        // e.g. folder = "Parser/protein data"
        File folder = new File(folderPath);
        // list all dirs under
        for (File layerFolder : folder.listFiles()) {
            // e.g. layerFolder = "Parser/protein data/virus protein"
            if (layerFolder.isDirectory()) {
                for (File typeFolder : layerFolder.listFiles()) {
                    // e.g. typeFolder = "Parser/protein data/virus protein/N protein"
                    if (typeFolder.isDirectory()) {
                        String hitTableFileName = null;
                        String alignmentFileName = null;
                        String dir = typeFolder.getPath();
                        for (File file : typeFolder.listFiles()) {
                            // e.g. file = "Parser/protein data/virus protein/N protein/*.csv"
                            if (file.getName().endsWith("csv")) {
                                hitTableFileName = file.getName();
                            } else if (file.getName().endsWith("Alignment.txt")) {
                                alignmentFileName = file.getName();
                            }
                        }
                        // path for where to store the file
                        String resultFileName = "Parser/Result/" +
                                dir.split("/")[2] +
                                "/" + dir.split("/")[3] +
                                "-" + dir.split("/")[2] +
                                ".csv";

                        // initialize an instance
                        ExtractSimilarityMatrix extractor = new ExtractSimilarityMatrix(hitTableFileName,
                                alignmentFileName, resultFileName, dir);

                        // call the method to extract one matrix and store to desired path
                        extractor.extractOneSimMatrix();
                    }
                }
            }
        }

    }
}