package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class NodeDto {
    private Long id;
    private String type;
    private String name;

    public NodeDto(Long id2, String name2, String nodeType) {
        this.id = id2;
        this.name = name2;
        this.type = nodeType;
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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
