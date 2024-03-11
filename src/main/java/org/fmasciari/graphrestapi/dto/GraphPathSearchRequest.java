package org.fmasciari.graphrestapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class GraphPathSearchRequest {

    @NotEmpty
    private String srcNode;

    @NotBlank
    private String dstNode;

    public String getDstNode() {
        return dstNode;
    }

    public void setDstNode(String dstNode) {
        this.dstNode = dstNode;
    }

    public String getSrcNode() {
        return srcNode;
    }

    public void setSrcNode(String srcNode) {
        this.srcNode = srcNode;
    }
}
