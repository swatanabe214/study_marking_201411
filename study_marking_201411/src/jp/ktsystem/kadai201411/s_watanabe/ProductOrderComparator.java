package jp.ktsystem.kadai201411.s_watanabe;

import java.util.Comparator;

/**
 * <p>生産指示ファイルをソートするクラスです。</p>
 *
 * @author s_watanabe
 *
 */
public class ProductOrderComparator implements Comparator<ProductOrder> {

    public int compare(ProductOrder o1, ProductOrder o2) {

        String date1 = o1.getDateAndTime();
        String date2 = o2.getDateAndTime();

        String id1 = o1.getOrderID();
        String id2 = o2.getOrderID();

        if (date1 == date2) {
            return Integer.parseInt(id1) - Integer.parseInt(id2);
        } else if (Double.parseDouble(date1) > Double.parseDouble(date2)) {
            return 1;
        } else {
            return -1;
        }
    }
}
