package entities;

import dto.ContactDTO;
import dto.OpportunityDTO;
import errorhandling.InvalidInput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contact")
@NamedQuery(name = "Contact.deleteAllRows", query = "DELETE from Contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "c_id")
    private int id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "c_name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Column(name = "jobtitle")
    private String jobtitle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opportunity> opportunityList;

    public Contact() {
        opportunityList = new ArrayList();
    }

    public Contact(ContactDTO dto) throws InvalidInput {
        opportunityList = new ArrayList();
        if (allFieldsAreSet(dto)) {
            name = dto.getName();
            email = dto.getEmail();
            company = dto.getCompany();
            jobtitle = dto.getJobtitle();
            phone = dto.getPhone();

            if (dto.getOpportunities() != null) {
                dto.getOpportunities().forEach((o) -> {
                    opportunityList.add(new Opportunity(o));
                });
            }
        } else {
            throw new InvalidInput("All fields must be set");
        }
    }

    public Contact(String name, String email, String company, String jobtitle, String phone) {
        opportunityList = new ArrayList();
        this.name = name;
        this.email = email;
        this.company = company;
        this.jobtitle = jobtitle;
        this.phone = phone;
    }
    
    @PreRemove
    public void removeContact() {
        for(int i = 0; i < opportunityList.size() ; i++) {
            opportunityList.get(i).setContact(null);
        }
        opportunityList = new ArrayList();
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

    public List<Opportunity> getOpportunityList() {
        return opportunityList;
    }

    public void addOpportunity(Opportunity o) {
        opportunityList.add(o);
        if (o != null) {
            o.setContact(this);
        }
    }
    
    public void removeOpportunity(Opportunity o) {
        opportunityList.remove(o);
        if(o.getContact() == this) {
            o.setContact(null);
        }
    }

    private boolean allFieldsAreSet(ContactDTO dto) {
        return !(dto.getName() == null || dto.getName().isEmpty()
                || dto.getEmail() == null || dto.getEmail().isEmpty()
                || dto.getCompany() == null || dto.getCompany().isEmpty()
                || dto.getJobtitle() == null || dto.getJobtitle().isEmpty()
                || dto.getPhone() == null || dto.getPhone().isEmpty());
    }
}
