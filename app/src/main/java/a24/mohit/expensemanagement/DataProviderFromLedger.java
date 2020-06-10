package a24.mohit.expensemanagement;

public class DataProviderFromLedger {
    public DataProviderFromLedger(String fromledger, String balfrom) {
        this.setFromledger(fromledger);
        this.setBalfrom(balfrom);
    }

    String fromledger;
    String balfrom;

    public String getFromledger() {
        return fromledger;
    }

    public void setFromledger(String fromledger) {
        this.fromledger = fromledger;
    }

    public String getBalfrom() {
        return balfrom;
    }

    public void setBalfrom(String balfrom) {
        this.balfrom = balfrom;
    }


}
