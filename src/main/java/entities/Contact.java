package entities;

import dto.ContactDTO;
import errorhandling.InvalidInput;
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
    private int id;

    private String name;
    private String email;
    private String company;
    private String jobtitle;
    private String phone;

    public Contact() {
    }

    public Contact(ContactDTO dto) throws InvalidInput {
        if (allFieldsAreSet(dto)) {
            name = dto.getName();
            email = dto.getEmail();
            company = dto.getCompany();
            jobtitle = dto.getJobtitle();
            phone = dto.getPhone();
        } else {
            throw new InvalidInput("All fields must be set");
        }
    }

    public Contact(String name, String email, String company, String jobtitle, String phone) {
        this.name = name;
        this.email = email;
        this.company = company;
        this.jobtitle = jobtitle;
        this.phone = phone;
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

    private boolean allFieldsAreSet(ContactDTO dto) {
        return !(dto.getName() == null || dto.getName().isEmpty()
                || dto.getEmail() == null || dto.getEmail().isEmpty()
                || dto.getCompany() == null || dto.getCompany().isEmpty()
                || dto.getJobtitle() == null || dto.getJobtitle().isEmpty()
                || dto.getPhone() == null || dto.getPhone().isEmpty());
    }
}
