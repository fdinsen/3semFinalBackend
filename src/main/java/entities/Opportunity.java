/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dto.OpportunityDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author gamma
 */
@Entity
@Table(name = "opportunity")
@NamedQuery(name = "Opportunity.deleteAllRows", query = "DELETE from Opportunity")
public class Opportunity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "o_id")
    private int id;

    @Column(name = "o_name")
    private String name;

    private int amount;

    @Temporal(TemporalType.DATE)
    private Date closeDate;

    @JoinColumn(name = "os_id", referencedColumnName = "os_id")
    @ManyToOne (optional = false, cascade = CascadeType.PERSIST)
    private OpportunityStatus opportunityStatus;
    
    
    @JoinColumn(name = "c_id", referencedColumnName = "c_id")
    @ManyToOne (optional = false, cascade = CascadeType.PERSIST)
    private Contact contact;

    public Opportunity() {
    }

    public Opportunity(String name, int amount, Date closeDate) {
        this.name = name;
        this.amount = amount;
        this.closeDate = closeDate;
    }
    
    public Opportunity(OpportunityDTO dto) {
        name = dto.getName();
        amount = dto.getAmount();
        closeDate = dto.getCloseDate();
        opportunityStatus = new OpportunityStatus(dto.getOpportunityStatus());
    }
    
    @PreRemove
    public void removeContact() {
        contact.removeOpportunity(this);
        contact = null;
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

    public OpportunityStatus getOpportunityStatus() {
        return opportunityStatus;
    }

    public void setOpportunityStatus(OpportunityStatus os) {
        this.opportunityStatus = os;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact c) {
        this.contact = c;
        if(c != null && !c.getOpportunityList().contains(this)) {
            c.addOpportunity(this);
        }
    }
    
    

}
