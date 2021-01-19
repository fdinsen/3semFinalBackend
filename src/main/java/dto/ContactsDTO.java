/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gamma
 */
public class ContactsDTO {
    List<ContactDTO> all;

    public ContactsDTO() {
        all = new ArrayList(); 
    }

    public ContactsDTO(List<ContactDTO> all) {
        this.all = all;
    }

    public List<ContactDTO> getAll() {
        return all;
    }

    public void setAll(List<ContactDTO> all) {
        this.all = all;
    }
    
    public void addContact(ContactDTO dto) {
        all.add(dto);
    }
}
