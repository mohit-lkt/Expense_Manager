package a24.mohit.expensemanagement;

public class DataProviderToLedger {
    String toledger;
    String balto;

    public DataProviderToLedger(String toledger, String balto) {
        this.setToledger(toledger);
        this.setBalto(balto);
    }

    public String getToledger() {
        return toledger;
    }

    public void setToledger(String toledger) {
        this.toledger = toledger;
    }

    public String getBalto() {
        return balto;
    }

    public void setBalto(String balto) {
        this.balto = balto;
    }
}
