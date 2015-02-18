package jp.ktsystem.kadai201411.common;

/**
 * <h3>定数クラス</h3>
 *
 * @author s_watanabe
 *
 */
public class AppConstants {

	/** ファイル名開始文字：order */
	public static final String ORDERFILENAME_START = "order";
	/** ファイル拡張子：.txt */
	public static final String EXTENTION_TEXT = ".txt";
	/** ファイルエンコード：UTF-8 */
	public static final String CHARACTER_CODE = "UTF-8";
	/** 無効な演算子（BOM） */
	public static final String BOM = "\uFEFF";
	/** 受注情報出力ファイル名 */
    public static final String ORDER_OUTPUTFILENAME = "\\ordercount.out";
    /** 入金情報ファイル名 */
    public static final String INCOME_FILENAME = "income.txt";
    /** 受注情報：日付フォーマット */
    public static final String ORDER_DATEFORMAT = "yyyymmdd";
    /** 入金情報：日付フォーマット */
    public static final String INCOME_DATEFORMAT = "yyyymmddhhmmss";
    /** 生産指示情報ファイル名 */
    public static final String PRODUCTORDER_OUTPUTFILENAME = "productorder.out";
    /** 退避ファイルディレクトリ */
    public static final String RESERVE_DIR = "reserve";
    /** 退避ファイル名 */
    public static final String RESERVEORDER_OUTPUTFILENAME = "reservation.dat";

}
