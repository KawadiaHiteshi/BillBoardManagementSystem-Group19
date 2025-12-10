/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

/**
 *
 * @author prekshapraveen
 */
public class FieldEngineerAssignmentRequest extends WorkRequest {

    private int engineerId;
    private String taskDescription;
    private PowerIssueRequest powerRequest;

    public int getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(int engineerId) {
        this.engineerId = engineerId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public PowerIssueRequest getPowerRequest() {
        return powerRequest;
    }

    public void setPowerRequest(PowerIssueRequest powerRequest) {
        this.powerRequest = powerRequest;
    }

    private int powerRequestId;

    public int getPowerRequestId() {
        return powerRequestId;
    }

    public void setPowerRequestId(int powerRequestId) {
        this.powerRequestId = powerRequestId;
    }
}
