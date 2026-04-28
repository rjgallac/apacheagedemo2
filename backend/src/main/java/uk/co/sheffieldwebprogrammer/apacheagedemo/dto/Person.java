package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class Person {
    private Long id;
    private String name;

    public Person() {
    }

    public Person(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
