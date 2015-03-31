package jp.ktsystem.kadai201411.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>ファイルのUtilクラスです。</p>
 *
 * @author s_watanabe
 *
 */
public class FileUtil {

    /**
     * <p>BOMをスキップします。</p>
     *
     * @param String str ファイルの一行
     * @return BOM除去後のファイルの一行
     */
    public static String skipBOM(String str) {

        if (str.startsWith(AppConstants.BOM)) {

            // BOMを取り外す
            str = str.substring(1);

        }

        return str;

    }

    /**
     * <p>ファイルを読み込みます。</p>
     *
     * @param File aFile 読み込むファイル
     * @return 読み込んだファイルのリスト
     * @throws IOException
     */
    public static List<String> readFile(File aFile) throws IOException {

        String str = null;
        List<String> list = new ArrayList<String>();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new InputStreamReader
                    (new FileInputStream(aFile), AppConstants.CHARACTER_CODE));

            while (null != (str = br.readLine())) {

                // BOM除去
                str = skipBOM(str);
                list.add(str);
            }
        } catch (IOException e) {
            throw e;
        } finally {

            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    // finallyでは例外投げない
                }
            }
        }
        return list;
    }

    /**
     * <p>ファイルに書き込みます</p>
     *
     * @param String aFilePath 書き込みファイルパス
     * @param List<String> aList 書き込むリスト
     * @throws IOException
     */
    public static void writeFile(String aFilePath, List<String> aList) throws IOException
    {

        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new OutputStreamWriter
                    (new FileOutputStream(aFilePath), AppConstants.CHARACTER_CODE));

            for (int i = 0; i < aList.size(); i++) {
                bw.write(aList.get(i));
                if (aList.size() - 1 != i) {
                    bw.newLine(); // 最終行は改行しない
                }
            }
        } catch (IOException e) {
            throw new IOException();
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
    }

    /**
     * <p>文字コードを判定します。</p>
     *
     * @param byte[] src チェックする文字列
     * @return
     */
    public static boolean isUTF8(byte[] src)
    {
        try {
            byte[] tmp = new String(src, AppConstants.CHARACTER_CODE).getBytes(AppConstants.CHARACTER_CODE);
            return Arrays.equals(tmp, src);
        }
        catch(UnsupportedEncodingException e) {
            return false;
        }
    }
}