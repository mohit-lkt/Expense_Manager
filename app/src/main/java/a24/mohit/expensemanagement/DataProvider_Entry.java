package a24.mohit.expensemanagement;

/**
 * Created by mohit on 11/27/2017.
 */

public class DataProvider_Entry {

    public DataProvider_Entry(String receiptNo, String date_entry, String fromHead, String mainExpense, String subExpense, String amount, String remark) {
        this.receiptNo = receiptNo;
        this.date_entry = date_entry;
        this.fromHead = fromHead;
        this.mainExpense = mainExpense;
        this.subExpense = subExpense;
        this.amount = amount;
        this.remark = remark;
    }
    String receiptNo;
    String date_entry;
    String fromHead;
    String mainExpense;
    String subExpense;
    String amount;
    String remark;

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDate_entry() {
        return date_entry;
    }

    public void setDate_entry(String date_entry) {
        this.date_entry = date_entry;
    }

    public String getFromHead() {
        return fromHead;
    }

    public void setFromHead(String fromHead) {
        this.fromHead = fromHead;
    }

    public String getMainExpense() {
        return mainExpense;
    }

    public void setMainExpense(String mainExpense) {
        this.mainExpense = mainExpense;
    }

    public String getSubExpense() {
        return subExpense;
    }

    public void setSubExpense(String subExpense) {
        this.subExpense = subExpense;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }





}
