package json;

import java.util.List;

public class NodeBranch {

	private int totalbranchlevel;
    private int branchnum;
    private String facet_name;
    private int totalbranchnum;
    private int facet_id;
    private String type;
    private List<NodeLeaf> nodeLeaf;
    private int totalleafnum;
    
	public int getTotalbranchlevel() {
		return totalbranchlevel;
	}
	public void setTotalbranchlevel(int totalbranchlevel) {
		this.totalbranchlevel = totalbranchlevel;
	}
	public int getBranchnum() {
		return branchnum;
	}
	public void setBranchnum(int branchnum) {
		this.branchnum = branchnum;
	}
	public String getFacet_name() {
		return facet_name;
	}
	public void setFacet_name(String facet_name) {
		this.facet_name = facet_name;
	}
	public int getTotalbranchnum() {
		return totalbranchnum;
	}
	public void setTotalbranchnum(int totalbranchnum) {
		this.totalbranchnum = totalbranchnum;
	}
	public int getFacet_id() {
		return facet_id;
	}
	public void setFacet_id(int facet_id) {
		this.facet_id = facet_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<NodeLeaf> getNodeLeaf() {
		return nodeLeaf;
	}
	public void setNodeLeaf(List<NodeLeaf> nodeLeaf) {
		this.nodeLeaf = nodeLeaf;
	}
	public int getTotalleafnum() {
		return totalleafnum;
	}
	public void setTotalleafnum(int totalleafnum) {
		this.totalleafnum = totalleafnum;
	}
    
}
