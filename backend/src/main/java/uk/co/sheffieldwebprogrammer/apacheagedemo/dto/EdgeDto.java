package uk.co.sheffieldwebprogrammer.apacheagedemo.dto;

public class EdgeDto {

    private Long nodeId1;
    private Long nodeId2;
    private String relationshipType;

    public Long getNodeId1() {
        return nodeId1;
    }

    public Long getNodeId2() {
        return nodeId2;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setNodeId1(Long nodeId1) {
        this.nodeId1 = nodeId1;
    }

    public void setNodeId2(Long nodeId2) {
        this.nodeId2 = nodeId2;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

}
