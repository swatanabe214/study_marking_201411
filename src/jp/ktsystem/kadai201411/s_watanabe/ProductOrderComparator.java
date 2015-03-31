package jp.ktsystem.kadai201411.s_watanabe;

import java.util.Comparator;

import jp.ktsystem.kadai201411.common.ErrorCode;
import jp.ktsystem.kadai201411.common.KadaiException;

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

        if (date1 == null || date2 == null || id1 == null || id2 == null) {
            try {
                throw new KadaiException(ErrorCode.INCOMEFILE_FORMAT_ERROR.getErrorCode());
            } catch (KadaiException e) {
            }
        }
        if (date1.equals(date2)) {
            return id1.compareTo(id2);
        } else if (Double.parseDouble(date1) > Double.parseDouble(date2)) {
            return 1;
        } else {
            return -1;
        }
    }
}