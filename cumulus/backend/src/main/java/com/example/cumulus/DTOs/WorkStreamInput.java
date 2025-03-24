package com.example.cumulus.DTOs;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "workstreamInputs")
public class WorkStreamInput {
    @Id
    private String ID;
    private String workstreamID;
    private String workStreamType;
    private String createBy;
    private int isWhole;
    private String reason;
    private int accessAmount;
    private int sensitivityLevel;
    private int isDirect;
    private int isLocal;
    private String importantAspect;
    private int isMultiPlatform;
    private String payPlan;
    private String priority;
    private int isConstant;
    private int isAutomated;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWorkstreamID() {
        return workstreamID;
    }

    public void setWorkstreamID(String workstreamID) {
        this.workstreamID = workstreamID;
    }

    public String getWorkStreamType() {
        return workStreamType;
    }

    public void setWorkStreamType(String workStreamType) {
        this.workStreamType = workStreamType;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String workStreamCreatedBy) {
        this.createBy = workStreamCreatedBy;
    }

    public int getIsWhole() {
        return isWhole;
    }

    public void setIsWhole(int isWhole) {
        this.isWhole = isWhole;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAccessAmount() {
        return accessAmount;
    }

    public void setAccessAmount(int accessAmount) {
        this.accessAmount = accessAmount;
    }

    public int getSensitivityLevel() {
        return sensitivityLevel;
    }

    public void setSensitivityLevel(int sensitivityLevel) {
        this.sensitivityLevel = sensitivityLevel;
    }

    public int getIsDirect() {
        return isDirect;
    }

    public void setIsDirect(int isDirect) {
        this.isDirect = isDirect;
    }

    public int getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(int isLocal) {
        this.isLocal = isLocal;
    }

    public String getImportantAspect() {
        return importantAspect;
    }

    public void setImportantAspect(String importantAspect) {
        this.importantAspect = importantAspect;
    }

    public int getIsMultiPlatform() {
        return isMultiPlatform;
    }

    public void setIsMultiPlatform(int isMultiPlatform) {
        this.isMultiPlatform = isMultiPlatform;
    }

    public String getPayPlan() {
        return payPlan;
    }

    public void setPayPlan(String payPlan) {
        this.payPlan = payPlan;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getIsConstant() {
        return isConstant;
    }

    public void setIsConstant(int isConstant) {
        this.isConstant = isConstant;
    }

    public int getIsAutomated() {
        return isAutomated;
    }

    public void setIsAutomated(int isAutomated) {
        this.isAutomated = isAutomated;
    }
    @Override
    public String toString() {
        return "WorkStreamInput{" +
                "ID='" + ID + '\'' +
                ", workstreamID='" + workstreamID + '\'' +
                ", workStreamType='" + workStreamType + '\'' +
                ", createBy='" + createBy + '\'' +
                ", isWhole=" + isWhole +
                ", reason='" + reason + '\'' +
                ", accessAmount=" + accessAmount +
                ", sensitivityLevel=" + sensitivityLevel +
                ", isDirect=" + isDirect +
                ", isLocal=" + isLocal +
                ", importantAspect='" + importantAspect + '\'' +
                ", isMultiPlatform=" + isMultiPlatform +
                ", payPlan='" + payPlan + '\'' +
                ", priority='" + priority + '\'' +
                ", isConstant=" + isConstant +
                ", isAutomated=" + isAutomated +
                '}';
    }
}
