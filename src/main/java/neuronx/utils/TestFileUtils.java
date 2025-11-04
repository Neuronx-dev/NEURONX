package neuronx.utils;

import neuronx.utils.FileUtils;

import java.util.*;
import java.io.*;

public class TestFileUtils {

    public static void main(String[] args) {
        System.out.println("=== 🔹 TESTING FileUtils for All ML Datasets ===\n");

        testSimpleLinearRegression();
        testMultipleLinearRegression();
        testClassification();
        testClustering();
    }

    // === Simple Linear Regression ===
    public static void testSimpleLinearRegression() {
        System.out.println("\n📘 Simple Linear Regression Dataset:");
        String path = "dataset_SLR.csv";
        List<Map<String, String>> data = FileUtils.read_csv(path);
        Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, true);
        Map<String, List<double[]>> split = FileUtils.train_test_split(xy.get("X"), xy.get("Y"), 0.3);

        printSummary(split);
    }

    // === Multiple Linear Regression ===
    public static void testMultipleLinearRegression() {
        System.out.println("\n📗 Multiple Linear Regression Dataset:");
        String path = "dataset_multiLinerRegression.csv";
        List<Map<String, String>> data = FileUtils.read_csv(path);
        Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, true);
        Map<String, List<double[]>> split = FileUtils.train_test_split(xy.get("X"), xy.get("Y"), 0.3);

        printSummary(split);
    }

    // === Classification ===
    public static void testClassification() {
        System.out.println("\n📙 Classification Dataset:");
        String path = "dataset_classificatication.csv";
        List<Map<String, String>> data = FileUtils.read_csv(path);
        Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, true);
        Map<String, List<double[]>> split = FileUtils.train_test_split(xy.get("X"), xy.get("Y"), 0.3);

        printSummary(split);
    }

    // === Clustering ===
    public static void testClustering() {
        System.out.println("\n📒 Clustering (Unsupervised) Dataset:");
        String path = "dataset_unsupervised.csv";
        List<Map<String, String>> data = FileUtils.read_csv(path);
        Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, false);
        Map<String, List<double[]>> split = FileUtils.train_test_split(xy.get("X"), xy.get("Y"), 0.3);

        printSummary(split);
    }

    // === Utility ===
    public static void printSummary(Map<String, List<double[]>> split) {
        System.out.println("Train size: " + split.get("X_train").size());
        System.out.println("Test size: " + split.get("X_test").size());
    }
}
