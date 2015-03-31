package jp.ktsystem.kadai201411.s_watanabe;

/**
 * <p>受注情報ファイルのデータクラスです。</p>
 *
 * @author s_watanabe
 *
 */
public class OrderData {

    // 受注ID
    private String orderID;
    // 顧客名
    private String name;
    // 製品名
    private String productName;
    // 数量
    private int quantity;
    // 納期
    private String deliveryDate;

    public OrderData(String orderID, String name, String productName,
            int quantity, String deliveryDate) {

        this.orderID = orderID;
        this.name = name;
        this.productName = productName;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
    }

    public OrderData() {
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderID() {
        return this.orderID;
    }

    public String getName() {
        return this.name;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getDeliveryDate() {
        return this.deliveryDate;
    }
}
