
---

# 📘 FileUtils – CSV Loader & Dataset Splitter (Java Utility)

### 📦 Package

```java
package neuronx.utils;
```

---

## 🌟 Overview

`FileUtils` is a **lightweight utility class** that provides:

* **CSV file loading** (any dataset)
* **Feature (X) and Label (Y) extraction**
* **Train/Test splitting**
* Works for **both supervised and unsupervised** learning datasets
* All methods are **static** → can be used directly **without creating objects**

This class is useful for any **Java ML project**, where you want to handle datasets like in Python’s `scikit-learn`.

---

## ⚙️ Features

| Function                                                              | Description                                                    | Works For |
| --------------------------------------------------------------------- | -------------------------------------------------------------- | --------- |
| `loadCSV(String filePath)`                                            | Loads a CSV file and returns data as a list of string arrays   | Both      |
| `extractXY(List<String[]> data, boolean supervised)`                  | Separates features (X) and labels (Y) if dataset is supervised | Both      |
| `trainTestSplit(List<double[]> X, List<double[]> Y, double testSize)` | Splits dataset into training and testing sets                  | Both      |

---

## 🧠 How It Works

### 🔹 1. Supervised Dataset

Used when your dataset has **features + target** (last column = label).

**Example:**

```csv
5.1,3.5,1.4,0.2,0
4.9,3.0,1.4,0.2,0
6.2,3.4,5.4,2.3,1
5.9,3.0,5.1,1.8,1
```

➡ Last column (`0` or `1`) = Y (target)
➡ Other columns = X (features)

### 🔹 2. Unsupervised Dataset

Used when your dataset **has no labels** (only features).

**Example:**

```csv
5.1,3.5,1.4,0.2
4.9,3.0,1.4,0.2
6.2,3.4,5.4,2.3
5.9,3.0,5.1,1.8
```

➡ All columns = X (features)
➡ No Y (target)

---

## 💻 Example Usage

### 🧩 Step 1 — Import the class

```java
import neuronx.utils.FileUtils;
import java.util.*;
```

---

### 🧩 Step 2 — Load dataset

```java
List<String[]> data = FileUtils.loadCSV("dataset_supervised.csv");
```

---

### 🧩 Step 3 — Extract features and labels

For **supervised**:

```java
boolean supervised = true;
Map<String, List<double[]>> xy = FileUtils.extractXY(data, supervised);
List<double[]> X = xy.get("X");
List<double[]> Y = xy.get("Y");
```

For **unsupervised**:

```java
boolean supervised = false;
Map<String, List<double[]>> xy = FileUtils.extractXY(data, supervised);
List<double[]> X = xy.get("X");   // features only
List<double[]> Y = xy.get("Y");   // will be empty
```

---

### 🧩 Step 4 — Split into training and testing data

```java
Map<String, List<double[]>> split = FileUtils.trainTestSplit(X, Y, 0.3);
```

It returns:

```java
split.get("X_train");
split.get("Y_train");
split.get("X_test");
split.get("Y_test");
```

---

### 🧩 Step 5 — Example: Printing results

```java
System.out.println("Train size: " + split.get("X_train").size());
System.out.println("Test size: " + split.get("X_test").size());
```

---

## 🧪 Example Output

```
=== DATA SPLIT SUMMARY ===
Train size: 3
Test size: 1

=== TRAIN DATA ===
X_train[0]: 5.1 3.5 1.4 0.2 | Y_train: 0
X_train[1]: 4.9 3.0 1.4 0.2 | Y_train: 0
X_train[2]: 6.2 3.4 5.4 2.3 | Y_train: 1

=== TEST DATA ===
X_test[0]: 5.9 3.0 5.1 1.8 | Y_test: 1
```

---

## 🧩 Folder Structure Example

```
project/
│
├── src/
│   └── neuronx/
│       └── utils/
│           └── FileUtils.java
│
├── dataset_supervised.csv
└── Main.java
```

---

## 🪶 Example Integration in Another Project

**In your ML project**, you can directly use it for preprocessing:

```java
import neuronx.utils.FileUtils;
import java.util.*;

public class KNNExample {
    public static void main(String[] args) {
        List<String[]> data = FileUtils.loadCSV("iris.csv");
        Map<String, List<double[]>> xy = FileUtils.extractXY(data, true);

        List<double[]> X = xy.get("X");
        List<double[]> Y = xy.get("Y");

        Map<String, List<double[]>> split = FileUtils.trainTestSplit(X, Y, 0.2);

        // Now pass to your ML model
        List<double[]> X_train = split.get("X_train");
        List<double[]> Y_train = split.get("Y_train");
    }
}
```

---

## ⚠️ Notes

* Works with numeric datasets only.
* CSV must be **comma-separated (no headers)**.
* All functions are **static**, no object creation needed.
* Suitable for small to medium datasets.

---

## 🧰 Example Dataset (for testing)

Save as `dataset_supervised.csv`:

```csv
5.1,3.5,1.4,0.2,0
4.9,3.0,1.4,0.2,0
6.2,3.4,5.4,2.3,1
5.9,3.0,5.1,1.8,1
6.0,2.2,4.0,1.0,1
```

---
