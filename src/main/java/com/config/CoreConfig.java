package com.config;

import com.dao.PersonDao;
import com.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class CoreConfig {

    @Bean (name = "personDao")
    public PersonDao getPersonDao() {
        System.out.println("Creating person dao");
        PersonDao personDao = new PersonDao(new ArrayList<>());
        for (int i = 0; i < 5; i++) {
            Person person = new Person(i, "PersonName_" + i);
            System.out.println("Add person: " + person);
            personDao.addPerson(person);
        }

        System.out.println("Person dao was created");
        return personDao;
    }
}
