import random
from lytrax_afm import generate_valid_afm

file = open("insert_mhtrwa.sql", "w")
for i in range(1, 10_001):
    afm = generate_valid_afm()
    salary = random.randint(700, 2_500)
    file.write(f"INSERT INTO MHTRWO (ONOMA, EPONYMO, PATRONYMO, AFM, HMNIA_GENHSHS, SALARY) VALUES('Test{i}', 'Test{i}', 'Test{i}', {afm}, CURRENT_DATE, {salary});\n")
