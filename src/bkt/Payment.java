package bkt;
import java.math.BigDecimal;
import java.sql.*;

public class Payment {
    public static void main(String[] args) {
        String fromId = "ACC01";
        String toId = "ACC02";
        BigDecimal amount = new BigDecimal("1000");
        try (
                Connection conn = DBConnection.openConnection()
        ) {
            conn.setAutoCommit(false);
            String checkSql = "SELECT Balance FROM Accounts WHERE AccountId = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setString(1, fromId);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new RuntimeException("Tai khoan khong hop le");
                }
                BigDecimal balance = rs.getBigDecimal("Balance");
                if (balance.compareTo(amount) < 0) {
                    throw new RuntimeException("So du khong du");
                }
            }
            CallableStatement call = conn.prepareCall("{call sp_UpdateBalance(?, ?)}");
            call.setString(1, fromId);
            call.setBigDecimal(2, amount.negate());
            call.execute();
            call.setString(1, toId);
            call.setBigDecimal(2, amount);
            call.execute();
            conn.commit();
            System.out.println("Chuyen tien thanh cong!");
            String query = "SELECT AccountId, FullName, Balance FROM Accounts WHERE AccountId IN (?, ?)";
            try (
                    PreparedStatement ps = conn.prepareStatement(query)
            ) {
                ps.setString(1, fromId);
                ps.setString(2, toId);
                ResultSet rs = ps.executeQuery();
                System.out.println("\n===== GIAO DICH THANH CONG =====");
                System.out.printf("%-10s %-20s %-10s\n", "AccountId", "FullName", "Balance");
                while (rs.next()) {
                    System.out.printf("%-10s %-20s %-10.2f\n",
                            rs.getString("AccountId"),
                            rs.getString("FullName"),
                            rs.getBigDecimal("Balance"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Xay ra loi:  rollback!");
            try (Connection conn = DBConnection.openConnection()) {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}