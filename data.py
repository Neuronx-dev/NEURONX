import random
import csv

with open("dataset_supervised.csv", "w", newline="") as f:
    writer = csv.writer(f)
    for i in range(500):
        label = random.choice([0, 1, 2])
        if label == 0:  
            row = [
                round(random.uniform(4.3, 5.8), 1),
                round(random.uniform(2.5, 4.4), 1),
                round(random.uniform(1.0, 1.9), 1),
                round(random.uniform(0.1, 0.6), 1),
                label,
            ]
        elif label == 1:  
            row = [
                round(random.uniform(5.0, 6.9), 1),
                round(random.uniform(2.0, 3.8), 1),
                round(random.uniform(3.0, 5.0), 1),
                round(random.uniform(1.0, 1.8), 1),
                label,
            ]
        elif label == 2 :
            row = [
                round(random.uniform(5.5, 7.9), 1),
                round(random.uniform(2.5, 3.9), 1),
                round(random.uniform(4.5, 6.9), 1),
                round(random.uniform(1.8, 2.5), 1),
                label,
            ]
        writer.writerow(row)
