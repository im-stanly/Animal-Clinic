begin;
DROP TABLE IF EXISTS Prescriptions CASCADE;
DROP TABLE IF EXISTS Visits CASCADE;
DROP TABLE IF EXISTS Vaxx CASCADE;
DROP TABLE IF EXISTS Vets_Specialities CASCADE;
DROP TABLE IF EXISTS Holidays CASCADE;
DROP TABLE IF EXISTS WorkHours CASCADE;
DROP TABLE IF EXISTS Pets CASCADE;
DROP TABLE IF EXISTS Vets CASCADE;
DROP TABLE IF EXISTS Employees CASCADE;
DROP TABLE IF EXISTS Persons CASCADE;
DROP TABLE IF EXISTS Medicine CASCADE;
DROP TABLE IF EXISTS Accounts CASCADE;

CREATE TABLE Persons (
    id          SERIAL          PRIMARY KEY,
    first_name  VARCHAR(30)     NOT NULL,
    last_name   VARCHAR(50)     NOT NULL,
    address     VARCHAR(255),
    city        VARCHAR(255),
    telephone   VARCHAR(20),
    email       VARCHAR(255)
);

CREATE TABLE Employees (
    id          SERIAL          PRIMARY KEY,
    person_id   INTEGER         REFERENCES Persons(id) NOT NULL,
    position    VARCHAR(255)    NOT NULL,
    salary      NUMERIC(10, 2)  NOT NULL,
    date_start  DATE            NOT NULL CHECK(date_start < date_fire),
    date_fire   DATE,
    fav_animal  VARCHAR(255)
);

CREATE TABLE Vets (
    id          INTEGER         PRIMARY KEY REFERENCES Employees(id) NOT NULL UNIQUE,
    office      VARCHAR(255)
);

CREATE TABLE Pets (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    sex         CHAR(1)         CHECK(sex = 'M' or sex = 'F'),
    type        VARCHAR(255)    NOT NULL,
    race        VARCHAR(255),
    birth_day   DATE,
    person_id   INTEGER         REFERENCES Persons(id) NOT NULL,
    weight      NUMERIC(10, 2),
    dangerous   BOOLEAN,
    estimate    BOOLEAN
);

CREATE TABLE Medicine (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    company     VARCHAR(255)    NOT NULL,
    type        VARCHAR(255)    NOT NULL
);

CREATE TABLE Visits (
    id          SERIAL          PRIMARY KEY,
    pet_id      INTEGER         REFERENCES Pets(id) NOT NULL,
    vet_id      INTEGER         REFERENCES Vets(id) NOT NULL,
    visit_date  DATE            NOT NULL,
    description VARCHAR(255),
    rate        NUMERIC(3, 1),
    price       NUMERIC(10, 2)  NOT NULL
);

CREATE TABLE Prescriptions (
    id_visit    INTEGER         REFERENCES Visits(id) NOT NULL,
    med_id      INTEGER         REFERENCES Medicine(id) NOT NULL,
    amount      INTEGER         NOT NULL,
    price       NUMERIC(10, 2)  NOT NULL,
    dosing      VARCHAR(255)
);

CREATE TABLE Holidays (
    employee_id   INTEGER       REFERENCES Employees(id) NOT NULL,
    start_date  DATE            CHECK(start_date  < end_date) NOT NULL,
    end_date    DATE
);

