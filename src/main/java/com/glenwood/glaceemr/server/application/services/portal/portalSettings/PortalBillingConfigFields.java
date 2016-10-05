package com.glenwood.glaceemr.server.application.services.portal.portalSettings;

import java.util.List;

import com.glenwood.glaceemr.server.application.models.BillingConfigTable;

public class PortalBillingConfigFields {

	List<BillingConfigTable> statesList;

	public List<BillingConfigTable> getStatesList() {
		return statesList;
	}

	public void setStatesList(List<BillingConfigTable> statesList) {
		this.statesList = statesList;
	}
	
}
