package com.controller;

import com.dao.PersonDao;
import com.entity.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zul.*;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class PersonController extends Window {
    private Listbox personListBox;
    private Toolbarbutton tbbDelete;
    private List<Person> personList;
    private PersonDao dao;
    private Textbox tbPersonName;
    private Toolbarbutton tbbSave;


    //first load page func
    public void onCreate() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext((ServletContext) getDesktop().getWebApp().getNativeContext());

        dao = (PersonDao) ctx.getBean("personDao");
        personList = dao.getAll();
        tbPersonName = (Textbox) this.getFellow("tbPersonName");
        tbbSave = (Toolbarbutton) this.getFellow("tbbSave");
        if (personList.size() > 0) {
            personListBox = (Listbox) this.getFellow("lbPerson");
            tbbDelete = (Toolbarbutton) this.getFellow("tbbDelete");
            populateListBox();
        }
    }

    private void populateListBox() {
        for (Person person : personList) {
            personListBox.appendChild(new Listitem(person.getName(), person));
        }
    }

    public void onDeletePerson() {
        List<Listitem> selectedPersons = getSelectedPersons();

        if (!CollectionUtils.isEmpty(selectedPersons)) {
            for (Listitem item : selectedPersons) {
                if (dao.removePerson(item.getValue())) {
                    personList.remove((Person) item.getValue());
                    personListBox.removeChild(item);
                }
            }
        }
    }

    private List<Listitem> getSelectedPersons() {
        List<Listitem> listItems = new ArrayList<>();
        for (Listitem li : personListBox.getItems()) {
            if (li.isSelected()) {
                listItems.add(li);
            }
        }
        return listItems;
    }

    public void onSelectLB() {
        if (personListBox.getSelectedCount() > 0) {
            tbbDelete.setDisabled(false);
            tbbSave.setDisabled(true);
            tbPersonName.setValue(((Person) personListBox.getSelectedItem().getValue()).getName());
        } else {
            tbbDelete.setDisabled(true);
        }
    }

    public void onSave() {
        if (tbPersonName.getValue() != null && tbPersonName.getValue().trim().length() > 0) {
            Listitem selectedItem = personListBox.getSelectedItem();

            if (null == selectedItem) {
                Messagebox.show("Select person is required");
                return;
            }

            Person person = selectedItem.getValue();
            person.setName(tbPersonName.getValue().trim());

            if (!dao.save(person)) {
                Messagebox.show("Failed to save person");
            } else {
                personListBox.getSelectedItem().setLabel(person.getName());
                personListBox.getSelectedItem().setValue(person);
            }

        } else {
            Messagebox.show("Field \"Name\" is required");
        }
    }
}
