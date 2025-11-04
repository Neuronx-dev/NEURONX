package test;

import java.util.*;
import neuronx.supervised.classification.KNNClassifier;
import neuronx.utils.FileUtils;

public class TestKNNWithCSV {
    public static void main(String[] args) {

        System.out.println("=== Testing KNN Classifier with CSV Dataset ===\n");

        // 1️⃣ Load Dataset
        String path = "dataset_classificatication.csv";
        List<Map<String, String>> data = FileUtils.read_csv(path);
        if (data == null || data.isEmpty()) {
            System.out.println("No data found! Check file path.");
            return;
        }

        // 2️⃣ Extract Features (X) and Labels (Y)
        Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, true);
        List<double[]> X = xy.get("X");
        List<double[]> Y_raw = xy.get("Y");

        List<String> Y = new ArrayList<>();
        for (double[] y : Y_raw) Y.add(String.valueOf((int) y[0]));

        // 3️⃣ Split into Train/Test
        Map<String, List<double[]>> split = FileUtils.train_test_split(X, Y_raw, 0.3);
        List<double[]> X_train = split.get("X_train");
        List<double[]> X_test = split.get("X_test");

        List<String> Y_train = new ArrayList<>(), Y_test = new ArrayList<>();
        for (double[] y : split.get("Y_train")) Y_train.add(String.valueOf((int) y[0]));
        for (double[] y : split.get("Y_test")) Y_test.add(String.valueOf((int) y[0]));

        // 4️⃣ Train Model
        KNNClassifier knn = new KNNClassifier(3);
        knn.fit(X_train, Y_train);

        // 5️⃣ Predict and Evaluate
        List<String> preds = knn.predict(X_test);
        int correct = 0;
        for (int i = 0; i < Y_test.size(); i++)
            if (Y_test.get(i).equals(preds.get(i))) correct++;

        double acc = (double) correct / Y_test.size() * 100;
        System.out.printf("Accuracy: %.2f%%\n", acc);

        // 6️⃣ Predict New Data
        double[][] samples = {
            {5.2, 3.4, 1.5, 0.2},
            {9.9, 6.8, 8.5, 9.3},
            {10.3, 11.9, 12.6, 14.8}
        };
        for (double[] s : samples)
            System.out.println(Arrays.toString(s) + " → " + getLabel(knn.predict(s)));
    }

    // Helper: map label → name
    public static String getLabel(String l) {
        return switch (l) {
            case "0" -> "Setosa";
            case "1" -> "Versicolor";
            case "2" -> "Virginica";
            default -> "Unknown";
        };
    }
}
