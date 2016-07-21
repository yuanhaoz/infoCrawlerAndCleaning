package json;

import java.util.List;

public class NodeTree {

	private int totalbranchlevel;
    private int totalbranchnum;
    private int branchnum;
    private int term_id;
    private String name;
    private List<Sortedbranchlist> nodeBranchSums;
    private int facet_id;
    private List<NodeBranch> nodeBranch;
    private int totalleafnum;
    
	public int getTotalbranchlevel() {
		return totalbranchlevel;
	}
	public void setTotalbranchlevel(int totalbranchlevel) {
		this.totalbranchlevel = totalbranchlevel;
	}
	public int getTotalbranchnum() {
		return totalbranchnum;
	}
	public void setTotalbranchnum(int totalbranchnum) {
		this.totalbranchnum = totalbranchnum;
	}
	public int getBranchnum() {
		return branchnum;
	}
	public void setBranchnum(int branchnum) {
		this.branchnum = branchnum;
	}
	public int getTerm_id() {
		return term_id;
	}
	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Sortedbranchlist> getNodeBranchSums() {
		return nodeBranchSums;
	}
	public void setNodeBranchSums(List<Sortedbranchlist> nodeBranchSums) {
		this.nodeBranchSums = nodeBranchSums;
	}
	public int getFacet_id() {
		return facet_id;
	}
	public void setFacet_id(int facet_id) {
		this.facet_id = facet_id;
	}
	public List<NodeBranch> getNodeBranch() {
		return nodeBranch;
	}
	public void setNodeBranch(List<NodeBranch> nodeBranch) {
		this.nodeBranch = nodeBranch;
	}
	public int getTotalleafnum() {
		return totalleafnum;
	}
	public void setTotalleafnum(int totalleafnum) {
		this.totalleafnum = totalleafnum;
	}
    
}
