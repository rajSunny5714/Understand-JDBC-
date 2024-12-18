import java.sql.*;
public class Update {
    public static void main(String args[]) throws ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "System@321sql";
        String query = "UPDATE employees\n" + "SET job = 'Java Developer', salary = 7700000.0\n" + "WHERE id = 1;";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        } catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("connection established successfully");
            Statement st = con.createStatement();
            int rowsaffected = st.executeUpdate(query);

            if(rowsaffected>0) {
                System.out.println("Updation Successfully"+rowsaffected+ "row(s) affected.");
            }else {
                System.out.println("Updation Failed..");
            }
            st.close();
            con.close();
            System.out.println();
            System.out.println("connection closed successfully!!!");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