CREATE TABLE WorkHours (
    employee_id INTEGER         REFERENCES Employees(id) NOT NULL,
    "WeekDay"   VARCHAR(10)     CHECK("WeekDay" IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')),
    start_time  TIME            CHECK(start_time < end_time) NOT NULL,
    end_time    TIME            NOT NULL
);

CREATE TABLE Vaxx (
    id          SERIAL          PRIMARY KEY,
    pet_id      INTEGER         REFERENCES Pets(id),
    period_start DATE           CHECK(period_start <= period_end) NOT NULL,
    period_end  DATE            NOT NULL,
    done        BOOLEAN         DEFAULT FALSE,
    type        VARCHAR(255)
);

CREATE TABLE Vets_Specialities (
    vet_id      INTEGER         REFERENCES Vets(id),
    name        VARCHAR(255)    NOT NULL,
    date_start  DATE            NOT NULL
);

CREATE TABLE Accounts (
    id          SERIAL          PRIMARY KEY,
    email       VARCHAR(255)    NOT NULL UNIQUE REFERENCES Accounts (email),
    username    VARCHAR(50)     NOT NULL,
    password    VARCHAR(255)    NOT NULL,
    user_permissions VARCHAR(255)
);

COPY persons (first_name, last_name, address, city, telephone, email) FROM stdin (Delimiter ',');
David,O Connor,65 Hudson St,Dulford,070218230685,daveoc@gmail.com
Elisabeth,Kowalski,79 Holgate Rd,Rannoch School,07789721317,LizSmith743@yahoo.com
Rishi,Sunak,10 Downing St,London,02072195437,rishi.sunak.mp@parliament.uk
Barbara,Richards,90 Balsham St,Harrogate,07017505023,barbararichards1937@gmail.com
Boris, Jansen,52 Holgate Rd, Rainham, 07924955449, bojoforpm@gmail.com
Mahava, Punja,7 Church Way,Bradfield,07751675751,mahpun@yahoo.com
Bernard, Rodriguez,74 Great North Road,Alticry,07025371694,bendriguez17@yahoo.com
Barry, British,24 St Andrews Lane,Dail Mor,07887136485,scoresamgowls@gmail.com
Anna, Buchmann,3 Town Lane, Sourton,07009283724,buchmanna@gmail.com
Daniele, Liberman,90 Mounthoollie Lane,Sutton,07979163114,danieleliberman342@gmail.com
\.


COPY pets (name, sex, type, race, birth_day, person_id, weight, dangerous, estimate) FROM stdin DELIMITER ',' CSV;
Bobby,M,cat,balinese, 2017-09-08, 3, 12.6, 0, 1
Don Juan III Pellegrini,M,dog,poodle, 2022-01-17, 1, 65.3, 1, 1
Katrina,F,frog,Madagascartomato, 2019-04-02, 7, 0.47, 1, 0
Joe Biden,M,dog,mongrel, 2015-02-02, 5, 78.73, 0, 1
Gienio,M,penguin,emperor, 2017-11-07, 7, 72.27, 0, 1
The Great One,F,hamster,syrian, 2021-07-30, 1, 0.36, 1, 0
Donatello Davide,M,lizard,gecko, 2012-12-25, 8, 0.12, 0, 0
Toastie,F,cockatoo,umbrella, 2001-09-12, 8, 10, 0, 0
\.

COPY vaxx (type, pet_id, period_start, period_end, done) FROM stdin (Delimiter ',');
cat flu, 8, 2022-09-12, 2022-10-12, 1
rabies, 4, 2022-11-13, 2023-01-13, 1
polyomavirus, 8, 2023-01-01, 2023-01-15, 1
rabies, 2, 2023-03-05, 2023-05-05, 0
feline calcivirus, 1, 2023-04-17, 2023-06-17, 1
\.

COPY Employees(person_id, position, salary, date_start, fav_animal) FROM stdin (Delimiter ',');
2,vet, 37800, 2019-02-01,Bobby
1,vet, 47100, 2019-03-01,Dog
6,vet, 42600, 2021-08-01,\N
3,vet, 53000, 2022-09-01,Joe Biden
9,receptionist, 30000, 2019-02-01,\N
7,cleaning, 27000, 2019-02-01,\N
5,vet, 42000, 2019-04-01,2020_05_17
\.

COPY Holidays(employee_id, start_date, end_date) FROM stdin (Delimiter ',');
1, 2021-07-01, 2021-07-15
2, 2021-08-04, 2021-08-16
1, 2022-11-23, 2022-12-01
4, 2022-07-16, 2022-07-23
5, 2022-08-24, 2022-09-06
\.

COPY Medicine(name, company, type) FROM stdin (Delimiter ',');
Loperamide,Imodium,pills
Mickeline,Wisney,gel
Yogobogo,Mustel,spray
Ratelidium,Sedlin,pills
\.

COPY Vets(id, office) FROM stdin (Delimiter ',');
1, 105
2, 106
3, 107
4, 108
\.

COPY Vets_specialities(vet_id, name, date_start) FROM stdin (Delimiter ',');
1,Dermatology, 2002-09-01
1,Cardiology, 2003-10-17
1,Nutrition, 2014-05-05
2,Oncology, 2018-09-07
2,Equine, 1998-04-01
3,Zoological medicine, 1999-08-18
3,Feline medicine, 1999-08-18
3,Dentistry, 2002-01-21
4,Animal welfare, 1978-10-13
4,Emergency and critical care, 1984-07-01
\.

COPY Visits(pet_id, vet_id, visit_date, description, rate, price) FROM stdin (Delimiter ',');
8, 1, 2022-09-27,cat flu vaccine administration, 10, 10
4, 3, 2022-12-21,rabies vaccine administration, 8.5, 30
8, 1, 2023-01-04,polyomavirus vaccine administration, 9.5, 25
6, 2, 2023-02-18,leg amputation, 9, 75
2, 4, 2023-03-01,routine check-up, 8, 15
1, 3, 2023-06-02,feline calcivirus vaccine administration, 10, 0
5, 4, 2023-06-18,routine check-up, 10, 42
\.

COPY Prescriptions (id_visit, med_id, amount, price, dosing) FROM stdin (Delimiter ',');
5, 1, 40, 7, 2
5, 2, 20, 8, 1
5, 3, 16, 12, 1
\.

COPY workHours(employee_id, "WeekDay", start_time, end_time) FROM stdin (Delimiter '|');
1|Monday|8:00:00|13:00:00
1|Wednesday|8:00:00|13:00:00
1|Thursday|8:00:00|13:00:00
1|Saturday|8:00:00|11:00:00
2|Monday|12:00:00|16:00:00
2|Tuesday|12:00:00|16:00:00
2|Wednesday|12:00:00|16:00:00
2|Thursday|8:00:00|16:00:00
2|Friday|12:00:00|16:00:00
3|Tuesday|8:00:00|13:00:00
3|Wednesday|9:00:00|13:00:00
3|Friday|8:00:00|16:00:00
4|Tuesday|9:00:00|14:00:00
4|Wednesday|9:00:00|14:00:00
4|Thursday|9:00:00|14:00:00
4|Saturday|11:00:00|13:00:00
5|Monday|8:00:00|16:00:00
5|Monday|15:00:00|17:00:00
5|Tuesday|15:00:00|17:00:00
5|Wednesday|15:00:00|17:00:00
5|Thursday|15:00:00|17:00:00
5|Friday|15:00:00|17:00:00
5|Saturday|13:00:00|14:00:00
6|Monday|8:00:00|16:00:00
6|Tuesday|8:00:00|16:00:00
6|Wednesday|8:00:00|16:00:00
6|Thursday|8:00:00|16:00:00
6|Friday|8:00:00|16:00:00
6|Saturday|8:00:00|13:00:00
\.
commit;