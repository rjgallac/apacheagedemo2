package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class EdgeGraphDto {
    private Long id;
    private String source;
    private String target;
    private String relation;

    public EdgeGraphDto() {
    }

    public EdgeGraphDto(Long id, String source, String target, String relation) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.relation = relation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
}
