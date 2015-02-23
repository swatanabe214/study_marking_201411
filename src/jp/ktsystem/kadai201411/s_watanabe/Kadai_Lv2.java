package jp.ktsystem.kadai201411.s_watanabe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.ktsystem.kadai201411.common.AppConstants;
import jp.ktsystem.kadai201411.common.CommonUtill;
import jp.ktsystem.kadai201411.common.ErrorCode;
import jp.ktsystem.kadai201411.common.KadaiException;

public class Kadai_Lv2 {

    /**
     * <p>退避ファイルを読み込み、受注情報リストを作成します。<br>
     * 異常系の場合、エラーコードを投げます。</p>
     *
     * @param String anOutputDir 退避ファイルディレクトリ
     * @param List<String[]> orderInfoList 受注情報リスト
     * @throws KadaiException エラー発生時投げる例外
     */
    public static void readReserveFile(String anOutputDir, List<OrderData> allOrderData) throws KadaiException {

        if (null != anOutputDir) {

            File reserveFile = new File(anOutputDir + "\\" + AppConstants.RESERVEORDER_OUTPUTFILENAME);

            if (reserveFile.exists()) {
                if (0 < reserveFile.length()) {

                    BufferedReader br = null;
//                    String[] oneRecord = new String[5];
                    try {

                        br = new BufferedReader(new InputStreamReader
                                (new FileInputStream(reserveFile), AppConstants.CHARACTER_CODE));

                        OrderData oneOrderData = new OrderData();

                        // 最終行まで
                        String str = null;
                        while (null != (str = br.readLine())) {

                            // 初期化
                            oneOrderData = new OrderData();

                            String[] array = str.split(",", -1);
                            if (5 == array.length) {
                                oneOrderData.setOrderID(array[0]);
                                oneOrderData.setName(array[1]);
                                oneOrderData.setProductName(array[2]);
                                oneOrderData.setQuantity(array[3]);
                                oneOrderData.setDeliveryDate(array[4]);

                                // 配列をリストにつめる
                                allOrderData.add(oneOrderData);

                            } else {
                                throw new KadaiException(ErrorCode.RESERVEFILE_INPUT_ERROR.getErrorCode());
                            }
                        }

                    } catch (IOException e) {
                        throw new KadaiException(ErrorCode.RESERVEFILE_INPUT_ERROR.getErrorCode());
                    } finally {

                        if (null != br) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                // finallyでは例外投げない
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * <p>フォーマットチェックを行いながら入金情報ファイルを読み込み、<br>
     * 入金情報リストを作成します。<br>
     * 異常系の場合、エラーコードを投げます。</p>
     *
     * @param String anIncomeFileDir 入金情報ファイルの入力ディレクトリ
     * @param List<String[]> incomeInfoList 入金情報ファイルリスト
     * @throws KadaiException エラー発生時投げる例外
     */
    public static void readIncomeFile(String anIncomeFileDir, List<IncomeData> allIncomeData) throws KadaiException {

        BufferedReader br = null;

        // 日付フォーマット作成
        String format = AppConstants.INCOME_DATEFORMAT;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        // 日付の厳密チェックON
        dateFormat.setLenient(false);

        if (null == anIncomeFileDir) {
            throw new KadaiException(ErrorCode.INCOMEFILE_INPUT_ERROR.getErrorCode());
        }

        File incomeFile = new File(anIncomeFileDir + "\\" + AppConstants.INCOME_FILENAME);

        // 入金情報ファイル存在チェック
        if (incomeFile.exists()) {
            if (0 < incomeFile.length()) {

                try {

                    br = new BufferedReader(new InputStreamReader
                            (new FileInputStream(incomeFile), AppConstants.CHARACTER_CODE));

                    IncomeData oneIncomeData = new IncomeData();

                    // 最終行まで
                    String str = null;
                    while (null != (str = br.readLine())) {

                        // 初期化
                        oneIncomeData = new IncomeData();

                        // BOM除去
                        str = CommonUtill.skipBOM(str);

                        String[] array = str.split(",", -1);
                        if (2 == array.length) {

                            for (int i = 0; i < array.length; i++) {

                                // 【受注ID・入金日時】必須チェック
                                if (null == array[i] || "".equals(array[i])) {

                                    throw new KadaiException(ErrorCode.INCOMEFILE_FORMAT_ERROR.getErrorCode());

                                } else if (1 == i) {

                                    // 【入金日時】yyyymmddhhmmssフォーマットチェック
                                    try {
                                        dateFormat.parse(array[i]);
                                    } catch (ParseException e) {
                                        throw new KadaiException(ErrorCode.INCOMEFILE_FORMAT_ERROR.getErrorCode());
                                    }
                                }

                                // １レコードをデータリストにつめる
                                if (0 == i) {
                                    oneIncomeData.setOrderID(array[i]);
                                } else if (1 == i) {
                                    oneIncomeData.setDateAndTime(array[i]);
                                }
                            }

                            // 【受注ID】重複チェック
                            for (int j = 0; j < allIncomeData.size(); j++) {
                                if (allIncomeData.get(j).getOrderID().equals(oneIncomeData.getOrderID())) {

                                    // 【入金日時】早い方を正とする
                                    if (Long.parseLong(oneIncomeData.getDateAndTime().toString()) <= Long.parseLong(allIncomeData.get(j).getDateAndTime().toString())) {
                                        allIncomeData.remove(j);
                                    }
                                }
                            }
                            // リストにつめる
                            allIncomeData.add(oneIncomeData);

                        } else {
                            throw new KadaiException(ErrorCode.INCOMEFILE_FORMAT_ERROR.getErrorCode());
                        }
                    }

                } catch (IOException e) {
                    throw new KadaiException(ErrorCode.INCOMEFILE_INPUT_ERROR.getErrorCode());
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
                throw new KadaiException(ErrorCode.INCOMEFILE_FORMAT_ERROR.getErrorCode());
            }
        } else {
            throw new KadaiException(ErrorCode.INCOMEFILE_INPUT_ERROR.getErrorCode());
        }
    }

    /**
     * <p>生産指示情報を出力し、退避受注ファイルリストを作成します。<br>
     * 異常系の場合、エラーコードを返します。</p>
     *
     * @param String anOutputDir 出力ディレクトリ
     * @param List<String[]> orderInfoList 受注情報リスト
     * @param List<String[]> incomeInfoList 入金情報リスト
     * @param List<String> allReserveOrder 退避受注情報リスト
     * @return 出力したレコード件数。またはエラーコード。
     * @throws KadaiException エラー発生時投げる例外
     */
    public static int writeCreateProductOrderCSV(String anOutputDir, List<OrderData> allOrderData,
            List<IncomeData> allIncomeData, List<String> allReserveOrder) throws KadaiException {

        int countOrErrorCode = 0;
        String productOutputFilePath = null;
        BufferedWriter bw = null;

        if (null != anOutputDir) {

            File productOrderFileDir = new File(anOutputDir);

            // ファイルディレクトリ存在チェック
            if (productOrderFileDir.exists()) {
                productOutputFilePath = productOrderFileDir + "\\" + AppConstants.PRODUCTORDER_OUTPUTFILENAME;

                try {

                    bw = new BufferedWriter(new OutputStreamWriter
                            (new FileOutputStream(productOutputFilePath), AppConstants.CHARACTER_CODE));

                    ProductOrder oneProductOrder = new ProductOrder();
                    List<ProductOrder> allProductOrder = new ArrayList<ProductOrder>();
                    List<String> reserveOrder = new ArrayList<String>();

                    // 受注情報リスト
                    for (int i = 0; i < allOrderData.size(); i++) {

                        // 初期化
                        oneProductOrder = new ProductOrder();
                        reserveOrder.clear();
                        String orderID = null;
                        int k = 0;

                        // １件分の受注情報
                        OrderData items = allOrderData.get(i);
                        orderID = items.getOrderID();

                        // 退避受注情報リスト
                        reserveOrder.add(items.getOrderID() + "," + items.getName() + "," + items.getProductName() + ","
                                + items.getQuantity() + "," + items.getDeliveryDate());
                        allReserveOrder.addAll(reserveOrder);

                        // 入金情報リスト
                        for (int j = 0; j < allIncomeData.size(); j++) {

                            if (orderID.equals(allIncomeData.get(j).getOrderID())) {

                                // 生産指示情報に入金日時追加
                                oneProductOrder.setOrderID(items.getOrderID());
                                oneProductOrder.setName(items.getName());
                                oneProductOrder.setProductName(items.getProductName());
                                oneProductOrder.setQuantity(items.getQuantity());
                                oneProductOrder.setDeliveryDate(items.getDeliveryDate());
                                oneProductOrder.setDateAndTime(allIncomeData.get(j).getDateAndTime());

                                allProductOrder.add(oneProductOrder);
                                // レコード件数
                                countOrErrorCode++;

                                // 退避受注情報リストから生産指示情報を削除
                                if (0 < allReserveOrder.size()) {
                                    k = allReserveOrder.indexOf(items.getOrderID() + "," + items.getName() + "," + items.getProductName() + ","
                                            + items.getQuantity() + "," + items.getDeliveryDate());
                                    allReserveOrder.remove(k);
                                }
                            }
                            reserveOrder.clear();
                        }
                    }

                    // ソート：入金日時の昇順。入金日時が同じ場合は受注IDの昇順。
                    Collections.sort(allProductOrder, new ProductOrderComparator());

                    // 生産指示ファイルへの出力
                    for (int l = 0; l < allProductOrder.size(); l++) {
                        bw.write(allProductOrder.get(l).getOrderID() + "," + allProductOrder.get(l).getName() + "," +
                                allProductOrder.get(l).getProductName() + "," + allProductOrder.get(l).getQuantity() + "," +
                                allProductOrder.get(l).getDeliveryDate() + "," + allProductOrder.get(l).getDateAndTime());
                        if (allProductOrder.size() - 1 != l) {
                            bw.newLine(); // 最終行は改行しない
                        }
                    }

                } catch (IOException e) {
                    return ErrorCode.CREATEPRODUCTORDER_OUTPUT_ERROR.getErrorCode();
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
                return ErrorCode.CREATEPRODUCTORDER_OUTPUT_ERROR.getErrorCode();
            }
        }
        return countOrErrorCode;
    }

    /**
     * <p>処理済みの受注情報ファイルをバックアップディレクトリに移動します。</p>
     *
     * @param String aBackupDir バックアップファイル出力ディレクトリ
     * @param List<String> backupFileNameList バックアップファイル名リスト
     * @param int countOrErrorCode 出力したレコード件数
     * @return 出力したレコード件数(参照渡し)。異常系の場合、エラーコード。
     */
    public static int removeOrderFile(String aBackupDir, List<String> backupFileNameList, int countOrErrorCode) {

        // 移動先ディレクトリ
        File dir = new File(aBackupDir);
        if (null != aBackupDir && dir.exists()) {
            for (int i = 0; i < backupFileNameList.size(); i++) {
                File file = new File(backupFileNameList.get(i));
                // 移動
                file.renameTo(new File(dir, file.getName()));
            }
        } else {
            return ErrorCode.BACKUP_FAILURE.getErrorCode();
        }
        return countOrErrorCode;
    }
}
