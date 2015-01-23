package jp.ktsystem.kadai201411.common;

/**
 * <p>共通処理クラスです。</p>
 *
 * @author s_watanabe
 *
 */
public class CommonUtill {

    /**
     * <p>BOMをスキップします。</p>
     *
     * @param str ファイルの一行
     * @return BOM除去後のファイルの一行
     */
    public static String skipBOM(String str) {

        if (str.startsWith(AppConstants.BOM)) {

            // BOMを取り外す
            str = str.substring(1);

        }

        return str;

    }
}
