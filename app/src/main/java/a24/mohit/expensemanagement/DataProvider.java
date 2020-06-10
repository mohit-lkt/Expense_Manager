package a24.mohit.expensemanagement;

/**
 * Created by mohit on 11/27/2017.
 */

public class DataProvider {
    DataProvider( String mainexpense, String subexpense, String limit){

       this.setMainexpense(mainexpense);
       this.setSubexpense(subexpense);
       this.setLimit(limit);


    }

    String mainexpense;
    String subexpense;

    public String getMainexpense() {
        return mainexpense;
    }

    public void setMainexpense(String mainexpense) {
        this.mainexpense = mainexpense;
    }

    public String getSubexpense() {
        return subexpense;
    }

    public void setSubexpense(String subexpense) {
        this.subexpense = subexpense;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    String limit;


}
