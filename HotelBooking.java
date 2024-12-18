import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Statement;

public class HotelBooking {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "System@321sql";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. view reservation");
                System.out.println("3. get room number");
                System.out.println("4. update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("0. Exit");
                System.out.println("Choose a option : ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1 -> reserveRoom(con, sc);
                    case 2 -> viewReservation(con);
                    case 3 -> getRoomNumber(con, sc);
                    case 4 -> updateReservation(con, sc);
                    case 5 -> deleteReservation(con, sc);
                    case 0 -> {
                        exit();
                        sc.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice!! try again..");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void reserveRoom(Connection con, Scanner sc) {
        try {
            System.out.println("Enter guest name: ");
            String guestName = sc.next();
            System.out.println("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber = sc.next();
            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " + "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";
            try (Statement st = con.createStatement()) {
                int affectRows = st.executeUpdate(sql);

                if(affectRows > 0) {
                    System.out.println("Reservation Successful!!");
                }else {
                    System.out.println("Reservation Failed.");
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection con) throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations;";
        try (Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql)) {
            System.out.println("Current Reservation:");
            System.out.println("+.................+................+...............+.....................+...............................+");
            System.out.println("| Reservation ID  | Guest          |  Room Number  | Contact Number         | Reservation Date |");
            System.out.println("+.................+................+...............+.....................+...............................+");

            while(rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                String contactNumber = rs.getString("contact_number");
                String reservationDate = rs.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+.................+................+...............+.....................+...............................+");

        }
    }

    private static void getRoomNumber(Connection con, Scanner sc) {
    try {
        System.out.println("Enter reservation id: ");
        int reservationId = sc.nextInt();
        System.out.println("Enter guest name: ");
        String guestName = sc.next();

        String sql = "SELECT room_number FROM reservations " + "WHERE reservation_id = " + reservationId + " AND guest_name = '" + guestName + "'";

        try(Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                System.out.println("Room number for reservation id " + reservationId + " and Guest " + guestName + " is: " + roomNumber);
            } else {
                System.out.println("Reservation not found for the given id and guest name..");
            }
        }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private  static  void updateReservation(Connection con, Scanner sc) {
        try {
            System.out.println("Enter reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine();

            if(reservationExists(con, reservationId)) {
                System.out.println("Reservation not found...");
                return;
            }

            System.out.println("Enter new guest name :");
            String newGuestName = sc.next();
            System.out.println("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber = sc.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " + "room_number = " + newRoomNumber + ", " + "contact_number = '" + newContactNumber + "' " + "WHERE reservation_id = " + reservationId;

            try(Statement st = con.createStatement()) {
                int affectedRows = st.executeUpdate(sql);

                if(affectedRows > 0) {
                    System.out.println("Reservation updated successfully!!");
                }else {
                    System.out.println("Reservation updated failed..");
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deleteReservation(Connection con, Scanner sc) {
        try {
            System.out.println("Enter reservation Id to delete: ");
            int reservationId = sc.nextInt();
           if(reservationExists(con, reservationId)) {
               System.out.println("Reservation not found..");
               return;
           }

           String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

           try(Statement st = con.createStatement()) {
               int affectedRows = st.executeUpdate(sql);

               if(affectedRows > 0) {
                   System.out.println("Reservation deleted successfully!");
               }else {
                   System.out.println("Reservation deletion failed..");
               }
           }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean reservationExists(Connection con, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try(Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
                return !rs.next();
            }
          }catch(SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void exit() throws InterruptedException {
        System.out.println("Exiting System");
        int i=5;
        while(i!=0) {
            System.out.print(".");
            Thread.sleep(300);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using HOTEL RESERVATION SYSTEM!!");
    }
}
