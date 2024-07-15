Checking Room Availability

Code:-

CREATE PROCEDURE CheckRoomAvailability(
    IN hotel_id INT,
    IN room_type VARCHAR(50),
    IN check_in DATE,
    IN check_out DATE
)
BEGIN
    SELECT RoomID
    FROM Rooms
    WHERE HotelID = hotel_id
      AND RoomType = room_type
      AND RoomID NOT IN (
          SELECT RoomID
          FROM Reservations
          WHERE (CheckInDate < check_out) AND (CheckOutDate > check_in)
      );
END;

User Login

Code:-

CREATE PROCEDURE UserLogin(
    IN username VARCHAR(50),
    IN password VARCHAR(50),
    OUT user_id INT
)
BEGIN
    SELECT UserID INTO user_id
    FROM Users
    WHERE Username = username
      AND Password = password;
END;

Register Rooms and Hotels

Code:-

CREATE PROCEDURE RegisterHotel(
    IN hotel_name VARCHAR(100),
    IN location VARCHAR(100),
    IN description TEXT,
    OUT hotel_id INT
)
BEGIN
    INSERT INTO Hotels (HotelName, Location, Description)
    VALUES (hotel_name, location, description);
    SET hotel_id = LAST_INSERT_ID();
END;

CREATE PROCEDURE RegisterRoom(
    IN hotel_id INT,
    IN room_number VARCHAR(10),
    IN room_type VARCHAR(50),
    IN price DECIMAL(10, 2),
    OUT room_id INT
)
BEGIN
    INSERT INTO Rooms (HotelID, RoomNumber, RoomType, Price)
    VALUES (hotel_id, room_number, room_type, price);
    SET room_id = LAST_INSERT_ID();
END;

Generate Bill

Code:-

CREATE PROCEDURE GenerateBill(
    IN reservation_id INT,
    OUT bill_id INT
)
BEGIN
    DECLARE total_amount DECIMAL(10, 2);
    
    SELECT SUM(Price * DATEDIFF(CheckOutDate, CheckInDate))
    INTO total_amount
    FROM Rooms r
    JOIN Reservations res ON r.RoomID = res.RoomID
    WHERE res.ReservationID = reservation_id;
    
    INSERT INTO Bills (ReservationID, Amount, IssueDate, Status)
    VALUES (reservation_id, total_amount, CURDATE(), 'unpaid');
    
    SET bill_id = LAST_INSERT_ID();
END;

Check-In and Check-Out Maintenance

Code:-

CREATE PROCEDURE CheckIn(
    IN reservation_id INT
)
BEGIN
    UPDATE Reservations
    SET Status = 'checked-in'
    WHERE ReservationID = reservation_id;
END;

CREATE PROCEDURE CheckOut(
    IN reservation_id INT
)
BEGIN
    UPDATE Reservations
    SET Status = 'checked-out'
    WHERE ReservationID = reservation_id;
END;
