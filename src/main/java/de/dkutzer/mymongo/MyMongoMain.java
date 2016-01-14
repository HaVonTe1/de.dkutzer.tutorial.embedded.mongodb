package de.dkutzer.mymongo;

import java.util.List;

public class MyMongoMain {

    public static void main(String[] args) {

        MyMongoDBPersonController.init();
        MyMongoDBPersonController.insertPerson(new PersonDTO("John", "Lennon", 40));
        MyMongoDBPersonController.insertPerson(new PersonDTO("Paul", "McCartney", 73));
        MyMongoDBPersonController.insertPerson(new PersonDTO("Geroge", "Harrison", 58));
        MyMongoDBPersonController.insertPerson(new PersonDTO("Ringo", "Starr", 75));

        final List<PersonDTO> allPersons = MyMongoDBPersonController.findAllPersons();
        allPersons.forEach(p -> {
            System.out.println(p);
        });

    }

}
