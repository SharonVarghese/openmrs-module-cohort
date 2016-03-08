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
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class  CohortAttributesDeleteController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/cohort/deletecohortattributes", method = RequestMethod.GET)
	public void manage(HttpSession httpSession,HttpServletRequest request, ModelMap model, @RequestParam(required = false, value = "value") String attribute_type,  @ModelAttribute("cohortatt") CohortAttribute attributes) {
		 CohortService departmentService = Context.getService(CohortService.class);
		    List<CohortAttribute> list1=departmentService.findCohortAtt(attribute_type);
		    for (int i = 0; i < list1.size(); i++) {
			    CohortAttribute c = list1.get(i);
		 if ("delete".equalsIgnoreCase(request.getParameter("delete"))&& c.getValue().equalsIgnoreCase(attribute_type)) {
            try {
                departmentService.purgeCohortAtt(c);
                httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "delete success");
                }
            catch (Exception ex) {
                httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "delete failure");
                log.error("Failed to delete cohort", ex);
            }
		 }
	}
	}
}
