import java.sql.*;
public class ConnectorJdbc {
    public static void main(String args[]) throws ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "System@321sql";
        String query = "select * from employees;";
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
        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String job = rs.getString("job");
            double salary = rs.getDouble("salary");

            System.out.println();
            System.out.println("=========================");
            System.out.println("Id : "+ id);
            System.out.println("Name: "+ name);
            System.out.println("Job : "+ job);
            System.out.println("Salary : "+ salary);
        }
    }catch (SQLException e) {
        System.out.println(e.getMessage());
    }
 }
}