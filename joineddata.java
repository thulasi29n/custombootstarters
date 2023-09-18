public class JoinedData {
    private String txn_ref;       // Common key from both classes
    private String lei;
    private String tr_date;
    private String otcflag;
    private String reg_name;
    private String isin;
    private String currency_code; // From the new data class

    // Default constructor
    public JoinedData() {}

    // Constructor with all fields for easy instantiation
    public JoinedData(String txn_ref, String lei, String tr_date, 
                      String otcflag, String reg_name, String isin, 
                      String currency_code) {
        this.txn_ref = txn_ref;
        this.lei = lei;
        this.tr_date = tr_date;
        this.otcflag = otcflag;
        this.reg_name = reg_name;
        this.isin = isin;
        this.currency_code = currency_code;
    }

    // Getters and setters for each field

    public String getTxn_ref() {
        return txn_ref;
    }

    public void setTxn_ref(String txn_ref) {
        this.txn_ref = txn_ref;
    }

    public String getLei() {
        return lei;
    }

    public void setLei(String lei) {
        this.lei = lei;
    }

    public String getTr_date() {
        return tr_date;
    }

    public void setTr_date(String tr_date) {
        this.tr_date = tr_date;
    }

    public String getOtcflag() {
        return otcflag;
    }

    public void setOtcflag(String otcflag) {
        this.otcflag = otcflag;
    }

    public String getReg_name() {
        return reg_name;
    }

    public void setReg_name(String reg_name) {
        this.reg_name = reg_name;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    // Optionally, you can also override toString(), equals(), and hashCode() methods
}
