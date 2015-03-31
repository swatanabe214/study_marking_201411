package jp.ktsystem.kadai201411.s_watanabe;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 課題テストクラス
 *
 * @author s_watanabe
 *
 */
@SuppressWarnings("deprecation")
@RunWith(JUnit4.class)
public class KadaiTest201411 {

    private static final String FILE_DIRECTORY = "C:\\pleiades\\kadai\\study_marking_201411\\";

    /**
     * Lv_1：正常系
     */
    @Test
    public void testCase001() {
        assertEqualsLv1("inputTest\\order_info_01(正常系)", "outputTest\\order_info_01", 2);
    }

    /**
     * Lv_1：納期フォーマットエラー
     */
    @Test
    public void testCase002() {
        assertEqualsFailLv1("inputTest\\order_info_02(納期フォーマット)", "outputTest\\order_info_02", -2);
    }

    /**
     * Lv_1：ファイルフォーマットエラー
     */
    @Test
    public void testCase003() {
        assertEqualsFailLv1("inputTest\\order_info_03(2件連続)", "outputTest\\order_info_03", -2);
    }

    /**
     * Lv_1：重複複数
     */
    @Test
    public void testCase004() {
        assertEqualsLv1("inputTest\\order_info_04(複数データ)", "outputTest\\order_info_04", 7);
    }

    /**
     * Lv_1：BOM
     */
    @Test
    public void testCase005() {
        assertEqualsLv1("inputTest\\order_info_05(BOM)", "outputTest\\order_info_05", 5);
    }

    /**
     * Lv_1：数量フォーマットエラー
     */
    @Test
    public void testCase006() {
        assertEqualsFailLv1("inputTest\\order_info_06(数量フォーマット)", "outputTest\\order_info_06", -2);
    }

    /**
     * Lv_1：必須項目エラー
     */
    @Test
    public void testCase007() {
        assertEqualsFailLv1("inputTest\\order_info_07(必須)", "outputTest\\order_info_07", -2);
    }

    /**
     * Lv_1：入力ディレクトリ存在しないエラー
     */
    @Test
    public void testCase008() {
        assertEqualsFailLv1("noDir", "outputTest\\order_info_06", -1);
    }

    /**
     * Lv_1：出力ディレクトリ存在しないエラー
     */
    @Test
    public void testCase009() {
        assertEqualsFailLv1("inputTest\\order_info_01(正常系)", "noDir", -8);
    }

    /**
     * Lv_1：空ファイルエラー
     */
    @Test
    public void testCase010() {
        assertEqualsFailLv1("inputTest\\order_info_09(空ファイル)", "outputTest\\order_info_01", -2);
    }

    /**
     * Lv2：正常系
     */
    @Test
    public void testCase011() {
        assertEqualsLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_01(正常系)",
                "outputTest\\productorder_info_01", "outputTest\\productorder_info_01", 2);
    }

    /**
     * Lv2：必須項目ないエラー
     */
    @Test
    public void testCase012() {
        assertEqualsFailLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_02(必須)",
                "outputTest\\productorder_info_01", "outputTest\\productorder_info_02", -4);
    }

    /**
     * Lv2：入金日時フォーマットエラー
     */
    @Test
    public void testCase013() {
        assertEqualsFailLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_03(入金日時フォーマット)",
                "outputTest\\productorder_info_03", "outputTest\\productorder_info_03", -4);
    }

    /**
     * Lv2：ID重複
     */
    @Test
    public void testCase014() {
        assertEqualsLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_04(ID重複)",
                "outputTest\\productorder_info_04", "outputTest\\productorder_info_04", 2);
    }

    /**
     * Lv2：空ファイルエラー
     */
    @Test
    public void testCase015() {
        assertEqualsFailLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_05(空ファイル)",
                "outputTest\\productorder_info_05", "outputTest\\productorder_info_05", -4);
    }

    /**
     * Lv2：入力ディレクトリエラー
     */
    @Test
    public void testCase016() {
        assertEqualsFailLv2("inputTest\\order_info_01(正常系)", "noDir",
                "outputTest\\productorder_info_01", "outputTest\\productorder_info_01", -3);
    }

    /**
     * Lv2：出力ディレクトリエラー
     */
    @Test
    public void testCase017() {
        assertEqualsFailLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_01(正常系)",
                "noDir", "outputTest\\productorder_info_01", -5);
    }

    /**
     * Lv2：バックアップディレクトリエラー
     */
    @Test
    public void testCase018() {
        assertEqualsFailLv2("inputTest\\order_info_08(退避)", "inputTest\\income_info_01(正常系)",
                "outputTest\\productorder_info_06", "noDir", -9);
    }

    /**
     * Lv2：退避ファイル入力エラー
     */
    @Test
    public void testCase019() {
        assertEqualsLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_01(正常系)",
                "outputTest\\productorder_info_02", "outputTest\\productorder_info_01", -6);
    }

    /**
     * Lv2：退避ファイル出力エラー
     */
    @Test
    public void testCase020() {
        assertEqualsLv2("inputTest\\order_info_01(正常系)", "inputTest\\income_info_01(正常系)",
                "noDir", "outputTest\\productorder_info_01", -7);
    }

    /**
     * Lv1正常系テスト
     *
     * @param anOrderFileDir 受注情報ファイルディレクトリ
     * @param anOutputDir 出力ファイルディレクトリ
     * @param anCountOrder 正常終了時の出力件数
     */
    private void assertEqualsLv1(String anOrderFileDir, String anOutputDir, int anCountOrder) {

        Assert.assertEquals(anCountOrder, Kadai.countOrder(FILE_DIRECTORY + anOrderFileDir, FILE_DIRECTORY + anOutputDir));
    }

    /**
     * Lv1異常系テスト
     *
     * @param anOrderFileDir 受注情報ファイルディレクトリ
     * @param anOutputDir 出力ファイルディレクトリ
     * @param anErrorCode エラーコード
     */
    private void assertEqualsFailLv1(String anOrderFileDir, String anOutputDir, int anErrorCode) {

        Assert.assertEquals(anErrorCode, Kadai.countOrder(FILE_DIRECTORY + anOrderFileDir, FILE_DIRECTORY + anOutputDir));
    }

    /**
     * Lv2正常系テスト
     *
     * @param anOrderFileDir
     * @param anIncomeFileDir
     * @param anOutputDir
     * @param aBackupDir
     * @param aCountCreateProductOrder
     */
    private void assertEqualsLv2(String anOrderFileDir, String anIncomeFileDir,
            String anOutputDir, String aBackupDir, int aCountCreateProductOrder) {

        Assert.assertEquals(aCountCreateProductOrder, Kadai.createProductOrder(FILE_DIRECTORY + anOrderFileDir,
                FILE_DIRECTORY + anIncomeFileDir, FILE_DIRECTORY + anOutputDir, FILE_DIRECTORY + aBackupDir));
    }

    /**
     * Lv2異常系テスト
     *
     * @param anOrderFileDir
     * @param anIncomeFileDir
     * @param anOutputDir
     * @param aBackupDir
     * @param anErrorCode
     */
    private void assertEqualsFailLv2(String anOrderFileDir, String anIncomeFileDir,
            String anOutputDir, String aBackupDir, int anErrorCode) {

        Assert.assertEquals(anErrorCode, Kadai.createProductOrder(FILE_DIRECTORY + anOrderFileDir,
                FILE_DIRECTORY + anIncomeFileDir, FILE_DIRECTORY + anOutputDir, FILE_DIRECTORY + aBackupDir));

    }

}