package org.openmrs.module.cohort.web.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortVisit;
import org.openmrs.module.cohort.api.CohortService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class CohortDashboardController {
	
	protected final Log log = LogFactory.getLog(getClass());
	private SessionStatus status;
	
	@RequestMapping("/module/cohort/dashboard")
	public void manage(@RequestParam("cid")Integer id, ModelMap map)
	{
		CohortService c=Context.getService(CohortService.class);
		List<CohortM> cohort1=c.findCohort(id);
		List<CohortVisit> cohort2=c.findCohortVisit();
		map.addAttribute("CohortL",cohort1);
		map.addAttribute("cohort",cohort1.get(0));
		map.addAttribute("CohortList",cohort2);
	}
}
