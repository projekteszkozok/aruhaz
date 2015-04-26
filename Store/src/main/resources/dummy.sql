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

CREATE TABLE CUSTOMER(
	CUSTOMER_ID INT PRIMARY KEY,
	"NAME" VARCHAR(20),
	ADDRESS VARCHAR(50),
	TELEPHONE VARCHAR(15),
	EMAIL VARCHAR(30)
);

CREATE TABLE MANUFACTURER(
	MANUFACTURER_ID INT PRIMARY KEY,
	"NAME" VARCHAR(40),
	CONTACT_NAME VARCHAR(20),
	CITY VARCHAR(20),
	TELEPHONE VARCHAR(15),
	STORE_ID INT CONSTRAINT FK_MA_ST REFERENCES STORE ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE PRODUCT(
	PRODUCT_ID INT PRIMARY KEY,
	"NAME" VARCHAR(30),
	MANUFACTURER_ID INT CONSTRAINT FK_PR_MA REFERENCES MANUFACTURER ON DELETE CASCADE ON UPDATE RESTRICT,
	DESCRIPTION VARCHAR(100),
	CATEGORY_ID INT CONSTRAINT FK_PR_CA REFERENCES CATEGORY ON DELETE CASCADE ON UPDATE RESTRICT,
	PRICE INT,
	STOCK INT,
	ACTIVE BOOLEAN
);

CREATE TABLE "ORDER"(
	ORDER_ID INT PRIMARY KEY,
	PRODUCT_ID INT CONSTRAINT FK_OR_PR REFERENCES PRODUCT ON DELETE CASCADE ON UPDATE RESTRICT,
	CUSTOMER_ID INT CONSTRAINT FK_OR_CU REFERENCES CUSTOMER ON DELETE CASCADE ON UPDATE RESTRICT
);

INSERT INTO SEQ VALUES (60);

INSERT INTO CATEGORY VALUES
(1, 'Alaplap'),
(2, 'HDD'),
(3, 'RAM'),
(4, 'Optikai meghajtó'),
(5, 'Pendrive'),
(6, 'Processzor'),
(7, 'Számítógépház'),
(8, 'Tápegység'),
(9, 'Videókártya');

INSERT INTO STORE VALUES
(10, 'Edigital', 'Budapest'),
(11, 'PCLand', 'Szeged'),
(12, 'EStore', 'Sopron'),
(13, 'Media Markt', 'Budapest'),
(14, 'Euronics', 'Kecskemét'),
(15, 'AVPlanet', 'Zalakaros'),
(16, 'Best Byte', 'Tata'),
(17, 'Aqua', 'Budapest');

INSERT INTO CUSTOMER VALUES
(18, 'Kiss Béla', '9999 Hajó utca 20.', '+36105678944', 'kissbela@customer.hu'),
(19, 'Forgács Eszter', '9123 Virág utca 195.', '+36401234567', 'eszter.forgacs@mail.hu'),
(20, 'Virág Árpád', '1234 Mezõ utca 72.', '+36801234598', 'arpika@watermail.com'),
(21, 'Kolompár Emánuel', '7456 Vass utca 19.', '+36801234123', 'emanuel@vasmuvek.hu'),
(22, 'Répási Réka', '6352 Nyúl utca 144.', '+36401121123','reka_nyuszi@amail.com'),
(23, 'Kis Virág', '1835 Föld utca 40.', '+36905555555','kiss@virag.hu');

INSERT INTO MANUFACTURER VALUES
(24, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 10),
(25, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 11),
(26, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 12),
(27, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 13),
(28, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 14),
(29, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 15),
(30, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 16),
(31, 'ASUS Magyarország', 'Azúsz Ferenc', 'Budapest', '+36888888888', 17),
(32, 'Intel Hungary Ltd.', 'Intel Béla', 'Budapest', '+36505555555', 10),
(33, 'Intel Hungary Ltd.', 'Intel Béla', 'Budapest', '+36505555555', 11),
(34, 'Intel Hungary Ltd.', 'Intel Béla', 'Budapest', '+36505555555', 12),
(35, 'Intel Hungary Ltd.', 'Intel Béla', 'Budapest', '+36505555555', 13),
(36, 'Intel Hungary Ltd.', 'Intel Béla', 'Budapest', '+36505555555', 14),
(37, 'Intel Hungary Ltd.', 'Intel Béla', 'Budapest', '+36505555555', 15),
(38, 'Intel Hungary Ltd.', 'Intel Béla', 'Budapest', '+36505555555', 16),
(39, 'Advanced Micro Devices', 'Edvenszd Júlia', 'Pécs', '+36201233123', 10),
(40, 'Advanced Micro Devices', 'Edvenszd Júlia', 'Pécs', '+36201233123', 16),
(41, 'Advanced Micro Devices', 'Edvenszd Júlia', 'Pécs', '+36201233123', 17),
(42, 'NVIDIA Corporation', 'Dzsíforsz Pista', 'Budapest', '+36103733732', 10),
(43, 'NVIDIA Corporation', 'Dzsíforsz Pista', 'Budapest', '+36103733732', 11),
(44, 'NVIDIA Corporation', 'Dzsíforsz Pista', 'Budapest', '+36103733732', 12),
(45, 'NVIDIA Corporation', 'Dzsíforsz Pista', 'Budapest', '+36103733732', 17),
(46, 'ATI Technologies', 'Rédon Róbert', 'Pécs', '+36301212121', 15),
(47, 'ATI Technologies', 'Rédon Róbert', 'Pécs', '+36301212121', 17);

INSERT INTO PRODUCT VALUES
(48, 'GeForce GTX 750 Ti', 42, 'Maxwell architectúrára épülõ kártya. Kifejezetten jó játékokhoz.', 9, 52000, 5, TRUE),
(49, 'GeForce GTX 750 Ti', 44, 'Maxwell architectúrára épülõ kártya. Kifejezetten jó játékokhoz.', 9, 49000, 2, FALSE),
(50, 'GeForce GTX 750 Ti', 45, 'Maxwell architectúrára épülõ kártya. Kifejezetten jó játékokhoz.', 9, 59200, 12, TRUE),
(51, 'Radeon HD 5870', 46, 'DirectX 11-es támogatás, 3 képernyõ támogatása ', 9, 59200, 12, TRUE),
(52, 'Core i7-4770K', 34, '4 magos. Foglalat: 1155, Órajel: 3500Mhz', 6, 99950, 4, TRUE),
(53, 'Core i7-4770K', 37, '4 magos. Foglalat: 1155, Órajel: 3500Mhz', 6, 99950, 5, TRUE);

INSERT INTO "ORDER" VALUES
(54, 48, 18),
(55, 52, 19),
(56, 48, 20),
(57, 51, 22),
(58, 50, 23),
(59, 53, 23);
