import csv

with open("enterprise-numbers") as f:
    content = f.readlines()

rows = [0]
k = 1
for i in content:
    # line by line
    if k % 4 == 0:
        rows.append(i.strip())
    if (k - 1) % 4 == 0:
        rows[-1] = [rows[-1], i.strip()]
    k += 1

print(rows)

with open('enterprise_numbers.csv', mode='w') as numberFile:
    employee_writer = csv.writer(numberFile, delimiter=';')

    for row in rows:
        employee_writer.writerow(row)
