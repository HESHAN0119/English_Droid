package com.exhera.englishdroid.FindPP;

public class PP {

    String present,past,pastP;

    public PP() {
    }

    public PP(String present, String past, String pastP) {
        this.present = present;
        this.past = past;
        this.pastP = pastP;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getPast() {
        return past;
    }

    public void setPast(String past) {
        this.past = past;
    }

    public String getPastP() {
        return pastP;
    }

    public void setPastP(String pastP) {
        this.pastP = pastP;
    }
}
