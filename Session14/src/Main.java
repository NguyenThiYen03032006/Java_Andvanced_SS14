import service.FlashSaleService;
import dao.ReportDAO;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        FlashSaleService service = new FlashSaleService();

        for (int i = 1; i <= 50; i++) {
            int userId = (i % 5) + 1;

            new Thread(() -> {
                Map<Integer, Integer> items = new HashMap<>();
                items.put(1, 1);

                service.placeOrder(userId, items);
            }).start();
        }

        Thread.sleep(5000);

        new ReportDAO().getTopBuyers();
    }
}
