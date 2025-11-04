
---

# 📘 FileUtils – CSV & JSON Dataset Utility for ML (Java)

### 📦 Package

```java
package neuronx.utils;
```

---

## 🌟 Overview

`FileUtils` is a **single-class Java utility** that provides dataset preprocessing functions similar to Python’s `pandas` and `scikit-learn`.
It supports:

✅ Reading **CSV** or **JSON** datasets
✅ Extracting **features (X)** and **labels (Y)**
✅ Splitting data into **train/test sets**
✅ Saving processed data back to CSV

All methods are **static**, meaning you can call them directly without creating any object.

---

## 🧩 File Overview

| File                 | Purpose                                                            |
| -------------------- | ------------------------------------------------------------------ |
| `FileUtils.java`     | Contains all data preprocessing utilities                          |
| `TestFileUtils.java` | Runs tests for regression, classification, and clustering datasets |

---

## 🧠 How It Works (Architecture)

```
┌──────────────────────────────────────────────────────────┐
│                        FileUtils                         │
│----------------------------------------------------------│
│  1. read_csv()         → Load CSV file                  │
│  2. read_json()        → Load JSON file                 │
│  3. extract_X_y()      → Extract features & labels       │
│  4. train_test_split() → Split dataset into train/test   │
│  5. to_csv()           → Save processed data to CSV      │
└──────────────────────────────────────────────────────────┘
```

---

## ⚙️ Features Table

| Method                                                                   | Description                                      | Works For               |
| ------------------------------------------------------------------------ | ------------------------------------------------ | ----------------------- |
| `read_csv(String filePath)`                                              | Reads dataset from CSV file                      | All                     |
| `read_json(String filePath)`                                             | Reads dataset from JSON file                     | All                     |
| `extract_X_y(List<Map<String,String>> data, boolean supervised)`         | Extracts feature matrix (X) and label vector (Y) | Supervised/Unsupervised |
| `train_test_split(List<double[]> X, List<double[]> Y, double test_size)` | Randomly splits into training/testing data       | All                     |
| `to_csv(List<Map<String,String>> data, String filePath)`                 | Saves data as CSV                                | All                     |

---

# 🧩 STEP-BY-STEP EXPLANATION WITH CODE

---

## 🪶 1️⃣ Read CSV File

### 📄 Code:

```java
public static List<Map<String, String>> read_csv(String filePath) {
    List<Map<String, String>> data = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        List<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null)
            if (!line.trim().isEmpty()) lines.add(line);

        if (lines.isEmpty()) return data;

        String[] headers = lines.get(0).split(",");
        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(",");
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < headers.length; j++) {
                String key = headers[j].trim();
                String val = j < values.length ? values[j].trim() : "";
                row.put(key, val);
            }
            data.add(row);
        }
    } catch (IOException e) {
        System.out.println("Error reading CSV: " + e.getMessage());
    }
    return data;
}
```

### 📘 Example Input: `dataset_SLR.csv`

```csv
X,Y
1,2
2,4
3,6
4,8
```

### 📤 Output:

```text
[{X=1, Y=2}, {X=2, Y=4}, {X=3, Y=6}, {X=4, Y=8}]
```

**Explanation:**
Each CSV row is converted into a `Map<String, String>` where **column names** are keys and **cell values** are string values.

---

## 🪶 2️⃣ Read JSON File

### 📄 Code:

```java
public static List<Map<String, String>> read_json(String filePath) {
    List<Map<String, String>> data = new ArrayList<>();
    StringBuilder json = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null)
            json.append(line.trim());
    } catch (IOException e) {
        System.out.println("Error reading JSON: " + e.getMessage());
    }

    String raw = json.toString().replace("[", "").replace("]", "");
    String[] objs = raw.split("\\},\\{");

    for (String obj : objs) {
        obj = obj.replace("{", "").replace("}", "");
        String[] pairs = obj.split(",");
        Map<String, String> map = new LinkedHashMap<>();
        for (String pair : pairs) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String key = kv[0].replaceAll("\"", "").trim();
                String val = kv[1].replaceAll("\"", "").trim();
                map.put(key, val);
            }
        }
        data.add(map);
    }
    return data;
}
```

### 📘 Input: `data.json`

```json
[
 {"X": "1", "Y": "2"},
 {"X": "2", "Y": "4"}
]
```

### 📤 Output:

```text
[{X=1, Y=2}, {X=2, Y=4}]
```

---

