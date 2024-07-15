Database Schema Design

Code:-

-- Create Users table
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    Password VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Role VARCHAR(20) NOT NULL
);

-- Create Hotels table
CREATE TABLE Hotels (
    HotelID INT AUTO_INCREMENT PRIMARY KEY,
    HotelName VARCHAR(100) NOT NULL,
    Location VARCHAR(100) NOT NULL,
    Description TEXT
);

-- Create Rooms table
CREATE TABLE Rooms (
    RoomID INT AUTO_INCREMENT PRIMARY KEY,
    HotelID INT,
    RoomNumber VARCHAR(10) NOT NULL,
    RoomType VARCHAR(50) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (HotelID) REFERENCES Hotels(HotelID)
);

-- Create Reservations table
CREATE TABLE Reservations (
    ReservationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    RoomID INT,
    CheckInDate DATE NOT NULL,
    CheckOutDate DATE NOT NULL,
    Status VARCHAR(20) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID)
);

-- Create Bills table
CREATE TABLE Bills (
    BillID INT AUTO_INCREMENT PRIMARY KEY,
    ReservationID INT,
    Amount DECIMAL(10, 2) NOT NULL,
    IssueDate DATE NOT NULL,
    Status VARCHAR(20) NOT NULL,
    FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID)
);
