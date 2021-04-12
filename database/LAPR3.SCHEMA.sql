CREATE TABLE Client (
  Fk_Person_NIF             number(20) NOT NULL, 
  Fk_Residential_Address_Id number(10) NOT NULL, 
  PRIMARY KEY (Fk_Person_NIF));
CREATE TABLE Courier (
  Fk_Person_NIF  number(20) NOT NULL, 
  Fk_Pharmacy_Id number(10) NOT NULL, 
  Weight         number(4,1) NOT NULL, 
  IsWorking      varchar2(255) DEFAULT 'true' NOT NULL, 
  PRIMARY KEY (Fk_Person_NIF));
CREATE TABLE Pharmacy (
  Id_Pharmacy            number(10) NOT NULL, 
  Name                   varchar2(255) NOT NULL, 
  Maximum_Payload        number(5,2) NOT NULL, 
  Minimum_Payload        number(5,2) NOT NULL, 
  Fk_Address_Id          number(10) NOT NULL, 
  Fk_Administrator_Email varchar2(255) NOT NULL, 
  IsActive               varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Pharmacy));
CREATE TABLE Product (
  Id_Product  number(10) NOT NULL, 
  Description varchar2(255) NOT NULL, 
  Price       number(10,2) NOT NULL, 
  Weight      number(6,3) NOT NULL, 
  PRIMARY KEY (Id_Product));
CREATE TABLE Order_Entry (
  Id_Order               number(20) NOT NULL, 
  Date_Entry             date NOT NULL, 
  Fk_Pharmacy_Id         number(10) NOT NULL, 
  Fk_Client_NIF          number(20) NOT NULL, 
  Fk_Shipment_Address_Id number(10) NOT NULL, 
  Fk_Credit_Card_Numero  varchar2(255) NOT NULL, 
  Delivery_Fee           number(4,2) NOT NULL, 
  PRIMARY KEY (Id_Order));
CREATE TABLE Vehicle (
  Id_Vehicle         number(10) NOT NULL, 
  Fk_Pharmacy_Id     number(10) NOT NULL, 
  Fk_Battery_Id      number(10) NOT NULL, 
  Fk_Vehicle_Type_Id number(10) NOT NULL, 
  QRCode             number(10) NOT NULL, 
  Weight             number(4,1) NOT NULL, 
  IsAvailable        varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Vehicle));
CREATE TABLE Order_Detail (
  Fk_Order_Id   number(20) NOT NULL, 
  Fk_Product_Id number(10) NOT NULL, 
  Quantity      number(10) NOT NULL, 
  FK_Id_VAT     number(10) NOT NULL, 
  PRIMARY KEY (Fk_Order_Id, 
  Fk_Product_Id));
CREATE TABLE Order_Type_Status (
  Id_Order_Type_Status number(10) NOT NULL, 
  Description          varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Order_Type_Status));
CREATE TABLE Order_Status (
  Fk_Order_Id  number(20) NOT NULL, 
  Fk_Status_Id number(10) NOT NULL, 
  Date_Entry   date NOT NULL, 
  PRIMARY KEY (Fk_Order_Id, 
  Fk_Status_Id));
CREATE TABLE Park (
  Fk_Pharmacy_Id           number(10) NOT NULL, 
  Scooter_Chargers_Number  number(10) NOT NULL, 
  Drone_Chargers_Number    number(10) NOT NULL, 
  Scooter_Charger_Capacity number(10) NOT NULL, 
  Drone_Charger_Capacity   number(10) NOT NULL, 
  PRIMARY KEY (Fk_Pharmacy_Id));
CREATE TABLE Person (
  NIF   number(20) NOT NULL, 
  Name  varchar2(255) NOT NULL, 
  Email varchar2(255) NOT NULL, 
  PRIMARY KEY (NIF));
CREATE TABLE Invoice (
  Fk_Order_Id number(20) NOT NULL, 
  Invoice_TAG varchar2(255) NOT NULL, 
  Total_Cost  number(10,2) NOT NULL, 
  PRIMARY KEY (Fk_Order_Id));
