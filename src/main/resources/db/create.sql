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
DROP TABLE IF EXISTS Species CASCADE;
DROP TABLE IF EXISTS Organisations CASCADE;
DROP TABLE IF EXISTS Org_reps CASCADE;
DROP TABLE IF EXISTS Positions CASCADE;
DROP TABLE IF EXISTS Pet_Owners CASCADE;
DROP TABLE IF EXISTS Medicine_type CASCADE;
DROP TABLE IF EXISTS Races CASCADE;
DROP TABLE IF EXISTS Visit_types CASCADE;
DROP TABLE IF EXISTS Vaxx_type CASCADE;

CREATE TABLE Persons (
    id          SERIAL          PRIMARY KEY,
    first_name  VARCHAR(20)     NOT NULL,
    last_name   VARCHAR(40)     NOT NULL,
    address     VARCHAR(200),
    city        VARCHAR(40),
    telephone   VARCHAR(20),
    email       VARCHAR(60)     UNIQUE,
    fav_animal  VARCHAR(40)
);

CREATE TABLE Species (
    id          SERIAL          PRIMARY KEY,
    name	     VARCHAR(80)     NOT NULL,
    avg_lifespan INTEGER,
    healthy_weight_low INTEGER,
    healthy_weight_high INTEGER
);

CREATE TABLE Organisations (
    id          SERIAL          PRIMARY KEY,
    name	    VARCHAR(40)     NOT NULL,
    address     VARCHAR(200),
    city        VARCHAR(40),
    telephone   VARCHAR(20),
    email       VARCHAR(60)
);

CREATE TABLE Org_reps(
    Org_id      INTEGER         REFERENCES Organisations(id) NOT NULL,
    Rep_id      INTEGER         REFERENCES Persons(id) NOT NULL,
    PRIMARY KEY(Org_id, Rep_id)
);

CREATE TABLE    Positions (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(80)     NOT NULL,
    min_salary  NUMERIC(10, 2)  NOT NULL
);

CREATE TABLE Employees (
    id          SERIAL          PRIMARY KEY,
    person_id   INTEGER         REFERENCES Persons(id) NOT NULL,
    position    INTEGER         REFERENCES Positions(id) NOT NULL,
    salary      NUMERIC(10, 2)  NOT NULL,
    date_start  DATE            NOT NULL CHECK(date_start < date_fire),
    date_fire   DATE
);

CREATE TABLE Vets (
    id          INTEGER         PRIMARY KEY REFERENCES Employees(id) NOT NULL UNIQUE,
    office      VARCHAR(40)
);

CREATE TABLE Races (
    id       INTEGER       PRIMARY KEY,
    species  INTEGER       REFERENCES Species(id) NOT NULL,
    race     VARCHAR(40)   NOT NULL,
    CONSTRAINT unique_species_race UNIQUE (species, race)
);

CREATE TABLE Pets (
    id          SERIAL         PRIMARY KEY,
    name        VARCHAR(40)    NOT NULL,
    sex         CHAR(1)        CHECK(sex = 'M' or sex = 'F'),
    type 	    INTEGER        REFERENCES Races(id) NOT NULL,
    birth_day   DATE,
    weight      NUMERIC(10, 2),
    dangerous   BOOLEAN,
    estimate    BOOLEAN
);

CREATE TABLE Pet_Owners (
    Pet_id          INTEGER         REFERENCES Pets(id) NOT NULL,
    Person_id       INTEGER         REFERENCES Persons(id),
    Org_id          INTEGER         REFERENCES Organisations(id),
    Period_start    DATE            NOT NULL CHECK (Period_start < Period_end),
    Period_end      DATE,
    CHECK (
        (Person_id IS NOT NULL AND Org_id IS NULL)
        OR (Person_id IS NULL AND Org_id IS NOT NULL)
    )
);

CREATE TABLE Medicine_type (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(80)     NOT NULL
);

CREATE TABLE Medicine (
    id          SERIAL         PRIMARY KEY,
    name        VARCHAR(80)    NOT NULL,
    company     VARCHAR(80)    NOT NULL,
    base_price  INTEGER        NOT NULL,
    type        INTEGER        REFERENCES Medicine_type(id)
);

CREATE TABLE Visit_types (
    id          SERIAL          PRIMARY KEY,
    name        VARCHAR(80)     NOT NULL,
    base_price  INTEGER         NOT NULL
);

CREATE TABLE Visits (
    id           SERIAL          PRIMARY KEY,
    pet_id       INTEGER         REFERENCES Pets(id) NOT NULL,
    vet_id       INTEGER         REFERENCES Vets(id) NOT NULL,
    visit_date   DATE            NOT NULL,
    visit_time   TIME            NOT NULL,
    type_id      INTEGER         REFERENCES Visit_types (id) NOT NULL,
    description  VARCHAR(255),
    rate         NUMERIC(3, 1)
);

CREATE TABLE Prescriptions (
    id           SERIAL         PRIMARY KEY,
    id_visit    INTEGER         REFERENCES Visits(id) NOT NULL,
    med_id      INTEGER         REFERENCES Medicine(id) NOT NULL,
    amount      INTEGER         NOT NULL,
    price       NUMERIC(10, 2)  NOT NULL,
    dosing      VARCHAR(255)
);

CREATE TABLE Holidays (
    employee_id   INTEGER       REFERENCES Employees(id) NOT NULL,
    start_date  DATE            CHECK(start_date  <= end_date) NOT NULL,
    end_date    DATE
);

