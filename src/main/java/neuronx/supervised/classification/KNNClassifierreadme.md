
---

# 🧠 KNN Classifier in Java (NeuronX)

A lightweight **K-Nearest Neighbors (KNN)** implementation in Java built for supervised classification tasks —
designed to work with numeric datasets (like the Iris dataset) for educational and modular ML development.

---

## 📁 Folder Structure

```
neuronx/
├── supervised/
│   └── classification/
│       └── KNNClassifier.java
└── utils/
│   └── FileUtils.java
test/
└── TestKNNWithCSV.java
dataset_classification.csv
```

---

## 🔹 1️⃣ `KNNClassifier.java`

### 🧩 Overview

This class implements the **K-Nearest Neighbors algorithm** for classification.
It predicts the label of a test sample based on the **majority class** among the `k` nearest points.

### ✳️ Key Methods

| Method                           | Description                                      |
| -------------------------------- | ------------------------------------------------ |
| `fit(X_train, Y_train)`          | Stores training samples and their labels.        |
| `predict(double[] sample)`       | Predicts the label for a single input sample.    |
| `predict(List<double[]> X_test)` | Predicts labels for a list of samples.           |
| `euclideanDistance(a, b)`        | Calculates distance between two feature vectors. |

---

### 💻 Example Code

```java
List<double[]> X_train = Arrays.asList(
    new double[]{1.0, 2.0},
    new double[]{2.0, 3.0},
    new double[]{3.0, 3.0},
    new double[]{6.0, 5.0}
);
List<String> y_train = Arrays.asList("A", "A", "B", "B");

KNNClassifier knn = new KNNClassifier(3);
knn.fit(X_train, y_train);

double[] test = {2.5, 2.7};
String result = knn.predict(test);

System.out.println("Predicted class: " + result);
```

🟢 **Output:**

```
✅ Model trained with 4 samples. (k=3)
Predicted class: A
```

---

## 🔹 2️⃣ `TestKNNWithCSV.java`

### 🧠 Purpose

Demonstrates how to train and evaluate the **KNN model** using a **CSV dataset**,
similar to how `scikit-learn` handles `fit` and `predict`.

---

### 💻 Full Example Code

```java
package test;

import java.util.*;
import neuronx.supervised.classification.KNNClassifier;
import neuronx.utils.FileUtils;

public class TestKNNWithCSV {
    public static void main(String[] args) {

        System.out.println("=== Testing KNN Classifier with CSV Dataset ===\n");

        // Load dataset
        String path = "dataset_classification.csv";
        List<Map<String, String>> data = FileUtils.read_csv(path);
        if (data == null || data.isEmpty()) {
            System.out.println("No data found! Check file path.");
            return;
        }

        // Extract features (X) and labels (Y)
        Map<String, List<double[]>> xy = FileUtils.extract_X_y(data, true);
        List<double[]> X = xy.get("X");
        List<double[]> Y_raw = xy.get("Y");

        List<String> Y = new ArrayList<>();
        for (double[] y : Y_raw) Y.add(String.valueOf((int) y[0]));

        // Split into train/test
        Map<String, List<double[]>> split = FileUtils.train_test_split(X, Y_raw, 0.3);
        List<double[]> X_train = split.get("X_train");
        List<double[]> X_test = split.get("X_test");

        List<String> Y_train = new ArrayList<>(), Y_test = new ArrayList<>();
        for (double[] y : split.get("Y_train")) Y_train.add(String.valueOf((int) y[0]));
        for (double[] y : split.get("Y_test")) Y_test.add(String.valueOf((int) y[0]));

        // Train model
        KNNClassifier knn = new KNNClassifier(3);
        knn.fit(X_train, Y_train);

        // Predict test data
        List<String> preds = knn.predict(X_test);
        int correct = 0;
        for (int i = 0; i < Y_test.size(); i++)
            if (Y_test.get(i).equals(preds.get(i))) correct++;

        double acc = (double) correct / Y_test.size() * 100;
        System.out.printf("Accuracy: %.2f%%\n", acc);

        // Predict unseen samples
        double[][] samples = {
            {5.2, 3.4, 1.5, 0.2},
            {9.9, 6.8, 8.5, 9.3},
            {10.3, 11.9, 12.6, 14.8}
        };
        for (double[] s : samples)
            System.out.println(Arrays.toString(s) + " → " + getLabel(knn.predict(s)));
    }

    // Label Mapping
    public static String getLabel(String l) {
        return switch (l) {
            case "0" -> "Setosa";
            case "1" -> "Versicolor";
            case "2" -> "Virginica";
            default -> "Unknown";
        };
    }
}
```

---

### 📊 **Expected Output**

```
=== Testing KNN Classifier with CSV Dataset ===

✅ Model trained with 105 samples. (k=3)
Accuracy: 96.67%

[5.2, 3.4, 1.5, 0.2] → Setosa
[9.9, 6.8, 8.5, 9.3] → Virginica
[10.3, 11.9, 12.6, 14.8] → Virginica
```

---

## 🔹 3️⃣ `dataset_classification.csv` Example

This dataset is inspired by the **Iris flower dataset**,
commonly used for classification problems.

| Sepal Length | Sepal Width | Petal Length | Petal Width | Label |
| ------------ | ----------- | ------------ | ----------- | ----- |
| 5.1          | 3.5         | 1.4          | 0.2         | 0     |
| 6.3          | 2.9         | 5.6          | 1.8         | 2     |
| 5.9          | 3.0         | 4.2          | 1.3         | 1     |
| 4.7          | 3.2         | 1.3          | 0.2         | 0     |
| 6.5          | 3.0         | 5.8          | 2.2         | 2     |

➡ **Label (last column):**

* `0` → Setosa 🌸
* `1` → Versicolor 🌺
* `2` → Virginica 🌼

➡ **Feature columns (1–4):**

* Sepal Length
* Sepal Width
* Petal Length
* Petal Width

---

## 🧩 How It Works Internally

1️⃣ **Distance Calculation** – Uses *Euclidean distance* to find how close a test sample is to each training sample.
2️⃣ **Sorting** – Finds the `k` smallest distances.
3️⃣ **Voting** – Takes the most frequent label among the `k` neighbors.
4️⃣ **Prediction** – Returns that majority label as output.

---

## 🧠 Changing String Values and Labels

If you want to use **custom string labels**, simply modify the mapping in:

```java
public static String getLabel(String l) {
    return switch (l) {
        case "0" -> "Apple";
        case "1" -> "Banana";
        case "2" -> "Cherry";
        default -> "Unknown";
    };
}
```

Then, update your CSV file’s last column accordingly:

```
4.5,3.0,1.3,0.2,0
5.0,3.5,1.6,0.6,1
6.2,2.8,4.8,1.8,2
```

🟢 Output Example:

```
[4.5, 3.0, 1.3, 0.2] → Apple
[5.0, 3.5, 1.6, 0.6] → Banana
[6.2, 2.8, 4.8, 1.8] → Cherry
```

---

## 🧾 Summary

| Step | Description                        |
| ---- | ---------------------------------- |
| 1️⃣  | Load dataset from CSV              |
| 2️⃣  | Extract features & labels          |
| 3️⃣  | Split into training & testing data |
| 4️⃣  | Train model using KNN              |
| 5️⃣  | Evaluate accuracy                  |
| 6️⃣  | Predict new unseen data            |
| ✅    | View results with class names      |

---

✨ **Conclusion:**
The **NeuronX KNN Classifier** provides a simple, clean, and educational implementation of one of the most intuitive machine learning algorithms — KNN — in pure Java, ready for any numeric classification dataset.
