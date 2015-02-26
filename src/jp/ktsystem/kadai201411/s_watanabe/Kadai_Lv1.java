package jp.ktsystem.kadai201411.s_watanabe;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jp.ktsystem.kadai201411.common.AppConstants;
import jp.ktsystem.kadai201411.common.ErrorCode;
import jp.ktsystem.kadai201411.common.FileUtil;
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

                File fileList[] = orderFileDir.listFiles();

                // ディレクトリ内の要素チェック
                for (int i = 0; i < fileList.length; i++) {

                    String fileName = null;
                    String backupFile = null;
                    fileName = fileList[i].getName();
                    backupFile = orderFileDir + "\\" + fileName;

                    if (fileName.startsWith(AppConstants.ORDERFILENAME_START)
                            && fileName.endsWith(AppConstants.EXTENTION_TEXT)) {

                        // 詰めなおし
                        orderFileNameList.add(fileName);
                        // バックアップ用
                        backupFileNameList.add(backupFile);
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
            List<OrderData> allOrderData) throws KadaiException {

        // 日付フォーマット作成
        String format = AppConstants.ORDER_DATEFORMAT;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        // 日付の厳密チェックON
        dateFormat.setLenient(false);

        int count = 0;
        // 受注情報ファイルを一つずつ処理する
        for (int i = 0; i < orderFileNameList.size(); i++) {

            String filePath = aFileDir + "\\" + (String) orderFileNameList.get(i);

            if (null != filePath) {

                File file = new File(filePath);

                if (file.exists()) {
                    if (0 < file.length()) {

                        try {

                            // ファイル読み込み
                            List<String> fileStrList = new ArrayList<String>();
                            fileStrList.addAll(FileUtil.readFile(file));

                            OrderData oneOrderData = new OrderData();

                            int rowCount = 0;
                            // 最終行まで
                            for (String str : fileStrList) {

                                oneOrderData = new OrderData();
                                count++;
                                rowCount++;

                                if (!"".equals(str) || rowCount != fileStrList.size()) {

                                    String[] array = str.split(",", -1);
                                    if (5 == array.length) {

                                        for (int j = 0; j < array.length; j++) {

                                            // 【受注ID・顧客名・製品名・数量】必須チェック
                                            if (4 != j && (null == array[j] || "".equals(array[j]))) {

                                                throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                                            }

                                            switch (j) {
                                            case 0:
                                                oneOrderData.setOrderID(array[j]);
                                                break;
                                            case 1:
                                                oneOrderData.setName(array[j]);
                                                break;
                                            case 2:
                                                oneOrderData.setProductName(array[j]);
                                                break;
                                            case 3:
                                                // 【数量】整数チェック
                                                if (!array[j].matches("^[0-9]+$")) {
                                                    throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                                                }
                                                oneOrderData.setQuantity(array[j]);
                                                break;
                                            case 4:
                                                // 【納期】yyyymmddフォーマットチェック
                                                if (!"".equals(array[j])) {
                                                    try {
                                                        dateFormat.parse(array[j]);
                                                    } catch (ParseException e) {
                                                        throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                                                    }
                                                }
                                                oneOrderData.setDeliveryDate(array[j]);
                                                break;
                                            }
                                        }

                                        // 【受注ID】重複チェック
                                        for (int k = 0; k < count - 1; k++) {
                                            if (allOrderData.get(k).getOrderID().equals(oneOrderData.getOrderID())) {
                                                allOrderData.remove(k);
                                                count--;
                                            }
                                        }

                                        // 配列をリストにつめる
                                        allOrderData.add(oneOrderData);

                                    } else {
                                        throw new KadaiException(ErrorCode.ORDERFILE_FORMAT_ERROR.getErrorCode());
                                    }
                                }
                            }

                        } catch (IOException e) {
                            throw new KadaiException(ErrorCode.ORDERFILE_INPUT_ERROR.getErrorCode());
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
    public static int writeOrderCSV(List<OrderData> allOrderData, String anOutputDir) throws KadaiException {

        int countOrder = 0;
        String outputFilePath = null;

        if (null != anOutputDir) {

            File orderFileDir = new File(anOutputDir);

            // ファイルディレクトリ存在チェック
            if (orderFileDir.exists()) {
                outputFilePath = orderFileDir + AppConstants.ORDER_OUTPUTFILENAME;

                try {

                    List<String> oneOrder = new ArrayList<String>();
                    List<String> allOrder = new ArrayList<String>();

                    for (int i = 0; i < allOrderData.size(); i++) {

                        // 初期化
                        oneOrder.clear();
                        int count = 0;
                        int index = 0;
                        int total = 0;

                        // 受注情報１件分
                        OrderData item = allOrderData.get(i);
                        count = Integer.parseInt(item.getQuantity());

                        for (int j = 0; j < allOrder.size(); j++) {

                            if (allOrder.get(j).contains(item.getProductName())) {
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
                        oneOrder.add(item.getProductName() + "," + count);
                        allOrder.addAll(oneOrder);
                    }

                    // レコード件数
                    countOrder = allOrder.size();

                    if (0 < countOrder) {

                        // 出力順：昇順
                        Collator collator = Collator.getInstance(Locale.JAPANESE);
                        Collections.sort(allOrder, collator);

                        // 出力ファイルへ書き込み
                        FileUtil.writeFile(outputFilePath, allOrder);
                    }

                } catch (IOException e) {
                    throw new KadaiException(ErrorCode.Q1FILE_OUTPUT_ERROR.getErrorCode());
                }

            } else {
                throw new KadaiException(ErrorCode.Q1FILE_OUTPUT_ERROR.getErrorCode());
            }
        }
        return countOrder;
    }
}
