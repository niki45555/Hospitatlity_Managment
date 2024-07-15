Integration with Reservation Process

Database Connection

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://your_host/your_database";
    private static final String USER = "your_user";
    private static final String PASSWORD = "your_password";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


Stored Procedure Integration

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelManagement {

    // Check Room Availability
    public List<Integer> checkRoomAvailability(int hotelId, String roomType, Date checkIn, Date checkOut) throws SQLException {
        List<Integer> availableRooms = new ArrayList<>();
        String call = "{call CheckRoomAvailability(?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            stmt.setInt(1, hotelId);
            stmt.setString(2, roomType);
            stmt.setDate(3, checkIn);
            stmt.setDate(4, checkOut);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    availableRooms.add(rs.getInt("RoomID"));
                }
            }
        }
        return availableRooms;
    }

    // User Login
    public int userLogin(String username, String password) throws SQLException {
        String call = "{call UserLogin(?, ?, ?)}";
        int userId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.execute();
            userId = stmt.getInt(3);
        }
        return userId;
    }

    // Register Hotel
    public int registerHotel(String hotelName, String location, String description) throws SQLException {
        String call = "{call RegisterHotel(?, ?, ?, ?)}";
        int hotelId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            stmt.setString(1, hotelName);
            stmt.setString(2, location);
            stmt.setString(3, description);
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.execute();
            hotelId = stmt.getInt(4);
        }
        return hotelId;
    }

    // Register Room
    public int registerRoom(int hotelId, String roomNumber, String roomType, double price) throws SQLException {
        String call = "{call RegisterRoom(?, ?, ?, ?, ?)}";
        int roomId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            stmt.setInt(1, hotelId);
            stmt.setString(2, roomNumber);
            stmt.setString(3, roomType);
            stmt.setDouble(4, price);
            stmt.registerOutParameter(5, Types.INTEGER);

            stmt.execute();
            roomId = stmt.getInt(5);
        }
        return roomId;
    }

    // Generate Bill
    public int generateBill(int reservationId) throws SQLException {
        String call = "{call GenerateBill(?, ?)}";
        int billId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            stmt.setInt(1, reservationId);
            stmt.registerOutParameter(2, Types.INTEGER);

            stmt.execute();
            billId = stmt.getInt(2);
        }
        return billId;
    }

    // Check-In
    public void checkIn(int reservationId) throws SQLException {
        String call = "{call CheckIn(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            stmt.setInt(1, reservationId);
            stmt.execute();
        }
    }

    // Check-Out
    public void checkOut(int reservationId) throws SQLException {
        String call = "{call CheckOut(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            stmt.setInt(1, reservationId);
            stmt.execute();
        }
    }

    public static void main(String[] args) {
        // Example usage
        HotelManagement hm = new HotelManagement();

        try {
            int hotelId = hm.registerHotel("Hotel California", "Los Angeles", "A lovely place");
            System.out.println("Registered Hotel ID: " + hotelId);

            int roomId = hm.registerRoom(hotelId, "101", "Suite", 299.99);
            System.out.println("Registered Room ID: " + roomId);

            List<Integer> availableRooms = hm.checkRoomAvailability(hotelId, "Suite", Date.valueOf("2024-07-20"), Date.valueOf("2024-07-25"));
            System.out.println("Available Rooms: " + availableRooms);

            int userId = hm.userLogin("john_doe", "password123");
            System.out.println("User ID: " + userId);

            int billId = hm.generateBill(1);
            System.out.println("Generated Bill ID: " + billId);

            hm.checkIn(1);
            System.out.println("Checked-In Reservation ID: 1");

            hm.checkOut(1);
            System.out.println("Checked-Out Reservation ID: 1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
