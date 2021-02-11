package com.dao;

import com.entity.Person;

import java.util.List;

public class PersonDao {

    private List<Person> personList;

    public PersonDao(List<Person> personList) {
        this.personList = personList;
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public List<Person> getAll() {
        return personList;
    }

    public boolean removePerson(Person person) {
       return personList.remove(person);
    }

    public boolean save(Person person) {
        Person storedPerson = getById(person.getId());
        if (null != storedPerson) {
            personList.remove(person);
        }
        return personList.add(person);
    }

    public Person getById(int id) {
      return  personList.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
}
