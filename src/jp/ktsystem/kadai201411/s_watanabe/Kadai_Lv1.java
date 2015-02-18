package jp.ktsystem.kadai201411.s_watanabe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jp.ktsystem.kadai201411.common.AppConstants;
import jp.ktsystem.kadai201411.common.CommonUtill;
import jp.ktsystem.kadai201411.common.ErrorCode;
import jp.ktsystem.kadai201411.common.KadaiException;

public class Kadai_Lv1 {

    /**
     * <p>受注情報ファイルの入力ディレクトリを読み込み、<br>
     * ディレクトリ内のファイルのリストを作成します。<br>
     * 異常系の場合、エラーコードを投げます。</p>
     *
     * @param String anOrderFileDir 受注情報ファイル入力ディレクトリ
     * @param List<String> orderFileNameList ディレクトリ内のファイル名リスト
     * @param List<String> backupFileNameList バックアップ時用のファイル名リスト
     * @throws KadaiException エラー発生時投げる例外
     */
    public static void inputOrderFileNameList(String anOrderFileDir, List<String> orderFileNameList, List<String> backupFileNameList) throws KadaiException {

        if (null != anOrderFileDir) {

            File orderFileDir = new File(anOrderFileDir);

            // ファイルディレクトリ存在チェック
            if (orderFileDir.exists()) {

                String fileList[] = orderFileDir.list();

                // ディレクトリ内の要素チェック
                for (int i = 0; i < fileList.length; i++) {

                    if (null != fileList[i] && !fileList[i].isEmpty()) {

                        String fileName = null;
                        String backupFile = null;
                        fileName = fileList[i];
                        backupFile = orderFileDir + "\\" + fileList[i];

                        if (fileName.startsWith(AppConstants.ORDERFILENAME_START)
                                && fileName.endsWith(AppConstants.EXTENTION_TEXT)) {

                            // 詰めなおし
                            orderFileNameList.add(fileName);
                            // バックアップ用
                            backupFileNameList.add(backupFile);

                        } else {
                            throw new KadaiException(ErrorCode.ORDERFILE_INPUT_ERROR.getErrorCode());
                        }
                    }
                }

                // ファイル名ソート
                Collections.sort(orderFileNameList);

            } else {
                throw new KadaiException(ErrorCode.ORDERFILE_INPUT_ERROR.getErrorCode());
            }

        } else {
            throw new KadaiException(ErrorCode.ORDERFILE_INPUT_ERROR.getErrorCode());
        }
    }

