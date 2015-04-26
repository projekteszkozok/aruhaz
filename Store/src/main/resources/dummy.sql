CREATE TABLE SEQ (VALUUE INT);

CREATE TABLE CATEGORY(
    CATEGORY_ID INT PRIMARY KEY,
    "NAME" VARCHAR(20)
);

CREATE TABLE STORE(
	STORE_ID INT PRIMARY KEY,
	"NAME" VARCHAR(20),
	PLACE VARCHAR(30)
);



INSERT INTO SEQ VALUES (18);

INSERT INTO CATEGORY VALUES
(1, 'Alaplap'),
(2, 'HDD'),
(3, 'RAM'),
(4, 'Optikai meghajt�'),
(5, 'Pendrive'),
(6, 'Processzor'),
(7, 'Sz�m�t�g�ph�z'),
(8, 'T�pegys�g'),
(9, 'Vide�k�rtya');

INSERT INTO STORE VALUES
(10, 'Edigital', 'Budapest'),
(11, 'PCLand', 'Szeged'),
(12, 'EStore', 'Sopron'),
(13, 'Media Markt', 'Budapest'),
(14, 'Euronics', 'Kecskem�t'),
(15, 'AVPlanet', 'Zalakaros'),
(16, 'Best Byte', 'Tata'),
(17, 'Aqua', 'Budapest');
