import random


file = open("insert_mhtrwa.sql", "w")
afms = random.sample(range(111_111_111, 1_000_000_000), 10_000)
for i, afm in enumerate(afms):
    i = i + 1
    salary = random.randint(700, 2_500)
    file.write(f"INSERT INTO MHTRWO (ONOMA, EPONYMO, PATRONYMO, AFM, HMNIA, SALARY) VALUES('Test{i}', 'Test{i}', 'Test{i}', {afm}, CURRENT_DATE, {salary});\n")
