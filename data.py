import random
import csv

# with open("dataset_unsupervised.csv", "w", newline="") as f:
#     writer = csv.writer(f)
#     for i in range(10000):
#         label = random.choice([0, 1, 2])
#         if label == 0:  
#             row = [
#                 round(random.uniform(4.3, 5.8), 1),
#                 round(random.uniform(2.5, 4.4), 1),
#                 round(random.uniform(1.0, 1.9), 1),
#                 round(random.uniform(0.1, 0.6), 1)
#             ]
#         elif label == 1:  
#             row = [
#                 round(random.uniform(5.0, 6.9), 1),
#                 round(random.uniform(2.0, 3.8), 1),
#                 round(random.uniform(3.0, 5.0), 1),
#                 round(random.uniform(1.0, 1.8), 1)
#             ]
#         elif label == 2 :
#             row = [
#                 round(random.uniform(5.5, 7.9), 1),
#                 round(random.uniform(2.5, 3.9), 1),
#                 round(random.uniform(4.5, 6.9), 1),
#                 round(random.uniform(1.8, 2.5), 1)
#             ]
#         writer.writerow(row)


import random
import csv

# --- Generate Simple Linear Regression dataset ---
with open("dataset_SLR.csv", "w", newline="") as f:
    writer = csv.writer(f)
    
    for i in range(10000):  
        x = round(random.uniform(1, 100), 2)  # Feature (X)
        noise = random.uniform(-5, 5)         # Random noise
        y = round(5 * x + 10 + noise, 2)      # Linear relation: y = 5x + 10 + noise
        writer.writerow([x, y])


import random
import csv

# --- Generate Multiple Linear Regression dataset ---
with open("dataset_multiLinerRegression.csv", "w", newline="") as f:
    writer = csv.writer(f)
    
    for i in range(10000):  # 10000 records
        x1 = round(random.uniform(1, 100), 2)
        x2 = round(random.uniform(50, 150), 2)
        x3 = round(random.uniform(100, 200), 2)
        
        noise = random.uniform(-20, 20)  # random noise
        y = round(3*x1 + 4*x2 + 2*x3 + 10 + noise, 2)
        
        writer.writerow([x1, x2, x3, y])

import random
import csv

# --- Generate Classification Dataset ---
with open("dataset_classificatication.csv", "w", newline="") as f:
    writer = csv.writer(f)
    # writer.writerow(["X1", "X2", "X3", "Label"])  # uncomment for header

    for i in range(10000):  # 150 records (50 per class)
        label = random.choice([0, 1, 2])

        if label == 0:  # Class 0 (e.g., small values cluster)
            x1 = round(random.uniform(1.0, 5.0), 2)
            x2 = round(random.uniform(1.0, 5.0), 2)
            x3 = round(random.uniform(1.0, 5.0), 2)

        elif label == 1:  # Class 1 (medium range)
            x1 = round(random.uniform(5.0, 10.0), 2)
            x2 = round(random.uniform(4.5, 9.5), 2)
            x3 = round(random.uniform(5.5, 10.5), 2)

        else:  # label == 2, Class 2 (high range)
            x1 = round(random.uniform(10.0, 15.0), 2)
            x2 = round(random.uniform(9.0, 14.0), 2)
            x3 = round(random.uniform(10.0, 15.0), 2)

        writer.writerow([x1, x2, x3, label])
