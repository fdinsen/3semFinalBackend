/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Contact;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gamma
 */
public class ContactDTO {

    private int id;

    private String name;
    private String email;
    private String company;
    private String jobtitle;
    private String phone;
    private List<OpportunityDTO> opportunities;

    public ContactDTO() {
        opportunities = new ArrayList();
    }

    public ContactDTO(Contact contact) {
        id = contact.getId();
        name = contact.getName();
        email = contact.getEmail();
        company = contact.getCompany();
        jobtitle = contact.getJobtitle();
        phone = contact.getPhone();

        opportunities = new ArrayList();
        if (contact.getOpportunityList() != null) {
            contact.getOpportunityList().forEach((o) -> {
                opportunities.add(new OpportunityDTO(o));
            });
        }
    }

    public ContactDTO(String name, String email, String company, String jobtitle, String phone) {
        this.name = name;
        this.email = email;
        this.company = company;
        this.jobtitle = jobtitle;
        this.phone = phone;
        opportunities = new ArrayList();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OpportunityDTO> getOpportunities() {
        return opportunities;
    }

    public void addOpportunity(OpportunityDTO o) {
        opportunities.add(o);
    }

    
}
