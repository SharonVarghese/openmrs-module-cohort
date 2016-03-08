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

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Privilege;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.web.WebConstants;
import org.openmrs.web.taglib.fieldgen.FieldGenHandlerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
public class  AddCohortMemberAttributeTypeController {
	
	protected final Log log = LogFactory.getLog(getClass());
	 private SessionStatus status;
	
	@RequestMapping(value = "/module/cohort/addcohortmemberattributetype", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("cohortattributes",new CohortMemberAttributeType());
		 List<String> formats = new ArrayList<String>(FieldGenHandlerFactory.getSingletonInstance().getHandlers().keySet());
			formats.add("java.lang.Character");
			formats.add("java.lang.Integer");
			formats.add("java.lang.Float");
			formats.add("java.lang.Boolean");
			model.addAttribute("formats",formats); 
	}
	 @RequestMapping(value = "/module/cohort/addcohortmemberattributetype.form", method = RequestMethod.POST)
	    public ModelAndView onSubmit(WebRequest request, HttpSession httpSession, ModelMap model,
	    		@RequestParam(required = false, value = "name") String attribute_type,
                @RequestParam(required = false, value = "description") String description,
	                                   @ModelAttribute("cohortattributes") CohortMemberAttributeType cohortattributes, BindingResult errors) {
	        CohortService departmentService = Context.getService(CohortService.class);
	        String voided = request.getParameter("voided");
	        String format=request.getParameter("format");
	        if(attribute_type=="" && description=="")
	        {
	        	httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Values cannot be null");
	            model.addAttribute("cohortattributes",new CohortMemberAttributeType());
			    List<String> formats = new ArrayList<String>(FieldGenHandlerFactory.getSingletonInstance().getHandlers().keySet());
				formats.add("java.lang.Character");
				formats.add("java.lang.Integer");
				formats.add("java.lang.Float");
				formats.add("java.lang.Boolean");
				model.addAttribute("formats",formats); 
	        }
	        else {
	        	cohortattributes.setFormat(format);
	        	departmentService.saveCohortMemberAttributeType(cohortattributes); 
	        	httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "insertion success");
	        }
	        return null;
	     }
}