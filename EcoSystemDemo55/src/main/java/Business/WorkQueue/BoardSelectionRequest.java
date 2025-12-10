/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author prekshapraveen
 */
public class BoardSelectionRequest extends WorkRequest {
    
    private List<Integer> selectedBoardIds = new ArrayList<>();
    private String campaignName;

    public List<Integer> getSelectedBoardIds() {
        return selectedBoardIds;
    }

    public void setSelectedBoardIds(List<Integer> selectedBoardIds) {
        this.selectedBoardIds = selectedBoardIds;
    }

    public void addBoardId(int boardId) {
        this.selectedBoardIds.add(boardId);
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }
}