CREATE TABLE Address (
  Id_Address number(10) NOT NULL, 
  Latitude   number(20,10) NOT NULL,
  Longitude  number(20,10) NOT NULL,
  Elevation  number(20,3) NOT NULL, 
  Address    varchar2(255) NOT NULL, 
  Zip_Code   varchar2(20) NOT NULL, 
  PRIMARY KEY (Id_Address));
CREATE TABLE Pharmacy_Product (
  Id_Transaction number(10) NOT NULL, 
  Fk_Pharmacy_Id number(10) NOT NULL, 
  Fk_Product_Id  number(10) NOT NULL, 
  Quantity       number(10) NOT NULL, 
  Date_Entry     date NOT NULL, 
  PRIMARY KEY (Id_Transaction));
CREATE TABLE Battery (
  Id_Battery number(10) NOT NULL, 
  Capacity   number(10,3) NOT NULL, 
  PRIMARY KEY (Id_Battery));
CREATE TABLE Vehicle_Type_Status (
  Id_Vehicle_Type_Status number(10) NOT NULL, 
  Description            varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Vehicle_Type_Status));
CREATE TABLE Vehicle_Status (
  Id_Vehicle_Status         number(10) NOT NULL, 
  Fk_Vehicle_Id             number(10) NOT NULL, 
  Fk_Vehicle_Type_Status_Id number(10) NOT NULL, 
  Date_Entry                date NOT NULL, 
  ActualCharge              number(10) NOT NULL, 
  PRIMARY KEY (Id_Vehicle_Status));
CREATE TABLE Payment (
  Fk_Order_Id number(20) NOT NULL, 
  Date_Entry  date NOT NULL, 
  PRIMARY KEY (Fk_Order_Id));
CREATE TABLE Credits (
  Fk_Order_Id   number(20) NOT NULL, 
  Fk_Client_NIF number(20) NOT NULL, 
  EarnedCredits number(10) NOT NULL, 
  Date_Entry    date NOT NULL, 
  PRIMARY KEY (Fk_Order_Id));
CREATE TABLE Credit_Card (
  Numero        varchar2(255) NOT NULL, 
  CCV           number(3) NOT NULL, 
  Valid_Thru    date NOT NULL, 
  Fk_Person_NIF number(20) NOT NULL, 
  PRIMARY KEY (Numero));
CREATE TABLE Delivery (
  Id_Delivery number(20) NOT NULL, 
  Date_Entry  date NOT NULL, 
  EnergyCost  number(10) NOT NULL, 
  PRIMARY KEY (Id_Delivery));
CREATE TABLE Delivery_Stops (
  Id_Delivery_Stops number(20) GENERATED ALWAYS as IDENTITY,
  Fk_Delivery_Id number(20) NOT NULL, 
  Fk_Order_Id    number(20) ,
  Fk_Courier_NIF number(20) NOT NULL, 
  Fk_Vehicle_Id  number(10) NOT NULL, 
  PRIMARY KEY (Id_Delivery_Stops));
CREATE TABLE VAT (
  Id_VAT number(10) NOT NULL, 
  PRIMARY KEY (Id_VAT));
CREATE TABLE User_Entry (
  Email           varchar2(255) NOT NULL, 
  Password        varchar2(255) NOT NULL, 
  Fk_User_Role_Id number(10) NOT NULL, 
  PRIMARY KEY (Email));
CREATE TABLE User_Role (
  Id_User_Role number(10) NOT NULL, 
  Description  varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_User_Role));
CREATE TABLE Courier_Type_Status (
  Id_Courier_Type_Status number(10) NOT NULL, 
  Description            varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Courier_Type_Status));
CREATE TABLE Courier_Status (
  Id_Courier_Status         number(10) NOT NULL, 
  Fk_Person_NIF             number(20) NOT NULL, 
  FK_Courier_Type_Status_Id number(10) NOT NULL, 
  Date_Entry                date NOT NULL, 
  PRIMARY KEY (Id_Courier_Status));
