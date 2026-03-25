package dao;

import utils.DBConnection;
import java.sql.*;

public class ReportDAO {

    public void getTopBuyers() throws Exception {
        String sql = "{CALL GetTopBuyers()}";

        Connection conn = DBConnection.getConnection();
        CallableStatement cs = conn.prepareCall(sql);

        ResultSet rs = cs.executeQuery();

        System.out.println("TOP BUYERS:");
        while (rs.next()) {
            System.out.println(
                    rs.getInt("id") + " - " +
                            rs.getString("name") + " - " +
                            rs.getInt("total_orders")
            );
        }
    }
}