## 🪶 3️⃣ Extract Features (X) and Labels (Y)

### 📄 Code:

```java
public static Map<String, List<double[]>> extract_X_y(List<Map<String, String>> data, boolean supervised)
```

This function converts your dataset map into **numeric arrays**.

* If `supervised = true`:
  → last column is treated as **Y** (target label).
* If `supervised = false`:
  → all columns are treated as **X**.

### 📘 Input:

```text
[{X1=1, X2=2, Y=5}, {X1=2, X2=3, Y=7}]
```

### 📤 Output:

```text
X = [[1, 2], [2, 3]]
Y = [[5], [7]]
```

**Explanation:**

* For supervised data, the **last column** is separated as labels.
* For unsupervised data, Y is empty.

---

## 🪶 4️⃣ Train/Test Split

### 📄 Code:

```java
public static Map<String, List<double[]>> train_test_split(
        List<double[]> X, List<double[]> Y, double test_size)
```

This randomly splits your dataset into training and testing subsets.

### 📘 Input:

```
X = [[1], [2], [3], [4]]
Y = [[2], [4], [6], [8]]
test_size = 0.25
```

### 📤 Output:

```
X_train = [[2], [4], [3]]
Y_train = [[4], [8], [6]]
X_test = [[1]]
Y_test = [[2]]
```

**Explanation:**

* Randomly shuffles indices
* 25% of the data → test set
* 75% → training set

---

## 🪶 5️⃣ Save to CSV

### 📄 Code:

```java
public static void to_csv(List<Map<String, String>> data, String filePath)
```

### 📘 Input:

```java
List<Map<String, String>> data = Arrays.asList(
  Map.of("X", "1", "Y", "2"),
  Map.of("X", "2", "Y", "4")
);
FileUtils.to_csv(data, "output.csv");
```

### 📤 Output File (`output.csv`):

```
X,Y
1,2
2,4
```

---

# 🧪 Testing File – `TestFileUtils.java`

### 📦 Package

```java
package neuronx.utils;
```

---

## 🧩 Full Code

```java
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
        String path = "dataset_classification.csv";
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

    public static void printSummary(Map<String, List<double[]>> split) {
        System.out.println("Train size: " + split.get("X_train").size());
        System.out.println("Test size: " + split.get("X_test").size());
    }
}
```

---

## 🧾 Sample Dataset Files

### 1️⃣ `dataset_SLR.csv`

```csv
X,Y
1,2
2,4
3,6
4,8
5,10
```

### 2️⃣ `dataset_multiLinerRegression.csv`

```csv
X1,X2,Y
1,2,5
2,1,4
3,5,9
4,3,8
```

### 3️⃣ `dataset_classification.csv`

```csv
F1,F2,Label
5.1,3.5,0
4.9,3.0,0
6.2,3.4,1
5.9,3.0,1
```

### 4️⃣ `dataset_unsupervised.csv`

```csv
X1,X2
1.2,3.4
2.3,4.5
3.1,2.9
5.0,6.1
```

---

## 🧪 Example Output (Terminal)

```
=== 🔹 TESTING FileUtils for All ML Datasets ===


📘 Simple Linear Regression Dataset:
Train size: 3
Test size: 2

📗 Multiple Linear Regression Dataset:
Train size: 3
Test size: 1

📙 Classification Dataset:
Train size: 3
Test size: 1

📒 Clustering (Unsupervised) Dataset:
Train size: 3
Test size: 1
```

---

## 🧱 Folder Structure

```
project/
│
├── src/
│   └── neuronx/
│       └── utils/
│           ├── FileUtils.java
│           └── TestFileUtils.java
│
├── dataset_SLR.csv
├── dataset_multiLinerRegression.csv
├── dataset_classification.csv
└── dataset_unsupervised.csv
```

---

## 🧭 How to Run

### 🔹 Compile

```bash
javac src/neuronx/utils/FileUtils.java src/neuronx/utils/TestFileUtils.java
```

### 🔹 Run

```bash
java -cp src neuronx.utils.TestFileUtils
```

---

## 🧩 Summary

| Step | Operation     | Description                 |
| ---- | ------------- | --------------------------- |
| 1️⃣  | Load CSV/JSON | Reads dataset into memory   |
| 2️⃣  | Extract X, Y  | Separates features & target |
| 3️⃣  | Split         | Divides into train/test     |
| 4️⃣  | Save          | Writes data to CSV          |
| 5️⃣  | Test          | Confirms all functionality  |

---
