package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016-07-19 0019.
 */
public final class UserWithdrawalsEntity {
    private String ctime;
    private String verify_status;
    private int amount;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(String verify_status) {
        this.verify_status = verify_status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