CREATE TABLE WorkHours (
    employee_id INTEGER         REFERENCES Employees(id) NOT NULL,
    "WeekDay"   VARCHAR(10)     CHECK("WeekDay" IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')),
    start_time  TIME            CHECK(start_time < end_time) NOT NULL,
    end_time    TIME            NOT NULL
);

CREATE TABLE Vaxx_type (
    id          SERIAL          PRIMARY KEY,
    frequency   INTEGER         NOT NULL,
    name        VARCHAR(80)     NOT NULL
);

CREATE TABLE Vaxx (
    id          SERIAL          PRIMARY KEY,
    pet_id      INTEGER         REFERENCES Pets(id),
    period_start DATE            CHECK(period_start <= period_end) NOT NULL,
    period_end  DATE            NOT NULL,
    done        BOOLEAN         DEFAULT FALSE,
    type        INTEGER REFERENCES Vaxx_type (id) NOT NULL
);

CREATE TABLE Vets_Specialities (
    vet_id      INTEGER         REFERENCES Vets(id),
    name        VARCHAR(50)     NOT NULL,
    date_start  DATE            NOT NULL
);

DROP TYPE IF EXISTS PERMISSION_TYPE CASCADE;
CREATE TYPE PERMISSION_TYPE AS ENUM ('ADMIN', 'EMPLOYEE', 'USER');

CREATE TABLE Accounts (
    id              SERIAL          PRIMARY KEY,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    username        VARCHAR(50)     NOT NULL UNIQUE,
    password        VARCHAR(250)    NOT NULL,
    user_permissions VARCHAR(10)   DEFAULT 'USER'
);

COPY Persons (first_name, last_name, address, city, telephone, email, fav_animal) FROM stdin (Delimiter ',');
David, O Connor,65 Hudson St,Dulford,070218230685,daveoc@gmail.com,Firefly
Elisabeth,Kowalski,79 Holgate Rd,Rannoch School,07789721317,LizSmith743@yahoo.com,Dog
Rishi,Sunak,10 Downing St,London,02072195437,rishi.sunak.mp@parliament.uk,Penguin
Barbara,Richards,90 Balsham St,Harrogate,07017505023,barbararichards1937@gmail.com,Cobra
Boris,Jansen,52 Holgate Rd, Rainham, 07924955449,bojoforpm@gmail.com,Narwhale
Mahava,Punja,7 Church Way,Bradfield,07751675751,mahpun@yahoo.com,Hedgehog
Bernard,Rodriguez,74 Great North Road,Alticry,07025371694,bendriguez17@yahoo.com,Platypus
Barry,British,24 St Andrews Lane,Dail Mor,07887136485,scoresamgowls@gmail.com,Rhinoceros
Anna,Buchmann,3 Town Lane, Sourton,07009283724,buchmanna@gmail.com,Giraffe
Daniele,Liberman,90 Mounthoollie Lane,Sutton,07979163114,danieleliberman342@gmail.com,Panther
John,Doe,123 Main St,New York City, 555123123,johndoe@example.com,Dog
Jane,Smith,456 Elm St,Los Angeles,555567567,janesmith@example.com,Cat
Michael,Johnson,789 Oak Ave,Chicago,555901901,michaeljohnson@example.com,Eagle
Emily,Williams,321 Pine Rd,Miami,555345345,emilywilliams@example.com,Horse
David,Brown,987 Cedar Lane,San Francisco,555789789,davidbrown@example.com,Rabbit
Sarah,Miller,654 Maple Dr,Boston,555234234,sarahmiller@example.com,Turtle
Daniel,Wilson,852 Birch Blvd,Seattle,555678678,danielwilson@example.com,Lion
Olivia,Taylor,159 Spruce Ct,Dallas,555012012,oliviataylor@example.com,Monkey
Matthew,Anderson,753 Walnut Way,Phoenix,555456456,matthewanderson@example.com,Elephant
Sophia,Thompson,951 Ash Street,Houston,555890890,sophiathompson@example.com,Dolphin
Robert,Johnson,246 Oak St,Chicago,555111222,robertjohnson@example.com,Cat
Elizabeth,Brown,789 Pine Ave,Los Angeles,555333444,elizabethbrown@example.com,Dog
William,Miller,654 Elm Rd,Miami,555555666,williammiller@example.com,Stork
Jennifer,Davis,852 Maple Blvd,New York City,555777888,jenniferdavis@example.com,Horse
Christopher,Anderson,123 Cedar Lane,San Francisco,555999000,christopheranderson@example.com,Rabbit
Jessica,Wilson,456 Birch Ct,Seattle,555222333,jessicawilson@example.com,Turtle
David,Taylor,951 Spruce Ave,Dallas,555444555,davidtaylor@example.com,Lion
Ashley,Thomas,753 Walnut Way,Phoenix,555666777,ashleythomas@example.com,Monkey
Michael,Jackson,159 Ash Street,Houston,555888999,michaeljackson@example.com,Elephant
Elizabth,Windsor,SW1A 1AA,London,3031237300,queenlizzy@uk.com,Dog
Steve,Bajeev,14 Elmo Lane,Boobooland,7218537657,stevobevo@example.com,Beaver
Jane,Dane,222 Blurgh St,Copenhagen,1255373756,tripleane@example.com,Crane
Joseph,Ironsky,13 Kittycat Ave,Tbilisi,12753765468,joeiroe@example.com,Bear
Pierre,Pierre,35 rue CDG,Paris,35372154656,pipierer@example.com,Dog
Sam,Palm,42 Helm St,Cleveland,4450304641,sambamdam@gmail.com,Whale
Dana,Local,37 Lutherstrasse,Berlin,525507781,idksomething@example.com,Cat
Bobby,Steve,3 Street Avenue Lane,New York City,16329579788,bobsteve@yahoo.com,Goose
\.

COPY Species (name, avg_lifespan, healthy_weight_low, healthy_weight_high) FROM stdin (Delimiter ',');
Birds,10,2,8
Dog,12,10,40
Cat,15,5,10
Elephant,70,4000,6000
Turtle,100,1,100
Lion,12,150,250
Giraffe,25,800,1200
Dolphin,25,150,250
Bee,1,1,2
Snake,20,1,10
Shark,20,500,1000
Penguin,20,10,20
Monkey,20,5,10
Bear,25,200,500
Gorilla,35,150,300
Tiger,15,100,250
Horse,25,400,800
Cow,20,500,1000
Sheep,12,50,100
Pig,15,100,200
Rabbit,8,1,2
Kangaroo,15,50,100
Ostrich,40,80,150
Whale,90,50000,200000
Seal,20,50,100
Fox,8,5,10
Eagle,20,2,5
Crocodile,70,500,1000
Parrot,50,1,5
Octopus,3,5,15
Sloth,15,5,10
Panda,20,100,200
Deer,10,80,150
Raccoon,5,2,5
Leopard,15,40,80
Wolf,10,30,50
Peacock,15,2,5
Owl,15,1,2
Koala,20,5,10
Orangutan,35,40,80
\.

COPY Vaxx_type(frequency,name) FROM stdin (Delimiter ',');
12,Rabies
24,Distemper
6,Parvovirus
12,Bordetella
36,Leptospirosis
\.

COPY Organisations (name, address, city, telephone, email) FROM stdin (Delimiter ',');
Animal Care Center,789 Oak Ave,Chicago,555901901,info@animalcarecenter.com
City Vet Clinic,321 Pine Rd,Miami,555345345,info@cityvetclinic.com
Paws and Claws Veterinary Hospital,987 Cedar Lane,San Francisco,555789789,info@pawsandclawsvet.com
Happy Tails Pet Clinic,654 Maple Dr,Boston,555234234,info@happytailspetclinic.com
Wildlife Rehabilitation Center,852 Birch Blvd,Seattle,555678678,info@wildliferehabcenter.com
Furry Friends Veterinary Care,159 Spruce Ct,Dallas,555012012,info@furryfriendsvetcare.com
Healthy Paws Animal Clinic,753 Walnut Way,Phoenix,555456456,info@healthypawsanimalclinic.com
All Creatures Veterinary Center,951 Ash Street,Houston,555890890,info@allcreaturesvetcenter.com
VetCare Express,357 Elm St,Boston,555567890,info@vetcareexpress.com
Pet Wellness Center,852 Oak Ave,Chicago,555789012,info@petwellnesscenter.com
Animal Aid Clinic,753 Pine Rd,Miami,555901234,info@animalaidclinic.com
Country Veterinary Practice,159 Cedar Lane,San Francisco,555123456,info@countryvetpractice.com
Urban Pet Hospital,654 Maple Dr,Los Angeles,555234567,info@urbanpethospital.com
Natures Touch Veterinary Clinic,951 Birch Blvd,Seattle,555345678,info@naturestouchvetclinic.com
Healthy Paws Animal Hospital,753 Spruce Ct,Dallas,555456789,info@healthypawsanimalhospital.com
Companion Care Veterinary Clinic,246 Walnut Way,Phoenix,555567890,info@companioncarevetclinic.com
Pet Oasis,357 Ash Street,Houston,555678901,info@petoasis.com
Paw Prints Animal Clinic,852 Elm St,New York City,555789012,info@pawprintsanimalclinic.com
\.


COPY Positions (name,min_salary) FROM stdin (Delimiter ',');
Veterinarian,5000.00
Veterinary Assistant,2500.00
Veterinary Technician,3000.00
Receptionist,2000.00
Animal Caretaker,1800.00
\.

COPY Employees (person_id,position,salary,date_start) FROM stdin (Delimiter ',');
1,2,5000.00,2021-01-01
2,3,6000.00,2021-02-15
3,1,4500.00,2021-03-10
4,2,5500.00,2021-04-05
5,1,6500.00,2021-05-20
6,1,4000.00,2021-06-15
7,2,5500.00,2021-07-10
8,5,7000.00,2021-08-25
9,1,4200.00,2021-09-20
10,2,5800.00,2021-10-15
11,3,7200.00,2021-11-10
12,5,4300.00,2021-12-05
13,2,6100.00,2022-01-20
14,3,7500.00,2022-02-15
15,4,4400.00,2022-03-10
16,2,6300.00,2022-04-25
17,1,7800.00,2022-05-20
18,1,4600.00,2022-06-15
19,1,6600.00,2022-07-10
20,3,8200.00,2022-08-25
21,1,4800.00,2022-09-20
22,2,6900.00,2022-10-15
23,3,8600.00,2022-11-10
24,1,5000.00,2022-12-05
37,2,7200.00,2023-01-20
\.

COPY Vets(id, office) FROM stdin (Delimiter ',');
3,Room 101
5,Room 103
6,Room 205
9,Room 302
17,Room 334
18,Room 410
19,Room 508
21,Room 512
24,Room 623
\.

COPY Vets_Specialities(vet_id,name,date_start) FROM stdin (Delimiter ',');
3,Surgery,2022-01-01
3,Dermatology,2023-03-15
5,Dermatology,2021-07-15
6,Internal Medicine,2022-05-20
6,Cardiology,2023-01-10
5,Internal Medicine,2023-03-10
9,Cardiology,2022-09-30
9,Ophthalmology,2023-01-20
19,Neurology,2022-11-05
3,Radiology,2023-02-15
6,Surgery,2022-03-10
17,Orthopedics,2022-08-15
18,Dermatology,2021-09-20
18,Internal Medicine,2023-01-05
18,Cardiology,2022-07-01
17,Ophthalmology,2023-02-10
21,Dermatology,2023-04-05
21,Radiology,2022-12-15
21,Internal Medicine,2021-08-20
19,Oncology,2022-06-10
19,Cardiology,2023-03-01
24,Neurology,2022-10-05
24,Internal Medicine,2023-01-15
24,Endocrinology,2023-05-10
\.

COPY WorkHours(employee_id,"WeekDay",start_time,end_time) FROM stdin (Delimiter ',');
1,Monday,09:00:00,17:00:00
1,Tuesday,09:00:00,17:00:00
1,Wednesday,09:00:00,17:00:00
1,Thursday,09:00:00,17:00:00
1,Friday,09:00:00,17:00:00
2,Monday,08:30:00,16:30:00
2,Tuesday,08:30:00,16:30:00
2,Wednesday,08:30:00,16:30:00
2,Thursday,08:30:00,16:30:00
2,Friday,08:30:00,16:30:00
3,Monday,10:00:00,18:00:00
3,Tuesday,10:00:00,18:00:00
3,Wednesday,10:00:00,18:00:00
3,Thursday,10:00:00,18:00:00
3,Friday,10:00:00,18:00:00
4,Monday,07:30:00,15:30:00
4,Tuesday,07:30:00,15:30:00
4,Wednesday,07:30:00,15:30:00
4,Thursday,07:30:00,15:30:00
4,Friday,07:30:00,15:30:00
5,Monday,12:00:00,20:00:00
5,Tuesday,12:00:00,20:00:00
5,Wednesday,12:00:00,20:00:00
5,Thursday,12:00:00,20:00:00
5,Friday,12:00:00,20:00:00
6,Monday,09:00:00,17:00:00
6,Tuesday,09:00:00,17:00:00
6,Wednesday,09:00:00,17:00:00
6,Thursday,09:00:00,17:00:00
6,Friday,09:00:00,17:00:00
7,Monday,08:30:00,16:30:00
7,Tuesday,08:30:00,16:30:00
7,Wednesday,08:30:00,16:30:00
7,Thursday,08:30:00,16:30:00
7,Friday,08:30:00,16:30:00
8,Monday,10:00:00,18:00:00
8,Tuesday,10:00:00,18:00:00
8,Wednesday,10:00:00,18:00:00
8,Thursday,10:00:00,18:00:00
8,Friday,10:00:00,18:00:00
9,Monday,07:30:00,15:30:00
9,Tuesday,07:30:00,15:30:00
9,Wednesday,07:30:00,15:30:00
9,Thursday,07:30:00,15:30:00
9,Friday,07:30:00,15:30:00
10,Monday,12:00:00,20:00:00
10,Tuesday,12:00:00,20:00:00
10,Wednesday,12:00:00,20:00:00
10,Thursday,12:00:00,20:00:00
10,Friday,12:00:00,20:00:00
11,Monday,09:00:00,17:00:00
11,Tuesday,09:00:00,17:00:00
11,Wednesday,09:00:00,17:00:00
11,Thursday,09:00:00,17:00:00
11,Friday,09:00:00,17:00:00
12,Monday,08:30:00,16:30:00
12,Tuesday,08:30:00,16:30:00
12,Wednesday,08:30:00,16:30:00
12,Thursday,08:30:00,16:30:00
12,Friday,08:30:00,16:30:00
13,Monday,10:00:00,18:00:00
13,Tuesday,10:00:00,18:00:00
13,Wednesday,10:00:00,18:00:00
13,Thursday,10:00:00,18:00:00
13,Friday,10:00:00,18:00:00
14,Monday,07:30:00,15:30:00
14,Tuesday,07:30:00,15:30:00
14,Wednesday,07:30:00,15:30:00
14,Thursday,07:30:00,15:30:00
14,Friday,07:30:00,15:30:00
15,Monday,12:00:00,20:00:00
15,Tuesday,12:00:00,20:00:00
15,Wednesday,12:00:00,20:00:00
15,Thursday,12:00:00,20:00:00
15,Friday,12:00:00,20:00:00
16,Monday,09:00:00,17:00:00
16,Tuesday,09:00:00,17:00:00
16,Wednesday,09:00:00,17:00:00
16,Thursday,09:00:00,17:00:00
16,Friday,09:00:00,17:00:00
17,Monday,08:30:00,16:30:00
17,Tuesday,08:30:00,16:30:00
17,Wednesday,08:30:00,16:30:00
17,Thursday,08:30:00,16:30:00
17,Friday,08:30:00,16:30:00
18,Monday,10:00:00,18:00:00
18,Tuesday,10:00:00,18:00:00
18,Wednesday,10:00:00,18:00:00
18,Thursday,10:00:00,18:00:00
18,Friday,10:00:00,18:00:00
19,Monday,07:30:00,15:30:00
19,Tuesday,07:30:00,15:30:00
19,Wednesday,07:30:00,15:30:00
19,Thursday,07:30:00,15:30:00
19,Friday,07:30:00,15:30:00
20,Monday,12:00:00,20:00:00
20,Tuesday,12:00:00,20:00:00
20,Wednesday,12:00:00,20:00:00
20,Thursday,12:00:00,20:00:00
20,Friday,12:00:00,20:00:00
21,Monday,09:00:00,17:00:00
21,Tuesday,09:00:00,17:00:00
21,Wednesday,09:00:00,17:00:00
21,Thursday,09:00:00,17:00:00
21,Friday,09:00:00,17:00:00
22,Monday,08:30:00,16:30:00
22,Tuesday,08:30:00,16:30:00
22,Wednesday,08:30:00,16:30:00
22,Thursday,08:30:00,16:30:00
22,Friday,08:30:00,16:30:00
23,Monday,10:00:00,18:00:00
23,Tuesday,10:00:00,18:00:00
23,Wednesday,10:00:00,18:00:00
23,Thursday,10:00:00,18:00:00
23,Friday,10:00:00,18:00:00
24,Monday,07:30:00,15:30:00
24,Tuesday,07:30:00,15:30:00
24,Wednesday,07:30:00,15:30:00
24,Thursday,07:30:00,15:30:00
24,Friday,07:30:00,15:30:00
25,Monday,12:00:00,20:00:00
25,Tuesday,12:00:00,20:00:00
25,Wednesday,12:00:00,20:00:00
25,Thursday,12:00:00,20:00:00
25,Friday,12:00:00,20:00:00
\.

COPY Races (id, species, race) FROM stdin (Delimiter ',');
1,2,Persian
2,2,Siamese
3,2,Bengal
4,2,Labrador Retriever
5,2,Golden Retriever
6,2,Poodle
7,3,Maine Coon
8,3,Sphynx
9,3,Ragdoll
10,4,German Shepherd
11,4,Bulldog
12,4,Pug
13,5,Abyssinian
14,5,Sphynx
15,5,Siamese
16,6,Beagle
17,6,Dalmatian
18,6,Bulldog
19,7,Persian
20,7,Bengal
21,2,Scottish Fold
22,2,Exotic Shorthair
23,2,Boxer
24,2,Rottweiler
25,3,Siberian
26,3,British Shorthair
27,4,Doberman
28,4,Chihuahua
29,5,Persian
30,5,Doggez
31,6,Husky
32,6,Great Dane
33,7,Parowa
34,7,Ragdoll
35,2,Birman
36,2,Sphynx
37,2,Shih Tzu
38,2,Pomeranian
39,3,Chartreux
40,3,American Shorthair
41,4,Labradoodle
42,4,Jack Russell Terrier
43,5,Maine Coon
44,5,Gogger
45,6,Basset Hound
46,6,Bull Terrier
47,7,Burmese
48,7,Savannah
49,2,Tonkinese
50,2,Devon Rex
\.

COPY Pets (name, sex, type, birth_day, weight, dangerous, estimate) FROM stdin (Delimiter ',');
Luna,F,1,2018-05-12,4.5,false,true
Max,M,32,2019-02-28,8.2,false,false
Bella,F,24,2020-09-15,3.7,false,false
Charlie,M,50,2017-11-03,5.1,false,false
Lucy,F,48,2020-01-19,6.8,false,false
Cooper,M,21,2016-08-10,9.3,false,false
Molly,F,3,2019-07-06,3.9,false,true
Simba,M,1,2017-09-22,5.7,false,false
Lola,F,2,2018-12-10,4.2,false,false
Rocky,M,37,2019-06-17,6.1,false,false
Daisy,F,4,2016-03-05,7.8,false,true
Oscar,M,5,2020-04-02,3.4,false,false
Mia,F,11,2017-11-28,6.5,false,false
Bailey,M,2,2018-08-14,9.9,false,true
Buddy,M,33,2019-03-12,5.6,false,false
Sadie,F,41,2017-06-25,8.3,false,false
Milo,M,5,2018-10-09,3.9,false,false
Coco,F,18,2020-02-15,4.7,false,true
Maximus,M,2,2016-12-01,7.2,false,false
Ruby,F,3,2019-08-18,5.4,false,false
Leo,M,49,2017-04-09,9.1,false,true
Lucky,M,1,2018-07-23,6.8,false,false
Luna,F,2,2019-04-17,4.5,false,true
Rocky,M,35,2020-01-10,7.3,false,false
Charlie,M,5,2016-05-20,3.6,false,false
Molly,F,1,2019-11-12,5.2,false,false
Max,M,2,2018-03-28,9.7,false,true
Simba,M,37,2017-10-15,6.2,false,false
Lucy,F,47,2018-06-08,7.9,false,false
Oliver,M,50,2019-01-02,4.1,false,false
Lola,F,12,2016-04-17,5.8,false,true
Maximus,M,2,2020-08-22,8.5,false,false
Bella,F,38,2017-02-10,3.3,false,false
Cooper,M,43,2018-09-05,9.2,false,false
Daisy,F,5,2019-05-12,3.7,false,false
Charlie,M,15,2018-08-28,6.4,false,false
Luna,F,2,2017-03-14,8.9,false,true
Max,M,3,2016-09-09,4.3,false,false
Molly,F,49,2020-04-26,7.6,false,false
Rocky,M,5,2017-12-03,5.5,false,false
Bella,F,1,2018-07-19,9.4,false,true
Simba,M,22,2019-06-11,4.8,false,false
Lola,F,3,2020-03-25,7.2,false,false
Rocky,M,4,2017-01-08,5.9,false,false
Bella,F,12,2018-08-03,3.5,false,false
Joe,M,5,2018-07-01,3.5,false,false
Stevey,M,3,2018-09-02,32.1,false,false
Fromage,F,42,2014-01-12,13.2, true,false
Omelette,F,1,2013-08-13,12.1,false,false
Parsley,M,24,2002-02-02,4.6,false,false
Khachapuri,F,1,2011-09-11,8.9,false,false
\.


COPY Positions (name,min_salary) FROM stdin (Delimiter ',');
Veterinarian,5000.00
Veterinary Assistant,2500.00
Veterinary Technician,3000.00
Receptionist,2000.00
Animal Caretaker,1800.00
\.

COPY Holidays(employee_id,start_date,end_date) FROM stdin (Delimiter ',');
1,2023-01-01,2023-01-02
2,2023-02-15,2023-02-16
3,2023-04-10,2023-04-10
4,2023-05-01,2023-05-01
5,2023-07-04,2023-07-04
6,2023-09-04,2023-09-05
7,2023-10-09,2023-10-09
8,2023-11-23,2023-11-24
9,2023-12-25,2023-12-26
10,2023-12-31,2024-01-01
11,2023-02-10,2023-02-10
12,2023-03-17,2023-03-17
13,2023-04-21,2023-04-21
14,2023-06-19,2023-06-19
15,2023-09-05,2023-09-05
16,2023-10-31,2023-10-31
17,2023-11-23,2023-11-24
18,2023-12-24,2023-12-25
19,2023-01-01,2023-01-02
20,2023-02-15,2023-02-16
21,2023-04-14,2023-04-14
22,2023-05-29,2023-05-29
23,2023-07-04,2023-07-04
24,2023-09-04,2023-09-05
25,2023-10-09,2023-10-10
\.

COPY Medicine_type (name) FROM stdin (Delimiter ',');
Antibiotic
Painkiller
Antifungal
Anti-inflammatory
Antiemetic
Antihistamine
Vitamin
Antidepressant
Antacid
Anticoagulant
Antiseptic
Analgesic
Antiviral
Antidiarrheal
Antihypertensive
Antipyretic
Decongestant
Anticonvulsant
Laxative
Antidiabetic
Antibacterial
Antipsychotic
Diuretic
Antiparasitic
Bronchodilator
Anthelmintic
Anticoagulant
Anti-anxiety
Anti-itch
Antispasmodic
Cardiotonic
Muscle relaxant
\.

COPY Medicine (name, company, base_price, type) FROM stdin (Delimiter ',');
Paracetamol,ABC Pharmaceuticals,10,2
Amoxicillin,XYZ Pharmaceuticals,15,1
Ibuprofen,DEF Pharmaceuticals,12,2
Loratadine,GHI Pharmaceuticals,8,6
Omeprazole,JKL Pharmaceuticals,20,3
Aspirin,MNO Pharmaceuticals,7,5
Acetaminophen,ABC Pharmaceuticals,10,1
Amoxicillin,XYZ Pharmaceuticals,15,2
Ibuprofen,DEF Pharmaceuticals,12,1
Loratadine,GHI Pharmaceuticals,8,3
Omeprazole,JKL Pharmaceuticals,20,2
Aspirin,MNO Pharmaceuticals,7,5
Ciprofloxacin,PQR Pharmaceuticals,18,2
Diphenhydramine,STU Pharmaceuticals,9,4
Simvastatin,VWX Pharmaceuticals,22,3
Metformin,YZA Pharmaceuticals,14,5
Codeine,BCD Pharmaceuticals,16,1
Acetaminophen,ABC Pharmaceuticals,10,1
Amoxicillin,XYZ Pharmaceuticals,15,2
Ibuprofen,DEF Pharmaceuticals,12,1
Loratadine,GHI Pharmaceuticals,8,3
Omeprazole,JKL Pharmaceuticals,20,2
Aspirin,MNO Pharmaceuticals,7,5
Ciprofloxacin,PQR Pharmaceuticals,18,2
Diphenhydramine,STU Pharmaceuticals,9,4
Simvastatin,VWX Pharmaceuticals,22,3
Metformin,YZA Pharmaceuticals,14,5
Codeine,BCD Pharmaceuticals,16,1
Paroxetine,LMN Pharmaceuticals,25,6
Ranitidine,OPQ Pharmaceuticals,13,3
Metoprolol,RST Pharmaceuticals,19,4
Cetirizine,UVW Pharmaceuticals,11,3
Acetaminophen,ABC Pharmaceuticals,10,1
Amoxicillin,XYZ Pharmaceuticals,15,2
Ibuprofen,DEF Pharmaceuticals,12,1
Loratadine,GHI Pharmaceuticals,8,3
Omeprazole,JKL Pharmaceuticals,20,2
Aspirin,MNO Pharmaceuticals,7,5
Ciprofloxacin,PQR Pharmaceuticals,18,2
Diphenhydramine,STU Pharmaceuticals,9,4
Simvastatin,VWX Pharmaceuticals,22,3
Metformin,YZA Pharmaceuticals,14,5
Codeine,BCD Pharmaceuticals,16,1
Paroxetine,LMN Pharmaceuticals,25,6
Ranitidine,OPQ Pharmaceuticals,13,3
Metoprolol,RST Pharmaceuticals,19,4
Cetirizine,UVW Pharmaceuticals,11,3
Clopidogrel,XYZ Pharmaceuticals,21,5
Atorvastatin,ABC Pharmaceuticals,17,3
Amiodarone,DEF Pharmaceuticals,23,6
Warfarin,GHI Pharmaceuticals,16,4
Gabapentin,JKL Pharmaceuticals,12,2
\.

COPY Visit_types (name, base_price) FROM stdin (Delimiter ',');
Consultation,50
Vaccination,30
Surgery,200
Dental Cleaning,80
Laboratory Tests,120
X-Ray,100
Ultrasound,150
Check-up,60
Emergency Care,250
Physical Examination,70
Microchipping,40
Allergy Testing,90
Deworming,20
Diagnostics,110
Grooming,65
Spay/Neuter,180
Eye Examination,95
Nutrition Counseling,55
Behavioral Consultation,85
Senior Care,130
Dental Surgery,220
Puppy/Kitten Care,45
Rehabilitation,175
Parasite Control,25
Wellness Package,150
Heartworm Testing,80
Nutritional Assessment,60
Ophthalmology Consultation,95
Orthopedic Consultation,105
Dermatology Consultation,90
Endoscopy,180
Blood Transfusion,200
Pet Boarding,75
Dental Extraction,95
Reproductive Services,140
Radiology Consultation,100
Flea and Tick Treatment,35
Behavior Modification,120
Chemotherapy,250
Physical Therapy,90
Pet Microscopy,80
Laser Therapy,150
Hospice Care,100
Pet Adoption,0
\.

COPY Org_reps(Org_id, Rep_id) FROM stdin (Delimiter ',');
2,26
5,32
3,37
1,28
6,29
4,33
7,36
\.

COPY Visits (pet_id, vet_id, visit_date, visit_time, type_id, description, rate) FROM stdin (Delimiter ',');
2, 3, 2023-06-01, 09:00:00, 4, Annual check-up, 4.5
3, 6, 2023-06-02, 14:30:00, 1, Vaccination, 4.8
1, 5, 2023-06-03, 10:15:00, 2, Dental cleaning, 4.7
4, 3, 2023-06-04, 11:30:00, 3, Physical examination, 4.6
3, 3, 2023-06-05, 15:45:00, 5, Surgery, 4.9
2, 9, 2023-06-06, 16:30:00, 2, Nail trimming, 4.3
5, 3, 2023-06-07, 11:00:00, 4, Microchipping, 4.2
1, 3, 2023-06-08, 13:45:00, 3, Physical examination, 4.9
4, 6, 2023-06-09, 15:00:00, 5, Surgery, 4.7
3, 5, 2023-06-10, 10:30:00, 1, Vaccination, 4.6
2, 3, 2023-06-11, 14:15:00, 3, Physical examination, 4.4
3, 21, 2023-06-17, 12:30:00, 3, Physical examination, 4.3
2, 3, 2023-06-18, 14:45:00, 4, Microchipping, 4.1
1, 24, 2023-06-19, 16:15:00, 5, Surgery, 4.6
5, 3, 2023-06-20, 09:30:00, 1, Vaccination, 4.4
3, 3, 2023-06-21, 11:00:00, 2, Dental cleaning, 4.2
1, 19, 2023-06-22, 13:30:00, 3, Physical examination, 4.7
4, 3, 2023-06-23, 15:45:00, 4, Microchipping, 4.4
2, 17, 2023-06-24, 09:15:00, 5, Surgery, 4.3
5, 24, 2023-06-25, 11:45:00, 1, Vaccination, 4.6
2, 18, 2023-06-26, 10:30:00, 2, Dental cleaning, 4.1
3, 3, 2023-06-27, 14:15:00, 3, Physical examination, 4.8
4, 5, 2023-06-28, 16:30:00, 4, Microchipping, 4.5
5, 24, 2023-06-29, 09:45:00, 5, Surgery, 4.4
1, 3, 2023-06-30, 12:00:00, 1, Vaccination, 4.7
6, 18, 2023-07-01, 11:00:00, 2, Dental cleaning, 4.3
7, 19, 2023-07-02, 13:45:00, 3, Physical examination, 4.9
8, 5, 2023-07-03, 15:30:00, 4, Microchipping, 4.6
9, 3, 2023-07-04, 08:45:00, 5, Surgery, 4.5
10, 3, 2023-07-05, 11:30:00, 1, Vaccination, 4.8
11, 3, 2023-07-06, 10:15:00, 2, Dental cleaning, 4.4
12, 17, 2023-07-07, 14:30:00, 3, Physical examination, 4.7
13, 18, 2023-07-08, 16:45:00, 4, Microchipping, 4.2
14, 9, 2023-07-09, 09:30:00, 5, Surgery, 4.1
15, 3, 2023-07-10, 12:00:00, 1, Vaccination, 4.6
16, 17, 2023-07-11, 11:30:00, 2, Dental cleaning, 4.3
17, 18, 2023-07-12, 15:45:00, 3, Physical examination, 4.8
18, 17, 2023-07-13, 17:30:00, 4, Microchipping, 4.6
19, 18, 2023-07-14, 10:00:00, 5, Surgery, 4.4
20, 19, 2023-07-15, 13:15:00, 1, Vaccination, 4.7
21, 24, 2023-07-16, 14:30:00, 2, Dental cleaning, 4.2
22, 21, 2023-07-17, 16:45:00, 3, Physical examination, 4.9
23, 21, 2023-07-18, 09:30:00, 4, Microchipping, 4.7
24, 9, 2023-07-19, 11:00:00, 5, Surgery, 4.5
25, 21, 2023-07-20, 12:15:00, 1, Vaccination, 4.6
26, 21, 2023-07-21, 15:30:00, 2, Dental cleaning, 4.3
27, 21, 2023-07-22, 17:15:00, 3, Physical examination, 4.8
28, 3, 2023-07-23, 10:45:00, 4, Microchipping, 4.6
29, 3, 2023-07-24, 13:30:00, 5, Surgery, 4.4
30, 9, 2023-07-25, 14:45:00, 1, Vaccination, 4.5
31, 17, 2023-07-26, 11:00:00, 2, Dental cleaning, 4.7
32, 18, 2023-07-27, 14:30:00, 3, Physical examination, 4.9
33, 19, 2023-07-28, 16:45:00, 4, Microchipping, 4.3
34, 9, 2023-07-29, 9:15:00, 5, Surgery, 4.5
35, 3, 2023-07-30, 12:30:00, 1, Vaccination, 4.2
37, 3, 2023-07-30, 13:30:00, 1, Microchipping, 4.3
38, 3, 2023-07-30, 14:45:00, 1, Surgery, 4.1
40, 21, 2023-07-30, 12:00:00, 1, Physical examination, 4.8
39, 3, 2023-07-30, 11:30:00, 1, Surgery, 4.0
39, 9, 2023-07-30, 10:30:00, 1, Vaccination, 4.9
1, 3, 2023-06-12, 09:00:00, 1, General check-up,\N
2, 3, 2023-06-12, 11:30:00, 4, Vaccination,\N
3, 3, 2023-06-12, 14:00:00, 7, Dental cleaning,\N
4, 5, 2023-06-12, 10:00:00, 2, Consultation,\N
5, 5, 2023-06-12, 12:30:00, 5, X-ray,\N
6, 5, 2023-06-12, 15:00:00, 8, Blood test,\N
7, 6, 2023-06-12, 09:30:00, 3, Ultrasound,\N
8, 6, 2023-06-12, 12:00:00, 6, Surgery,\N
9, 6, 2023-06-12, 14:30:00, 9, Vaccination,\N
10, 9, 2023-06-12, 10:30:00, 4, Dental cleaning,\N
11, 9, 2023-06-12, 13:00:00, 7, General check-up,\N
12, 9, 2023-06-12, 15:30:00, 10, X-ray,\N
13, 17, 2023-06-12, 09:30:00, 5, Blood test,\N
14, 17, 2023-06-12, 12:00:00, 8, Ultrasound,\N
15, 17, 2023-06-12, 14:30:00, 11, Surgery,\N
16, 18, 2023-06-12, 10:00:00, 6, Dental cleaning,\N
17, 18, 2023-06-12, 12:30:00, 9, General check-up,\N
18, 18, 2023-06-12, 15:00:00, 12, Vaccination,\N
19, 19, 2023-06-12, 09:00:00, 7, X-ray,\N
20, 19, 2023-06-12, 11:30:00, 10, Blood test,\N
21, 19, 2023-06-12, 14:00:00, 13, Ultrasound,\N
22, 21, 2023-06-12, 10:30:00, 8, Surgery,\N
23, 21, 2023-06-12, 13:00:00, 11, Dental cleaning,\N
24, 21, 2023-06-12, 15:30:00, 14, General check-up,\N
25, 24, 2023-06-12, 09:00:00, 12, X-ray,\N
26, 24, 2023-06-12, 11:30:00, 15, Blood test,\N
27, 24, 2023-06-12, 14:00:00, 1, Ultrasound,\N
28, 3, 2023-06-13, 09:00:00, 2, Surgery,\N
29, 3, 2023-06-13, 11:30:00, 5, Dental cleaning,\N
30, 3, 2023-06-13, 14:00:00, 8, General check-up,\N
31, 5, 2023-06-13, 10:00:00, 3, X-ray,\N
32, 5, 2023-06-13, 12:30:00, 6, Blood test,\N
33, 5, 2023-06-13, 15:00:00, 9, Ultrasound,\N
34, 6, 2023-06-13, 09:30:00, 4, Vaccination,\N
35, 6, 2023-06-13, 12:00:00, 7, Dental cleaning,\N
36, 6, 2023-06-13, 14:30:00, 10, General check-up,\N
37, 9, 2023-06-13, 10:30:00, 5, X-ray,\N
38, 9, 2023-06-13, 13:00:00, 8, Blood test,\N
39, 9, 2023-06-13, 15:30:00, 11, Ultrasound,\N
40, 17, 2023-06-13, 09:30:00, 6, Surgery,\N
41, 17, 2023-06-13, 12:00:00, 9, Dental cleaning,\N
42, 17, 2023-06-13, 14:30:00, 12, General check-up,\N
43, 18, 2023-06-13, 10:00:00, 7, Vaccination,\N
44, 18, 2023-06-13, 12:30:00, 10, X-ray,\N
45, 18, 2023-06-13, 15:00:00, 13, Blood test,\N
46, 19, 2023-06-13, 09:00:00, 8, Ultrasound,\N
47, 19, 2023-06-13, 11:30:00, 11, Surgery,\N
48, 19, 2023-06-13, 14:00:00, 14, Dental cleaning,\N
49, 21, 2023-06-13, 10:30:00, 9, General check-up,\N
50, 21, 2023-06-13, 13:00:00, 12, Vaccination,\N
51, 21, 2023-06-13, 15:30:00, 15, X-ray,\N
22, 24, 2023-06-13, 09:00:00, 1, Blood test,\N
23, 24, 2023-06-13, 11:30:00, 4, Ultrasound,\N
4, 24, 2023-06-13, 14:00:00, 7, Surgery,\N
15, 3, 2023-06-14, 09:00:00, 4, Dental cleaning,\N
26, 3, 2023-06-14, 11:30:00, 7, General check-up,\N
37, 3, 2023-06-14, 14:00:00, 10, X-ray,\N
38, 5, 2023-06-14, 10:00:00, 5, Blood test,\N
39, 5, 2023-06-14, 12:30:00, 8, Ultrasound,\N
10, 5, 2023-06-14, 15:00:00, 11, Surgery,\N
11, 6, 2023-06-14, 09:30:00, 6, Dental cleaning,\N
12, 6, 2023-06-14, 12:00:00, 9, General check-up,\N
13, 6, 2023-06-14, 14:30:00, 12, Vaccination,\N
14, 9, 2023-06-14, 10:30:00, 7, X-ray,\N
15, 9, 2023-06-14, 13:00:00, 10, Blood test,\N
16, 9, 2023-06-14, 15:30:00, 13, Ultrasound,\N
17, 17, 2023-06-14, 09:30:00, 8, Surgery,\N
18, 17, 2023-06-14, 12:00:00, 11, Dental cleaning,\N
19, 17, 2023-06-14, 14:30:00, 14, General check-up,\N
20, 18, 2023-06-14, 10:00:00, 9, Vaccination,\N
21, 18, 2023-06-14, 12:30:00, 12, X-ray,\N
22, 18, 2023-06-14, 15:00:00, 15, Blood test,\N
23, 19, 2023-06-14, 09:00:00, 10, Ultrasound,\N
24, 19, 2023-06-14, 11:30:00, 13, Surgery,\N
25, 19, 2023-06-14, 14:00:00, 1, Dental cleaning,\N
26, 21, 2023-06-14, 10:30:00, 11, General check-up,\N
27, 21, 2023-06-14, 13:00:00, 14, Vaccination,\N
28, 21, 2023-06-14, 15:30:00, 2, X-ray,\N
29, 24, 2023-06-15, 09:00:00, 12, Blood test,\N
30, 24, 2023-06-15, 11:30:00, 15, Ultrasound,\N
31, 24, 2023-06-15, 14:00:00, 1, Surgery,\N
32, 3, 2023-06-15, 09:00:00, 5, Dental cleaning,\N
33, 3, 2023-06-15, 11:30:00, 8, General check-up,\N
34, 3, 2023-06-15, 14:00:00, 11, X-ray,\N
35, 5, 2023-06-15, 10:00:00, 6, Blood test,\N
36, 5, 2023-06-15, 12:30:00, 9, Ultrasound,\N
37, 5, 2023-06-15, 15:00:00, 12, Surgery,\N
38, 6, 2023-06-15, 09:30:00, 7, Dental cleaning,\N
39, 6, 2023-06-15, 12:00:00, 10, General check-up,\N
40, 6, 2023-06-15, 14:30:00, 13, Vaccination,\N
41, 9, 2023-06-15, 10:30:00, 8, X-ray,\N
42, 9, 2023-06-15, 13:00:00, 11, Blood test,\N
43, 9, 2023-06-15, 15:30:00, 14, Ultrasound,\N
44, 17, 2023-06-15, 09:30:00, 9, Surgery,\N
45, 17, 2023-06-15, 12:00:00, 12, Dental cleaning,\N
46, 17, 2023-06-15, 14:30:00, 15, General check-up,\N
47, 18, 2023-06-15, 10:00:00, 10, Vaccination,\N
48, 18, 2023-06-15, 12:30:00, 13, X-ray,\N
49, 18, 2023-06-15, 15:00:00, 1, Blood test,\N
10, 19, 2023-06-15, 09:00:00, 11, Ultrasound,\N
11, 19, 2023-06-15, 11:30:00, 14, Surgery,\N
12, 19, 2023-06-15, 14:00:00, 2, Dental cleaning,\N
13, 21, 2023-06-15, 10:30:00, 13, General check-up,\N
\.

COPY Prescriptions (id_visit, med_id, amount, price, dosing) FROM stdin (Delimiter ',');
1, 23, 2, 30.00, "1 tablet twice daily"
2, 12, 1, 15.50, "5 mL once daily"
3, 19, 3, 45.75, "2 tablets every 8 hours"
4, 8, 4, 60.80, "1 capsule daily with food"
5, 16, 2, 25.60, "1 spray to affected area twice daily"
6, 9, 3, 50.25, "1 tablet three times daily"
7, 5, 1, 12.80, "2 drops in each eye twice daily"
8, 14, 2, 35.60, "1 teaspoon every 12 hours"
9, 20, 1, 18.90, "1 spray to affected area once daily"
10, 18, 4, 67.20, "1 capsule with meals"
11, 7, 2, 25.50, "1 tablet in the morning"
12, 16, 3, 48.75, "1 teaspoon every 8 hours"
13, 22, 1, 15.90, "2 sprays to affected area twice daily"
14, 11, 2, 32.40, "1 capsule with food"
15, 19, 4, 58.80, "1 drop in each ear daily"
16, 6, 3, 38.70, "1 tablet twice daily"
17, 14, 2, 25.60, "1 teaspoon before meals"
18, 25, 1, 12.80, "Apply a thin layer to the affected area"
19, 9, 4, 52.40, "1 capsule with water"
20, 17, 3, 45.90, "2 drops in each eye twice daily"
21, 12, 2, 30.40, "1 tablet daily"
22, 28, 3, 38.70, "Apply to the affected area twice daily"
23, 7, 1, 15.90, "1 spray in each nostril twice daily"
24, 19, 4, 64.80, "1 teaspoon with food"
25, 11, 2, 25.60, "1 capsule before bedtime"
26, 9, 3, 45.90, "Apply a thin layer to the affected area three times a day"
27, 15, 1, 12.50, "Take 2 tablets in the morning"
28, 4, 2, 21.80, "Take 1 tablet with meals"
29, 17, 3, 56.70, "Apply a small amount to the affected area twice daily"
30, 23, 1, 9.90, "Take 1 capsule daily with water"
31, 12, 2, 35.60, "Take 1 tablet in the morning and 1 tablet in the evening"
32, 19, 1, 18.90, "Apply a thin layer to the affected area once daily"
33, 6, 3, 51.20, "Take 2 capsules with meals"
34, 14, 2, 29.50, "Apply a small amount to the affected area as needed"
35, 21, 1, 14.80, "Take 1 tablet daily after breakfast"
36, 9, 2, 42.10, "Take 1 tablet with water"
37, 17, 1, 25.80, "Apply a thin layer to the affected area twice daily"
38, 28, 3, 68.90, "Take 2 capsules before meals"
39, 8, 2, 37.50, "Apply a small amount to the affected area as directed"
40, 24, 1, 19.90, "Take 1 tablet in the morning"
\.


COPY Pet_Owners (Pet_id, Person_id, Org_id, Period_start, Period_end) FROM stdin (Delimiter ',');
1,2,\N,2020-01-01,2022-06-30
2,\N,1,2019-03-15,2021-12-31
3,4,\N,2018-09-01,2022-02-28
4,\N,2,2021-05-10,\N
5,3,\N,2020-07-01,2023-01-31
6,\N,1,2017-11-20,2023-06-30
7,1,\N,2022-02-15,\N
2,\N,3,2021-03-01,\N
4,\N,1,2019-08-10,2022-12-31
6,2,\N,2020-05-15,\N
11,\N,2,2018-12-01,2021-09-30
13,1,\N,2017-06-20,2023-03-31
15,\N,3,2019-01-15,\N
17,3,\N,2022-04-10,2023-11-30
12,\N,3,2021-03-01,\N
14,\N,1,2019-08-10,2022-12-31
16,2,\N,2020-05-15,\N
11,\N,2,2018-12-01,2021-09-30
13,1,\N,2017-06-20,2023-03-31
25,\N,3,2019-01-15,\N
27,3,\N,2022-04-10,2023-11-30
23,\N,2,2018-11-15,2022-09-30
24,\N,3,2019-05-10,\N
25,2,\N,2020-08-01,2023-04-30
26,\N,1,2017-04-25,2022-08-31
22,1,\N,2019-01-05,\N
27,\N,2,2021-02-10,2023-12-31
21,3,\N,2018-06-20,2021-11-30
32,12,\N,2022-01-01,\N
34,\N,6,2021-08-15,2023-03-31
36,25,\N,2019-05-10,\N
31,\N,9,2020-12-01,2022-09-30
33,14,\N,2018-06-20,\N
35,\N,2,2019-11-15,\N
37,19,\N,2021-03-10,2023-12-31
30,\N,15,2018-11-15,2022-09-30
4,\N,10,2020-05-10,\N
5,24,\N,2019-08-01,2023-04-30
6,\N,8,2017-04-25,2022-08-31
2,23,\N,2019-01-05,\N
7,\N,6,2021-02-10,2023-12-31
10,15,\N,2018-06-20,2021-11-30
2,18,\N,2021-01-01,\N
4,\N,12,2019-08-15,2022-03-31
6,29,\N,2020-06-10,\N
10,\N,8,2018-12-01,2021-09-30
3,25,\N,2017-06-20,\N
5,\N,2,2019-01-15,2023-04-30
7,9,\N,2022-02-10,2023-11-30
\.

COPY Vaxx (pet_id, period_start, period_end, done, type) FROM stdin (Delimiter ',');
5, 2023-01-15, 2023-02-15, true, 3
9, 2023-03-10, 2023-03-20, false, 1
7, 2023-04-05, 2023-05-05, false, 2
2, 2023-06-01, 2023-06-30, true, 4
8, 2023-07-10, 2023-07-20, false, 1
3, 2023-08-15, 2023-09-15, false, 2
1, 2023-10-10, 2023-10-20, true, 4
6, 2023-11-05, 2023-12-05, false, 1
4, 2024-01-01, 2024-01-31, false, 3
10, 2024-02-10, 2024-02-20, true, 2
1, 2023-06-25, 2023-07-25, false, 2
20, 2023-07-15, 2023-08-15, false, 3
10, 2023-08-05, 2023-08-20, true, 1
30, 2023-09-01, 2023-09-30, false, 4
40, 2023-10-10, 2023-11-10, false, 5
12, 2023-11-20, 2023-12-20, false, 3
38, 2023-12-05, 2023-12-25, true, 2
17, 2024-01-10, 2024-02-10, false, 4
44, 2024-02-01, 2024-02-29, false, 1
33, 2024-03-15, 2024-04-15, false, 5
28, 2024-04-10, 2024-05-10, false, 3
6, 2024-05-01, 2024-05-31, false, 1
41, 2024-06-15, 2024-07-15, false, 5
19, 2024-07-10, 2024-08-10, false, 2
31, 2024-08-15, 2024-09-15, false, 4
12, 2024-09-01, 2024-09-30, false, 3
31, 2024-10-15, 2024-11-15, false, 2
44, 2024-11-10, 2024-12-10, false, 5
8, 2024-12-15, 2025-01-15, false, 4
21, 2025-01-10, 2025-02-10, false, 1
17, 2025-02-15, 2025-03-15, false, 2
39, 2025-03-10, 2025-04-10, false, 1
6, 2025-04-15, 2025-05-15, false, 5
42, 2025-05-10, 2025-06-10, false, 3
14, 2025-06-15, 2025-07-15, false, 4
16, 2025-07-10, 2025-08-10, false, 5
33, 2025-08-15, 2025-09-15, false, 4
31, 2025-09-10, 2025-10-10, false, 3
26, 2025-10-15, 2025-11-15, false, 2
29, 2025-11-10, 2025-12-10, false, 1
23, 2026-01-10, 2026-02-10, false, 2
16, 2026-02-15, 2026-03-15, false, 5
44, 2026-03-10, 2026-04-10, false, 4
35, 2026-04-15, 2026-05-15, false, 3
40, 2026-05-10, 2026-06-10, false, 2
22, 2026-06-15, 2026-07-15, false, 1
12, 2026-07-10, 2026-08-10, false, 3
38, 2026-08-15, 2026-09-15, false, 3
36, 2026-09-10, 2026-10-10, false, 2
41, 2026-10-15, 2026-11-15, false, 2
46, 2026-11-10, 2026-12-10, false, 4
19, 2026-12-15, 2027-01-15, false, 5
37, 2027-01-10, 2027-02-10, false, 2
14, 2027-02-15, 2027-03-15, false, 3
26, 2027-03-10, 2027-04-10, false, 1
\.

COPY Accounts(email,username,password,user_permissions) FROM stdin (Delimiter ',');
rishi.sunak.mp@parliament.uk,rishi.sunak,d701a5343fa2bcaef5aaf79c4e325c7c987e08ebb3558b40df097a28605cf84c,EMPLOYEE
bojoforpm@gmail.com,boris.jansen,5f4526ea50df81ea950beb4e5634b4c7fc339f2c9db2a78c4393a7b034e453ed,EMPLOYEE
mahpun@yahoo.com,mahava.punja,ad751879245cdf85f51b8efe4cdea7de430b9ff97fd05646eb83c30c4365b41f,EMPLOYEE
buchmanna@gmail.com,anna.buchmann,51fdba97d8161d35e15b85dcfb3e5b5548589175232b3dedf316cad0d3fd9228,EMPLOYEE
danielwilson@example.com,daniel.wilson,430579e231aaf3b8cc78889fe9dae4c805aa5042b97478854de9ffb4e853a6e5,EMPLOYEE
oliviataylor@example.com,olivia.taylor,0f9c774876349a4dac3258449793dd96ede87ba6f6ef136cbf572d76b7df0d39,EMPLOYEE
matthewanderson@example.com,matthew.anderson,575069fa4cfeb351b234d2e44f87ea55f36be16c0564d4e5f342a5d57c4241a9,EMPLOYEE
robertjohnson@example.com,robert.johnson,aa2b49b651c0c9cdd1957170154a2e286f4d595503737a55e01a1ec27b754d4b,EMPLOYEE
jenniferdavis@example.com,jennifer.davis,e4190161c79ab161130e6b339d11417dad81c9706ef7f909425fba3c1d906859,EMPLOYEE
admin@wp.pl,admin,8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918,ADMIN
\.

----OBLICZNIE OCENY WETERYNARZA

CREATE OR REPLACE FUNCTION calculate_vet_rating(search_id INTEGER)
RETURNS NUMERIC(3, 1) AS $$
DECLARE
    total_rating INTEGER := 0;
    total_visits INTEGER := 0;
    avg_rating NUMERIC(3, 1);
BEGIN
    SELECT COUNT(*), COALESCE(SUM(rate), 0)
    INTO total_visits, total_rating
    FROM Visits
    WHERE search_id = vet_id;

    IF total_visits > 0 THEN
        avg_rating := total_rating::NUMERIC / total_visits::NUMERIC;
        RETURN ROUND(avg_rating, 1);
    ELSE
        RETURN 0.0;
    END IF;
END;
$$ LANGUAGE plpgsql;

DROP VIEW IF EXISTS EmployeeDetails;
CREATE VIEW EmployeeDetails AS
SELECT e.id, p.first_name, p.last_name, pos.name AS position, p.email, e.salary,
    e.date_start, e.date_fire, calculate_vet_rating(p.id) AS rating
FROM Employees e
JOIN Persons p ON e.person_id = p.id
JOIN Positions pos ON e.position = pos.id;

DROP VIEW IF EXISTS VetSchedule;
CREATE VIEW VetSchedule AS
SELECT v.id, p.first_name, p.last_name, vs.name AS specialization
FROM Employees e
JOIN Persons p ON e.person_id = p.id
JOIN Vets v ON v.id = e.id
JOIN Vets_Specialities vs ON vs.vet_id = v.id
WHERE e.date_fire IS NULL;

----DAWANIE UPRAWNIEN

DROP FUNCTION IF EXISTS grant_permissions;
CREATE OR REPLACE FUNCTION grant_permissions(account_id INT, permission_type VARCHAR(10))
RETURNS VOID AS $$
BEGIN
    UPDATE Accounts
    SET user_permissions = permission_type
    WHERE id = account_id;
END;
$$ LANGUAGE plpgsql;


--CZY DOKTOR DOSTEPNY W PRZEDZIALE CZASU

DROP FUNCTION IF EXISTS check_doctor_availability;
CREATE OR REPLACE FUNCTION check_doctor_availability(
    spec_name VARCHAR(50),
    start_date DATE,
    end_date DATE
)
RETURNS BOOLEAN AS $$
DECLARE
    doctor_available BOOLEAN;
BEGIN
    SELECT EXISTS (
        SELECT 1
        FROM Vets v
        INNER JOIN Vets_Specialities vs ON v.id = vs.vet_id
        WHERE vs.name = spec_name
        AND NOT EXISTS (
            SELECT 1
            FROM Visits vi
            WHERE vi.vet_id = v.id
            AND (vi.visit_date >= start_date AND vi.visit_date <= end_date)
        )
    ) INTO doctor_available;

    RETURN doctor_available;
END;
$$ LANGUAGE plpgsql;

-- DODAJ PRACOWNIKA FUNKCJA
DROP FUNCTION IF EXISTS add_employee_with_person;
CREATE OR REPLACE FUNCTION add_employee_with_person(
  p_first_name VARCHAR(20),
  p_last_name VARCHAR(40),
  p_address VARCHAR(200),
  p_city VARCHAR(40),
  p_telephone VARCHAR(20),
  p_email VARCHAR(60),
  p_fav_animal VARCHAR(40),
  p_position INTEGER,
  p_salary NUMERIC(10, 2),
  p_date_start DATE
) RETURNS VOID AS $$
DECLARE
  v_person_id INTEGER;
  v_employee_id INTEGER;
BEGIN
  -- Dodaj osobę
  INSERT INTO Persons (first_name, last_name, address, city, telephone, email, fav_animal)
  VALUES (p_first_name, p_last_name, p_address, p_city, p_telephone, p_email, p_fav_animal)
  RETURNING id INTO v_person_id;

  SELECT COALESCE(MAX(id), 0) + 1 INTO v_employee_id FROM Employees;

  IF v_employee_id = 1 THEN
    ALTER SEQUENCE employees_id_seq RESTART;
  END IF;


  INSERT INTO Employees (id, person_id, position, salary, date_start)
  VALUES (v_employee_id, v_person_id, p_position, p_salary, p_date_start);
END;
$$ LANGUAGE plpgsql;


commit;