    /**
     * <p>フォーマットチェックをしながらファイルを読み込み、<br>
     * 受注情報リストを作成します。<br>
     * 異常系の場合、エラーコードを投げます。</p>
     *
     * @param String aFileDir 受注情報ファイル入力ディレクトリ
     * @param ArrayList<String> orderFileNameList ディレクトリ内のファイル名リスト
     * @param List<String[]> orderInfoList 受注情報リスト
     * @throws KadaiException エラー発生時投げる例外
     */
    public static void readInputOrderFile(String aFileDir, List<String> orderFileNameList,
            List<String[]> orderInfoList) throws KadaiException {

        String[] oneRecord = new String[5];

        // 日付フォーマット作成
        String format = AppConstants.ORDER_DATEFORMAT;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        // 日付の厳密チェックON
        dateFormat.setLenient(false);

        // 受注情報ファイルを一つずつ処理する
        for (int i = 0; i < orderFileNameList.size(); i++) {
            String filePath = aFileDir + "\\" + (String) orderFileNameList.get(i);

            if (null != filePath) {

                File file = new File(filePath);

                if (file.exists()) {
                    if (0 < file.length()) {

                        BufferedReader br = null;
                        try {

                            br = new BufferedReader(new InputStreamReader
                                    (new FileInputStream(file), AppConstants.CHARACTER_CODE));

                            // 最終行まで
                            String str = null;

                            while (null != (str = br.readLine())) {

                                // BOM除去
                                str = CommonUtill.skipBOM(str);

                                String[] array = str.split(",", -1);
                                if (5 == array.length) {

                                    for (int j = 0; j < array.length; j++) {

                                        // 【受注ID・顧客名・製品名・数量】必須チェック
                                        if (4 != j && (null == array[j] || "".equals(array[j]))) {

                                            throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());

                                        } else if (3 == j) {

                                            // 【数量】整数チェック
                                            try {
                                                Double.parseDouble(array[j]);
                                            } catch (NumberFormatException e) {
                                                throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                                            }

                                        } else if (4 == j) {

                                            // 【納期】yyyymmddフォーマットチェック
                                            if (!"".equals(array[j])) {
                                                try {
                                                    dateFormat.parse(array[j]);
                                                } catch (ParseException e) {
                                                    throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                                                }
                                            }
                                        }

                                        // １ファイルの要素を配列につめる
                                        oneRecord[j] = array[j];
                                    }

                                    // 【受注ID】重複チェック
                                    for (int k = 0; k < i; k++) {
                                        if (orderInfoList.get(k)[0].equals(oneRecord[0])) {
                                            orderInfoList.remove(k);
                                        }
                                    }

                                    // 配列をリストにつめる
                                    orderInfoList.add(oneRecord.clone());

                                } else {
                                    throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                                }
                            }

                        } catch (IOException e) {

                            throw new KadaiException(ErrorCode.ORDERFILE_INPUT_ERROR.getErrorCode());

                        } finally {

                            if (null != br) {
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    // finallyでは例外投げない
                                }
                            }
                        }
                    } else {
                        throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                    }
                } else {
                    throw new KadaiException(ErrorCode.ORDERFILE_INPUT_ERROR.getErrorCode());
                }
            }
        }
    }

    /**
     * <p>受注情報リストから、製品と受注数量の一覧を出力し、<br>
     * 出力したレコード件数を返します。<br>
     * 異常系の場合、エラーコードを返します。</p>
     *
     * @param List<String[]> inputOrderInfoList 受注情報ファイルリスト
     * @param String anOutputDir 出力ディレクトリ
     * @return 出力したレコードの件数または、エラーコード
     * @throws KadaiException エラー発生時投げる例外
     */
    public static int writeOrderCSV(List<String[]> orderInfoList, String anOutputDir) throws KadaiException {

        int countOrder = 0;
        String outputFilePath = null;
        BufferedWriter bw = null;

        if (null != anOutputDir) {

            File orderFileDir = new File(anOutputDir);

            // ファイルディレクトリ存在チェック
            if (orderFileDir.exists()) {
                outputFilePath = orderFileDir + AppConstants.ORDER_OUTPUTFILENAME;

                try {

                    bw = new BufferedWriter(new OutputStreamWriter
                            (new FileOutputStream(outputFilePath), AppConstants.CHARACTER_CODE));

                    List<String> oneOrder = new ArrayList<String>();
                    List<String> allOrder = new ArrayList<String>();

                    for (int i = 0; i < orderInfoList.size(); i++) {

                        // 初期化
                        oneOrder.clear();
                        int count = 0;
                        int index = 0;
                        int total = 0;

                        // 受注情報１件分
                        String[] items = orderInfoList.get(i);
                        count = Integer.parseInt(items[3]);

                        for (int j = 0; j < allOrder.size(); j++) {

                            if (allOrder.get(j).contains(items[2])) {
                                // 【製品名】重複チェック
                                index = allOrder.get(j).indexOf(",");
                                // 重複した製品の数量合計
                                total = Integer.parseInt(allOrder.get(j).substring(index + 1)) + count;
                                // 重複した製品名・数量をリストから削除
                                allOrder.remove(allOrder.indexOf(allOrder.get(j)));
                                count = total;
                            }
                        }
                        // 【製品名】【数量】１レコードをリストに追加
                        oneOrder.add(items[2] + "," + count);
                        allOrder.addAll(oneOrder);
                    }

                    // レコード件数
                    countOrder = allOrder.size();

                    // 出力順：昇順
                    Collator collator = Collator.getInstance(Locale.JAPANESE);
                    Collections.sort(allOrder, collator);

                    // 出力ファイルへ書き込み
                    for (int k = 0; k < allOrder.size(); k++) {
                        bw.write(allOrder.get(k));
                        if (allOrder.size() - 1 != k) {
                            bw.newLine(); // 最終行は改行しない
                        }
                    }

                } catch (IOException e) {
                    throw new KadaiException(ErrorCode.Q1FILE_OUTPUT_ERROR.getErrorCode());
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

            } else {
                throw new KadaiException(ErrorCode.Q1FILE_OUTPUT_ERROR.getErrorCode());
            }
        }
        return countOrder;
    }
}
