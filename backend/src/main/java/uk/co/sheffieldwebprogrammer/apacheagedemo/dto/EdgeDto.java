package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class EdgeDto {

    private Long nodeId1;
    private Long nodeId2;
    private String relation;

    public EdgeDto() {
    }

    public EdgeDto(Long nodeId1, Long nodeId2, String relation) {
        this.nodeId1 = nodeId1;
        this.nodeId2 = nodeId2;
        this.relation = relation;
    }

    public Long getNodeId1() {
        return nodeId1;
    }

    public Long getNodeId2() {
        return nodeId2;
    }

    public String getRelation() {
        return relation;
    }

    public void setNodeId1(Long nodeId1) {
        this.nodeId1 = nodeId1;
    }

    public void setNodeId2(Long nodeId2) {
        this.nodeId2 = nodeId2;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

}
