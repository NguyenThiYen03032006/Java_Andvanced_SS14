package dao;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDAO {

    public int getStock(int productId) throws Exception {
        String sql = "SELECT stock FROM products WHERE id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("stock");
        }
        return 0;
    }

    // trừ stock (QUAN TRỌNG)
    public boolean decreaseStock(int productId, int quantity) throws Exception {
        String sql = "UPDATE product SET stock = stock - ? WHERE id = ? AND stock >= ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, quantity);
        ps.setInt(2, productId);
        ps.setInt(3, quantity);

        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
