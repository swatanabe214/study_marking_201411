package jp.ktsystem.kadai201411.s_watanabe;

import java.util.Comparator;

/**
 * <p>受注情報ファイル名をソートするクラスです。</p>
 *
 * @author s_watanabe
 *
 */
public class OrderFileComparator implements Comparator<String> {

    public int compare(String o1, String o2) {

        if (0 == o1.compareToIgnoreCase(o2)) {

            return o1.compareTo(o2);

        } else {

            return o1.compareToIgnoreCase(o2);

        }
    }
}
