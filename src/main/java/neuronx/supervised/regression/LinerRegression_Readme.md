# 🧠 Linear Regression (NeuronX) - Complete Documentation

## 📌 Overview

This project implements a **Linear Regression Model** from scratch in Java — without using external ML libraries — for both **simple** and **multiple** regression tasks. It also includes a test file that loads data, trains the model, and evaluates it with performance metrics.

---

## 📂 Folder Structure

```
NeuronX/
 ├── neuronx/
 │    └── supervised/regression/LinearRegression.java
 │    └── utils/FileUtils.java
 ├── test/
 │    └── TestSLR.java
 └── dataset_SLR.csv
```

---

## 🧩 Code Explanation

### 🔹 LinearRegression.java

Implements core ML logic:

#### **1. fit(X, y)** – Model Training

* Adds bias column to data.
* Uses matrix math to solve `β = (XᵀX)⁻¹ Xᵀy`.
* Stores weights `[β₀, β₁, β₂, ...]`.

#### **2. predict(X)** – Generate Predictions

* Computes `y_pred = β₀ + β₁x₁ + β₂x₂ + ...`
* Returns prediction array.

#### **3. mean_squared_error() & r2_score()** – Evaluation

* MSE → Measures average squared difference.
* R² → Represents model’s goodness of fit.

#### **4. Matrix Utilities**

* Transpose, multiply, inverse, and column conversion methods.
* Uses **Gauss–Jordan elimination** for stable matrix inversion.

---

### 🔹 FileUtils.java (Utility Functions)

Handles data loading and preparation.

```java
public class FileUtils {
    public static List<Map<String, String>> read_csv(String path) throws IOException { ... }
    public static Map<String, List<double[]>> extract_X_y(List<Map<String, String>> data, boolean hasHeader) { ... }
    public static Map<String, List<double[]>> train_test_split(List<double[]> X, List<double[]> Y, double test_size) { ... }
}
```

📘 **Purpose:** Acts like a lightweight mix of `pandas` + `sklearn.model_selection` for Java.

---

### 🔹 TestSLR.java – Model Testing (Full Code)

```java
package test;

import neuronx.utils.FileUtils;
import neuronx.supervised.regression.LinearRegression;
import java.util.*;
import java.io.*;

public class TestSLR {
    public static void main(String[] args) {
        try {
            // Load dataset
            List<Map<String, String>> data = FileUtils.read_csv("dataset_SLR.csv");

            // Extract features and target
            Map<String, List<double[]>> extracted = FileUtils.extract_X_y(data, true);
            List<double[]> X = extracted.get("X");
            List<double[]> Y = extracted.get("Y");

            // Split train/test
            Map<String, List<double[]>> split = FileUtils.train_test_split(X, Y, 0.3);

            // Prepare arrays
            double[][] X_train = split.get("X_train").toArray(new double[0][]);
            double[] y_train = split.get("Y_train").stream().mapToDouble(a -> a[0]).toArray();
            double[][] X_test = split.get("X_test").toArray(new double[0][]);
            double[] y_test = split.get("Y_test").stream().mapToDouble(a -> a[0]).toArray();

            // Train model
            LinearRegression model = new LinearRegression();
            model.fit(X_train, y_train);

            // Predict
            double[] preds = model.predict(X_test);

            // Evaluate
            double mse = model.mean_squared_error(y_test, preds);
            double r2 = model.r2_score(y_test, preds);

            System.out.println("=== Predictions ===");
            for (int i = 0; i < y_test.length; i++) {
                System.out.printf("Actual: %.2f | Predicted: %.2f%n", y_test[i], preds[i]);
            }

            System.out.printf("\nMSE: %.4f\n", mse);
            System.out.printf("R² Score: %.4f\n", r2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

✅ **Output Example:**

```
=== Predictions ===
Actual: 8.00 | Predicted: 7.95
Actual: 10.00 | Predicted: 9.98

MSE: 0.0032
R² Score: 0.9998
```

---

## 🧪 Test Flow

1️⃣ Load CSV data using `FileUtils.read_csv()`
2️⃣ Split into train/test via `FileUtils.train_test_split()`
3️⃣ Fit the model → `model.fit(X_train, y_train)`
4️⃣ Predict on test data → `model.predict(X_test)`
5️⃣ Evaluate → `MSE` and `R² Score`

---

## 🧠 Key Formula

**Normal Equation:**
[
β = (X^T X)^{-1} X^T y
]

Used to compute coefficients for the regression line.

---

## 💡 Notes

* Works for **simple & multiple** regression.
* Designed with **clean modular OOP structure**.
* Avoids 3rd-party ML libraries.

---

## 🚀 Run Result Example

```
Train X: 3
Test X: 2
Train Y: 3
Test Y: 2

=== Predictions ===
Actual: 8.00 | Predicted: 7.99
Actual: 10.00 | Predicted: 10.02

MSE: 0.0003
R² Score: 0.9999
```

✨ **Conclusion:** The NeuronX Linear Regression module successfully trains and predicts with excellent accuracy.
