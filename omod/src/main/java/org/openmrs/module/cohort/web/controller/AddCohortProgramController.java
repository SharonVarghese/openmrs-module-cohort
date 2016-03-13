/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.cohort.web.controller;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.cohort.CohortProgram;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * The main controller.
 */
@Controller
public class AddCohortProgramController {
	
	@Autowired(required = true)
	@Qualifier("addCohortProgramValidator")
	private Validator validator;
	
	@RequestMapping(value = "/module/cohort/addcohortprogram", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("cohortprogram", new CohortProgram());
	}
	
	@RequestMapping(value = "/module/cohort/addcohortprogram.form", method = RequestMethod.POST)
	public String onSearch(WebRequest request, HttpSession httpSession, ModelMap model,
	                       @RequestParam(required = false, value = "name") String cohort_name,
	                       @RequestParam(required = false, value = "description") String description,
	                       @ModelAttribute("cohortprogram") CohortProgram cp, BindingResult errors) {
		CohortService departmentService = Context.getService(CohortService.class);
		if (!Context.isAuthenticated()) {
			errors.reject("Required");
		}
		this.validator.validate(cp, errors);
		System.out.println("Before BR");
		if (errors.hasErrors()) {
			System.out.println("BR has errors: " + errors.getErrorCount());
			System.out.println(errors.getAllErrors());
			return "/module/cohort/addcohortprogram";
		}
		if (cohort_name.length() > 20) {
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "name cannot be greater than 20");
		} else {
			departmentService.saveCohortProgram(cp);
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "insertion success");
		}
		return null;
	}
}