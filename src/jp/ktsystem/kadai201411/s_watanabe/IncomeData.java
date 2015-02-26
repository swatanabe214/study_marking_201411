package jp.ktsystem.kadai201411.s_watanabe;

/**
 * <p>入金データのデータクラスです。</p>
 *
 * @author s_watanabe
 *
 */
public class IncomeData {

    // 受注ID
    private String orderID;
    // 入金日時
    private String dateAndTime;

    public IncomeData(String orderID, String dateAndTime) {
        this.orderID = orderID;
        this.dateAndTime = dateAndTime;
    }

    public IncomeData() {
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getOrderID() {
        return this.orderID;
    }

    public String getDateAndTime() {
        return this.dateAndTime;
    }
}