CREATE TABLE Road_Restriction (
  Id_Road_Restriction number(10) NOT NULL, 
  Fk_Address_Id_Start number(10) NOT NULL, 
  Fk_Address_Id_End   number(10) NOT NULL, 
  PRIMARY KEY (Id_Road_Restriction));
CREATE TABLE Vehicle_Type (
  Id_Vehicle_Type number(10) NOT NULL, 
  Description     varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Vehicle_Type));
CREATE TABLE Back_Order (
  Id_Back_Order           number(10) NOT NULL, 
  Fk_Pharmacy_Id_Client   number(10) NOT NULL, 
  Fk_Pharmacy_Id_Provider number(10) NOT NULL, 
  Fk_Product_Id           number(10) NOT NULL, 
  Quantity                number(10) NOT NULL, 
  Date_Entry              date NOT NULL, 
  PRIMARY KEY (Id_Back_Order));
CREATE TABLE Transfer (
  Id_Transfer               number(10) NOT NULL, 
  FK_Pharmacy_Id_Issuer     number(10) NOT NULL, 
  FK_Pharmacy_Id_Receiver   number(10) NOT NULL, 
  FK_Document_TransferNote  number(10) NOT NULL,
  FK_Document_Delivery_Note number(10) ,
  Fk_Order_Id               number(20) NOT NULL, 
  FK_Product_Id             number(10) NOT NULL, 
  Quantity                  number(10) NOT NULL, 
  Date_Entry                date NOT NULL, 
  PRIMARY KEY (Id_Transfer));
CREATE TABLE Transfer_Document (
  Id_Document     number(10) NOT NULL, 
  DocumentContent varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Document));
CREATE TABLE Transfer_Status (
   FK_Transfer_Id number(10) NOT NULL, 
  FK_Transfer_Status_Id       number(10) NOT NULL, 
  Date_Entry                  date NOT NULL,
  PRIMARY KEY ( FK_Transfer_Id, 
  FK_Transfer_Status_Id));
CREATE TABLE Transfer_Type_Status (
  Id_Transfer_Type_Status number(10) NOT NULL, 
  Description             varchar2(255) NOT NULL, 
  PRIMARY KEY (Id_Transfer_Type_Status));
CREATE TABLE Aerial_Restriction (
  id_Aerial_Restriction number(10) NOT NULL, 
  Fk_Address_Origin     number(10) NOT NULL, 
  Fk_Address_End        number(10) NOT NULL, 
  PRIMARY KEY (id_Aerial_Restriction));
