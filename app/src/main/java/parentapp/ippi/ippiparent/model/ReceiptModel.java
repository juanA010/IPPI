package parentapp.ippi.ippiparent.model;

public class ReceiptModel {
    String ParentName, SitterName, startTime, endTime, reqTime, newEnd, addCharge, totalCharge;


    public ReceiptModel(String ParentName, String SitterName, String startTime, String endTime, String reqTime, String newEnd, String addCharge, String totalCharge) {

        this.ParentName = ParentName;
        this.SitterName = SitterName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reqTime = reqTime;
        this.newEnd = newEnd;
        this.addCharge = addCharge;
        this.totalCharge = totalCharge;

    }

    public ReceiptModel() {
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public String getSitterName() {
        return SitterName;
    }

    public void setSitterName(String sitterName) {
        SitterName = sitterName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
