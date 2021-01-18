/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Opportunity;
import java.util.Date;

/**
 *
 * @author gamma
 */
public class OpportunityDTO {
    private int id;
    private String name;
    private int amount;
    private Date closeDate;
    private String opportunityStatus;

    public OpportunityDTO(Opportunity o) {
        id = o.getId();
        name = o.getName();
        amount = o.getAmount();
        closeDate = o.getCloseDate();
        opportunityStatus = o.getOpportunityStatus().getName();
    }

    public OpportunityDTO() {
    }

    public OpportunityDTO(String name, int amount, Date closeDate, String os) {
        this.name = name;
        this.amount = amount;
        this.closeDate = closeDate;
        this.opportunityStatus = os;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getOpportunityStatus() {
        return opportunityStatus;
    }

    public void setOpportunityStatus(String opportunityStatus) {
        this.opportunityStatus = opportunityStatus;
    }
    
    
    
    
}
