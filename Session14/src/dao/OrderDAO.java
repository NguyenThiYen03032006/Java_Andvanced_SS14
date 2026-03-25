package dao;

import utils.DBConnection;

import java.sql.*;
import java.util.Map;

public class OrderDAO {

    public int createOrder(int userId) throws Exception {
        String sql = "INSERT INTO orders(user_id) VALUES(?)";
        Connection conn = DBConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, userId);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public void insertOrderDetails(int orderId, Map<Integer, Integer> items) throws Exception {
        String sql = "INSERT INTO order_details(order_id, product_id, quantity) VALUES(?,?,?)";
        Connection conn = DBConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql);

        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            ps.setInt(1, orderId);
            ps.setInt(2, entry.getKey());
            ps.setInt(3, entry.getValue());
            ps.addBatch();
        }

        ps.executeBatch();
    }
}
