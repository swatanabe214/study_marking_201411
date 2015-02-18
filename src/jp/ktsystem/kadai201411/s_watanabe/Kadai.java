package jp.ktsystem.kadai201411.s_watanabe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import jp.ktsystem.kadai201411.common.AppConstants;
import jp.ktsystem.kadai201411.common.ErrorCode;
import jp.ktsystem.kadai201411.common.KadaiException;

public class Kadai {

    /**
     * <p>受注情報ファイルを読み込み、<br>
     * 製品名ごとに数量を集計して出力します。</p>
     *
     * @param String anOrderFileDir 受注情報ファイル入力ディレクトリ
     * @param String anOutputDir 出力ディレクトリ
     * @return 出力したレコードの件数（異常終了の場合はエラーコード）
     * @throws KadaiException エラー発生時投げる例外
     */
    public static int countOrder(String anOrderFileDir, String anOutputDir) {

        int countOrder = 0;
        List<String> orderFileNameList = new ArrayList<String>();
        List<String> backupFileNameList = new ArrayList<String>();
        List<String[]> orderInfoList = new ArrayList<String[]>();

        try {
            // 受注情報ファイル名のリストを作成
            Kadai_Lv1.inputOrderFileNameList(anOrderFileDir, orderFileNameList, backupFileNameList);

            // 受注情報ファイルのリストを作成
            Kadai_Lv1.readInputOrderFile(anOrderFileDir, orderFileNameList, orderInfoList);

            // 受注情報を出力し、レコード件数を返す
            countOrder = Kadai_Lv1.writeOrderCSV(orderInfoList, anOutputDir);
            return countOrder;

        } catch (KadaiException e) {
            return e.getErrorCode();
        }
    }

    /**
     * <p>受注情報ファイルと入金情報ファイルを読み込み、<br>
     * 両方に存在する受注IDをもつ情報を<br>
     * 生産指示ファイルに出力します。</p>
     *
     * @param String anOrderFileDir 受注情報ファイル入力ディレクトリ
     * @param String anIncomeFileDir 入金情報ファイル入力ディレクトリ
     * @param String anOutputDir 出力ディレクトリ
     * @param String aBackupDir バックアップファイル出力ディレクトリ
     * @return 出力したレコードの件数。異常終了の場合はエラーコード。
     * @throws KadaiException エラー発生時投げる例外
     */
    public static int createProductOrder(String anOrderFileDir, String anIncomeFileDir,
            String anOutputDir, String aBackupDir) {

        int countOrErrorCode = 0;
        List<String> orderFileNameList = new ArrayList<String>();
        List<String> backupFileNameList = new ArrayList<String>();
        List<String[]> orderInfoList = new ArrayList<String[]>();
        List<String[]> incomeInfoList = new ArrayList<String[]>();
        List<String> allReserveOrder = new ArrayList<String>();

        try {
            // 受注情報ファイル名のリストを作成
            Kadai_Lv1.inputOrderFileNameList(anOrderFileDir, orderFileNameList, backupFileNameList);

            // 退避ファイルを読み込む
            Kadai_Lv2.readReserveFile(anOutputDir, orderInfoList);

            // 受注情報ファイルのリストを作成
            Kadai_Lv1.readInputOrderFile(anOrderFileDir, orderFileNameList, orderInfoList);

            // 入金情報ファイルのリストを作成
            Kadai_Lv2.readIncomeFile(anIncomeFileDir, incomeInfoList);

            // 生産指示情報を出力し、レコード件数を返す
            countOrErrorCode = Kadai_Lv2.writeCreateProductOrderCSV(anOutputDir, orderInfoList, incomeInfoList, allReserveOrder);

            // 退避ファイルへの出力
            if (0 < allReserveOrder.size()) {
                writeReserveFile(anOutputDir, allReserveOrder);
            }

            // 処理済みの受注情報ファイルをバックアップディレクトリに移動
            if (0 < countOrErrorCode) {
                countOrErrorCode = Kadai_Lv2.removeOrderFile(aBackupDir, backupFileNameList, countOrErrorCode);
            }
            return countOrErrorCode;
        } catch (KadaiException e) {
            return e.getErrorCode();
        }
    }

    /**
     * <p>生産指示ファイルに出力しなかった受注情報が存在した場合、<br>
     * 出力されなかった受注情報をファイルに退避させ、<br>
     * 退避ファイルのディレクトリを返します。</p>
     *
     * @param String anOutputDir 出力ディレクトリ
     * @param List<String> reserveOrder 生産指示ファイルに出力されなかった受注情報リスト
     * @return 退避ファイルディレクトリ
     * @throws KadaiException エラー発生時投げる例外
     */
    private static String writeReserveFile(String anOutputDir, List<String> allReserveOrder) throws KadaiException {

        BufferedWriter bw = null;
        String reserveFileDir = anOutputDir + "\\" + AppConstants.RESERVE_DIR;

        try {
            // 退避ファイルディレクトリ作成
            File newDir = new File(reserveFileDir);
            newDir.mkdir();

            File file = new File(reserveFileDir + "\\" + AppConstants.RESERVEORDER_OUTPUTFILENAME);
            bw = new BufferedWriter(new OutputStreamWriter
                    (new FileOutputStream(file), AppConstants.CHARACTER_CODE));

            for (int i = 0; i < allReserveOrder.size(); i++) {
                bw.write(allReserveOrder.get(i));
                if (allReserveOrder.size() - 1 != i) {
                    bw.newLine(); // 最終行は改行しない
                }
            }

        } catch (IOException e) {
            throw new KadaiException(ErrorCode.RESERVEFILE_OUTPUT_ERROR.getErrorCode());
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                    bw.flush();
                } catch (IOException e) {
                    // finallyでは例外投げない
                }
            }
        }
        return reserveFileDir;
    }
}
