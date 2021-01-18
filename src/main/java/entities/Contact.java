package entities;

import dto.ContactDTO;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Contact.deleteAllRows", query = "DELETE from Contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String company;
    private String jobtitle;
    private String phone;

    public Contact() {
    }

    public Contact(ContactDTO dto) {
        if (allFieldsAreSet(dto)) {
            name = dto.getName();
            email = dto.getEmail();
            company = dto.getCompany();
            jobtitle = dto.getJobtitle();
            phone = dto.getPhone();
        } else {
            //TODO throw error
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    private boolean allFieldsAreSet(ContactDTO dto) {
        return !(dto.getName() == null
                || dto.getEmail() == null
                || dto.getCompany() == null
                || dto.getJobtitle() == null
                || dto.getPhone() == null);
    }
}
