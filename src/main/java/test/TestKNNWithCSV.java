package test;

import nuronx.utils.FileUtils;
import nuronx.supervised.classification.KNNClassifier;
import java.util.*;
import java.io.*;

public class TestKNNWithCSV {
    public static void main(String[] args) {

        // === 1️⃣ Load Dataset ===
        String datasetPath = "dataset_supervised.csv";
        boolean supervised = true;

        System.out.println("📂 Loading dataset from: " + datasetPath);
        List<String[]> data = FileUtils.loadCSV(datasetPath);

        if (data.isEmpty()) {
            System.out.println("❌ No data found! Check path or file format.");
            return;
        }

        // === 2️⃣ Extract X, Y ===
        Map<String, List<double[]>> xy = FileUtils.extractXY(data, supervised);
        List<double[]> X = xy.get("X");
        List<double[]> Y_array = xy.get("Y");

        // Convert Y to string labels
        List<String> Y = new ArrayList<>();
        for (double[] arr : Y_array) {
            Y.add(String.valueOf((int) arr[0]));
        }

        // === 3️⃣ Split into train and test ===
        double testSize = 0.3; // 30% test data
        Map<String, List<double[]>> split = FileUtils.trainTestSplit(X, Y_array, testSize);
        List<double[]> X_train = split.get("X_train");
        List<double[]> X_test = split.get("X_test");

        List<String> Y_train = new ArrayList<>();
        List<String> Y_test = new ArrayList<>();

        for (double[] arr : split.get("Y_train")) Y_train.add(String.valueOf((int) arr[0]));
        for (double[] arr : split.get("Y_test")) Y_test.add(String.valueOf((int) arr[0]));

        System.out.println("\n✅ Train size: " + X_train.size());
        System.out.println("✅ Test size: " + X_test.size());

        // === 4️⃣ Train Model ===
        KNNClassifier knn = new KNNClassifier(3); // K = 3
        knn.fit(X_train, Y_train);

        // === 5️⃣ Predict test samples ===
        List<String> predictions = knn.predict(X_test);

        // === 6️⃣ Evaluate ===
        int correct = 0;
        System.out.println("\n🔍 Test Predictions with Class Names:");
        for (int i = 0; i < Y_test.size(); i++) {
            String trueLabel = Y_test.get(i);
            String predLabel = predictions.get(i);

            String trueName = getLabelName(trueLabel);
            String predName = getLabelName(predLabel);

            System.out.println("🔹 Sample " + (i + 1) + ": True = " + trueName + " | Pred = " + predName);

            if (trueLabel.equals(predLabel)) correct++;
        }

        double accuracy = (double) correct / Y_test.size() * 100.0;
        System.out.printf("\n🎯 Accuracy: %.2f%%\n", accuracy);

        // === 7️⃣ Predict new unseen data ===
        double[] newSample1 = {5.2, 3.4, 1.5, 0.2};  // Setosa
        double[] newSample2 = {6.3, 2.9, 5.6, 1.8};  // Virginica
        double[] newSample3 = {5.9, 2.8, 4.5, 1.3};  // Versicolor

        String pred1 = knn.predict(newSample1);
        String pred2 = knn.predict(newSample2);
        String pred3 = knn.predict(newSample3);

        System.out.println("\n🧩 New Data Predictions:");
        System.out.println("➡ [5.2, 3.4, 1.5, 0.2] → " + getLabelName(pred1));
        System.out.println("➡ [6.3, 2.9, 5.6, 1.8] → " + getLabelName(pred2));
        System.out.println("➡ [5.9, 2.8, 4.5, 1.3] → " + getLabelName(pred3));
    }

    // === Helper function to get class name by if-else ===
    public static String getLabelName(String label) {
        if (label.equals("0")) {
            return "Setosa";
        } else if (label.equals("1")) {
            return "Versicolor";
        } else if (label.equals("2")) {
            return "Virginica";
        } else {
            return "Unknown";
        }
    }
}
