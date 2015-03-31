package jp.ktsystem.kadai201411.common;

public enum ErrorCode {

    /**
     * エラーコード
     */
    // 1:受注情報ファイル入力エラー
    ORDERFILE_INPUT_ERROR(-1),
    // 2:受注情報ファイルフォーマットエラー
    ORDERFILE_FORMAT_ERROR(-2),
    // 3:入金情報ファイル入力エラー
    INCOMEFILE_INPUT_ERROR(-3),
    // 4:入金情報ファイルフォーマットエラー
    INCOMEFILE_FORMAT_ERROR(-4),
    // 5:生産指示ファイル出力エラー
    CREATEPRODUCTORDER_OUTPUT_ERROR(-5),
    // 6:退避ファイル入力エラー
    RESERVEFILE_INPUT_ERROR(-6),
    // 7:退避ファイル出力エラー
    RESERVEFILE_OUTPUT_ERROR(-7),
    // 8:問1のファイル出力エラー
    Q1FILE_OUTPUT_ERROR(-8),
    // 9:バックアップ失敗
    BACKUP_FAILURE(-9),
    // 10:計算エラー（オーバーフロー等）
    CALC_ERROR(-10);

    private int errorCode;

    /**
     * <p>エラーコードを設定します。</p>
     * @param errorCode
     */
    private ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * <p>エラーコードを返します。</p>
     * @return エラーコード
     */
    public int getErrorCode() {
        return errorCode;
    }

}