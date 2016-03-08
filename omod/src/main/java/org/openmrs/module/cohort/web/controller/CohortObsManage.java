/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.cohort.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortEncounter;
import org.openmrs.module.cohort.CohortObs;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * The main controller.
 */
@Controller
public class CohortObsManage {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/cohort/cohortobs.form", method = RequestMethod.GET)
	public void manage(HttpSession httpSession,HttpServletRequest request, ModelMap model, @RequestParam(required = false, value = "obsId")Integer id,  @ModelAttribute("cohortobs")CohortObs cobs)
	{
		CohortEncounter enc=new CohortEncounter();
		CohortService service = Context.getService(CohortService.class);
		    if("search".equals(request.getParameter("search")))
		    {
		    	 List<CohortObs> list1=service.findCohortObs(id);
		    	 model.addAttribute("CohortList",list1); 
		    }
	}
}
