package com.sbigeneral.LoginCapatchePoc.Service;

import java.util.List;

import com.sbigeneral.LoginCapatchePoc.model.RenewalMisReport;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsModel;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsReport;

public interface RenewalMisReportService {

	
	public List<RenewalMisReport> fetchRenewalMisReport(RenewalRmsModel renewalRmsModel);
}
