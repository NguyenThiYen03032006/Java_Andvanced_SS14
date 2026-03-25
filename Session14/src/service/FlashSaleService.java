package service;

import dao.OrderDAO;
import dao.ProductDAO;
import utils.DBConnection;

import java.sql.Connection;
import java.util.Map;

public class FlashSaleService {

    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    public void placeOrder(int userId, Map<Integer, Integer> items) {

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // 1. trừ stock
            for (Map.Entry<Integer, Integer> item : items.entrySet()) {
                if (!productDAO.decreaseStock(item.getKey(), item.getValue())) {
                    throw new RuntimeException("Hết hàng");
                }
            }

            // 2. tạo order
            int orderId = orderDAO.createOrder(userId);

            // 3. chi tiết đơn
            orderDAO.insertOrderDetails(orderId, items);

            conn.commit();
            System.out.println("SUCCESS user " + userId);

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("FAILED user " + userId);

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
