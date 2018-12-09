package parentapp.ippi.ippiparent.model;

public class ReceiptModel {
    String SitterName, startTime, endTime, reqTime, newEnd, addCharge, totalCharge;


    public ReceiptModel(String SitterName, String reqTime, String newEnd, String addCharge, String totalCharge) {

        this.SitterName = SitterName;
        this.reqTime = reqTime;
        this.newEnd = newEnd;
        this.addCharge = addCharge;
        this.totalCharge = totalCharge;

    }

    public ReceiptModel() {
    }


    public String getSitterName() {
        return SitterName;
    }

    public void setSitterName(String sitterName) {
        SitterName = sitterName;
    }


    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getNewEnd() {
        return newEnd;
    }

    public void setNewEnd(String newEnd) {
        this.newEnd = newEnd;
    }

    public String getAddCharge() {
        return addCharge;
    }

    public void setAddCharge(String addCharge) {
        this.addCharge = addCharge;
    }

    public String getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(String totalCharge) {
        this.totalCharge = totalCharge;
    }


}
