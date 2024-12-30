package model;

import java.io.Serializable;

public class RequestContext implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int employeId;

    public int getEmployeId() {
        return employeId;
    }

    public void setEmployeId(int employeId) {
        this.employeId = employeId;
    }
}