ALTER TABLE Order_Detail ADD CONSTRAINT FKOrder_Deta422583 FOREIGN KEY (Fk_Product_Id) REFERENCES Product (Id_Product);
ALTER TABLE Order_Detail ADD CONSTRAINT FKOrder_Deta442673 FOREIGN KEY (Fk_Order_Id) REFERENCES Order_Entry (Id_Order);
ALTER TABLE Order_Entry ADD CONSTRAINT FKOrder_Entr148808 FOREIGN KEY (Fk_Client_NIF) REFERENCES Client (Fk_Person_NIF);
ALTER TABLE Order_Status ADD CONSTRAINT FKOrder_Stat699541 FOREIGN KEY (Fk_Order_Id) REFERENCES Order_Entry (Id_Order);
ALTER TABLE Order_Status ADD CONSTRAINT FKOrder_Stat55054 FOREIGN KEY (Fk_Status_Id) REFERENCES Order_Type_Status (Id_Order_Type_Status);
ALTER TABLE Courier ADD CONSTRAINT FKCourier208553 FOREIGN KEY (Fk_Pharmacy_Id) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Vehicle ADD CONSTRAINT FKVehicle337728 FOREIGN KEY (Fk_Pharmacy_Id) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Order_Entry ADD CONSTRAINT FKOrder_Entr873177 FOREIGN KEY (Fk_Pharmacy_Id) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Park ADD CONSTRAINT FKPark906453 FOREIGN KEY (Fk_Pharmacy_Id) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Courier ADD CONSTRAINT FKCourier178724 FOREIGN KEY (Fk_Person_NIF) REFERENCES Person (NIF);
ALTER TABLE Client ADD CONSTRAINT FKClient707624 FOREIGN KEY (Fk_Person_NIF) REFERENCES Person (NIF);
ALTER TABLE Invoice ADD CONSTRAINT FKInvoice374667 FOREIGN KEY (Fk_Order_Id) REFERENCES Order_Entry (Id_Order);
ALTER TABLE Pharmacy ADD CONSTRAINT FKPharmacy845816 FOREIGN KEY (Fk_Address_Id) REFERENCES Address (Id_Address);
ALTER TABLE Client ADD CONSTRAINT FKClient747272 FOREIGN KEY (Fk_Residential_Address_Id) REFERENCES Address (Id_Address);
ALTER TABLE Order_Entry ADD CONSTRAINT FKOrder_Entr94897 FOREIGN KEY (Fk_Shipment_Address_Id) REFERENCES Address (Id_Address);
ALTER TABLE Pharmacy_Product ADD CONSTRAINT has FOREIGN KEY (Fk_Pharmacy_Id) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Pharmacy_Product ADD CONSTRAINT has2 FOREIGN KEY (Fk_Product_Id) REFERENCES Product (Id_Product);
ALTER TABLE Vehicle ADD CONSTRAINT FKVehicle786577 FOREIGN KEY (Fk_Battery_Id) REFERENCES Battery (Id_Battery);
ALTER TABLE Vehicle_Status ADD CONSTRAINT FKVehicle_St591303 FOREIGN KEY (Fk_Vehicle_Id) REFERENCES Vehicle (Id_Vehicle);
ALTER TABLE Payment ADD CONSTRAINT FKPayment485960 FOREIGN KEY (Fk_Order_Id) REFERENCES Invoice (Fk_Order_Id);
ALTER TABLE Credits ADD CONSTRAINT FKCredits46472 FOREIGN KEY (Fk_Client_NIF) REFERENCES Client (Fk_Person_NIF);
ALTER TABLE Credit_Card ADD CONSTRAINT FKCredit_Car991242 FOREIGN KEY (Fk_Person_NIF) REFERENCES Client (Fk_Person_NIF);
ALTER TABLE Vehicle_Status ADD CONSTRAINT FKVehicle_St896571 FOREIGN KEY (Fk_Vehicle_Type_Status_Id) REFERENCES Vehicle_Type_Status (Id_Vehicle_Type_Status);
ALTER TABLE Credits ADD CONSTRAINT FKCredits940801 FOREIGN KEY (Fk_Order_Id) REFERENCES Order_Entry (Id_Order);
ALTER TABLE Delivery_Stops ADD CONSTRAINT FKDelivery_S203474 FOREIGN KEY (Fk_Delivery_Id) REFERENCES Delivery (Id_Delivery);
ALTER TABLE Delivery_Stops ADD CONSTRAINT FKDelivery_S863938 FOREIGN KEY (Fk_Order_Id) REFERENCES Order_Entry (Id_Order);
ALTER TABLE Delivery_Stops ADD CONSTRAINT FKDelivery_S29683 FOREIGN KEY (Fk_Courier_NIF) REFERENCES Courier (Fk_Person_NIF);
ALTER TABLE Delivery_Stops ADD CONSTRAINT FKDelivery_S157540 FOREIGN KEY (Fk_Vehicle_Id) REFERENCES Vehicle (Id_Vehicle);
ALTER TABLE Order_Detail ADD CONSTRAINT FKOrder_Deta178683 FOREIGN KEY (FK_Id_VAT) REFERENCES VAT (Id_VAT);
ALTER TABLE Person ADD CONSTRAINT FKPerson915434 FOREIGN KEY (Email) REFERENCES User_Entry (Email);
ALTER TABLE User_Entry ADD CONSTRAINT FKUser_Entry345154 FOREIGN KEY (Fk_User_Role_Id) REFERENCES User_Role (Id_User_Role);
ALTER TABLE Pharmacy ADD CONSTRAINT FKPharmacy26211 FOREIGN KEY (Fk_Administrator_Email) REFERENCES User_Entry (Email);
ALTER TABLE Courier_Status ADD CONSTRAINT FKCourier_St306625 FOREIGN KEY (FK_Courier_Type_Status_Id) REFERENCES Courier_Type_Status (Id_Courier_Type_Status);
ALTER TABLE Courier_Status ADD CONSTRAINT FKCourier_St148049 FOREIGN KEY (Fk_Person_NIF) REFERENCES Courier (Fk_Person_NIF);
ALTER TABLE Road_Restriction ADD CONSTRAINT FKRoad_Restr961139 FOREIGN KEY (Fk_Address_Id_Start) REFERENCES Address (Id_Address);
ALTER TABLE Road_Restriction ADD CONSTRAINT FKRoad_Restr591 FOREIGN KEY (Fk_Address_Id_End) REFERENCES Address (Id_Address);
ALTER TABLE Vehicle ADD CONSTRAINT FKVehicle454029 FOREIGN KEY (Fk_Vehicle_Type_Id) REFERENCES Vehicle_Type (Id_Vehicle_Type);
ALTER TABLE Order_Entry ADD CONSTRAINT FKOrder_Entr798363 FOREIGN KEY (Fk_Credit_Card_Numero) REFERENCES Credit_Card (Numero);
ALTER TABLE Back_Order ADD CONSTRAINT FKBack_Order259963 FOREIGN KEY (Fk_Pharmacy_Id_Client) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Back_Order ADD CONSTRAINT FKBack_Order593908 FOREIGN KEY (Fk_Product_Id) REFERENCES Product (Id_Product);
ALTER TABLE Back_Order ADD CONSTRAINT FKBack_Order557652 FOREIGN KEY (Fk_Pharmacy_Id_Provider) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Transfer ADD CONSTRAINT FKTransfer933333 FOREIGN KEY (FK_Document_TransferNote) REFERENCES Transfer_Document (Id_Document);
ALTER TABLE Transfer ADD CONSTRAINT FKTransfer583332 FOREIGN KEY (FK_Product_Id) REFERENCES Product (Id_Product);
ALTER TABLE Transfer ADD CONSTRAINT FKTransfer44819 FOREIGN KEY (FK_Pharmacy_Id_Issuer) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Transfer ADD CONSTRAINT FKTransfer602376 FOREIGN KEY (Fk_Order_Id) REFERENCES Order_Entry (Id_Order);
ALTER TABLE Transfer ADD CONSTRAINT FKTransfer294442 FOREIGN KEY (FK_Pharmacy_Id_Receiver) REFERENCES Pharmacy (Id_Pharmacy);
ALTER TABLE Transfer_Status ADD CONSTRAINT FKTransfer_S688465 FOREIGN KEY ( FK_Transfer_Id) REFERENCES Transfer (Id_Transfer);
ALTER TABLE Transfer_Status ADD CONSTRAINT FKTransfer_S808840 FOREIGN KEY (FK_Transfer_Status_Id) REFERENCES Transfer_Type_Status (Id_Transfer_Type_Status);
ALTER TABLE Transfer ADD CONSTRAINT FKTransfer508384 FOREIGN KEY (FK_Document_Delivery_Note) REFERENCES Transfer_Document (Id_Document);
ALTER TABLE Aerial_Restriction ADD CONSTRAINT FKAerial_Res689653 FOREIGN KEY (Fk_Address_Origin) REFERENCES Address (Id_Address);
ALTER TABLE Aerial_Restriction ADD CONSTRAINT FKAerial_Res782939 FOREIGN KEY (Fk_Address_End) REFERENCES Address (Id_Address);
