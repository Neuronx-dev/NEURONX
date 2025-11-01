package nuronx.utils;

import java.io.*;
import java.util.*;

public class FileUtils {

    // === Load CSV file ===
    public static List<String[]> loadCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                data.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
        return data;
    }

    // === Extract X and Y ===
    // supervised = true → last column is Y
    public static Map<String, List<double[]>> extractXY(List<String[]> data, boolean supervised) {
        Map<String, List<double[]>> result = new HashMap<>();
        List<double[]> X = new ArrayList<>();
        List<double[]> Y = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            System.out.println("No data loaded!");
            result.put("X", X);
            result.put("Y", Y);
            return result;
        }

        int nFeatures = supervised ? data.get(0).length - 1 : data.get(0).length;

        for (String[] row : data) {
            double[] features = new double[nFeatures];
            for (int i = 0; i < nFeatures; i++)
                features[i] = Double.parseDouble(row[i]);
            X.add(features);

            if (supervised) {
                double[] label = new double[1];
                label[0] = Double.parseDouble(row[row.length - 1]);
                Y.add(label);
            }
        }

        result.put("X", X);
        result.put("Y", Y);
        return result;
    }

    // === Train/Test Split ===
    // Works for both supervised and unsupervised
    public static Map<String, List<double[]>> trainTestSplit(List<double[]> X, List<double[]> Y, double testSize) {
        int total = X.size();
        int testCount = (int) (total * testSize);

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < total; i++) indices.add(i);
        Collections.shuffle(indices, new Random());

        List<double[]> X_train = new ArrayList<>();
        List<double[]> X_test = new ArrayList<>();
        List<double[]> Y_train = new ArrayList<>();
        List<double[]> Y_test = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            int idx = indices.get(i);
            if (i < testCount) {
                X_test.add(X.get(idx));
                if (Y != null && !Y.isEmpty()) Y_test.add(Y.get(idx));
            } else {
                X_train.add(X.get(idx));
                if (Y != null && !Y.isEmpty()) Y_train.add(Y.get(idx));
            }
        }

        Map<String, List<double[]>> split = new HashMap<>();
        split.put("X_train", X_train);
        split.put("X_test", X_test);
        split.put("Y_train", Y_train);
        split.put("Y_test", Y_test);

        return split;
    }

    // === MAIN for Testing ===
    public static void main(String[] args) {
        // 🔹 Change this file name depending on dataset
        String path = "dataset_supervised.csv";

        // If you’re testing supervised CSV, set this true
        boolean supervised = true;

        List<String[]> data = loadCSV(path);
        Map<String, List<double[]>> xy = extractXY(data, supervised);
        List<double[]> X = xy.get("X");
        List<double[]> Y = xy.get("Y");

        Map<String, List<double[]>> split = trainTestSplit(X, Y, 0.3);

        System.out.println("=== DATA SPLIT SUMMARY ===");
        System.out.println("Train size: " + split.get("X_train").size());
        System.out.println("Test size: " + split.get("X_test").size());

        System.out.println("\n=== TRAIN DATA ===");
        for (int i = 0; i < split.get("X_train").size(); i++) {
            double[] x = split.get("X_train").get(i);
            System.out.print("X_train[" + i + "]: ");
            for (double val : x) System.out.print(val + " ");
            if (supervised && !split.get("Y_train").isEmpty())
                System.out.println("| Y_train: " + split.get("Y_train").get(i)[0]);
            else
                System.out.println();
        }

        System.out.println("\n=== TEST DATA ===");
        for (int i = 0; i < split.get("X_test").size(); i++) {
            double[] x = split.get("X_test").get(i);
            System.out.print("X_test[" + i + "]: ");
            for (double val : x) System.out.print(val + " ");
            if (supervised && !split.get("Y_test").isEmpty())
                System.out.println("| Y_test: " + split.get("Y_test").get(i)[0]);
            else
                System.out.println();
        }
    }
}
