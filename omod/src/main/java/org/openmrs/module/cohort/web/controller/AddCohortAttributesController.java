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
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.web.WebConstants;
import org.openmrs.web.taglib.fieldgen.FieldGenHandlerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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
import org.springframework.web.servlet.view.RedirectView;

/**
 * The main controller.
 */
@Controller
public class  AddCohortAttributesController {
	
	protected final Log log = LogFactory.getLog(getClass());
	 private SessionStatus status;
	// CohortAttributeType a=new CohortAttributeType();
    // CohortM m=new CohortM();
	
	@RequestMapping(value = "/module/cohort/addcohortattributes", method = RequestMethod.GET)
	public void manage(@RequestParam(required = false, value="ca") Integer id,WebRequest request,ModelMap model,@ModelAttribute("cohortatt") CohortAttribute cohortattribute) {
		int a=0;
		CohortM cohort=null;
		CohortAttributeType coat=null;
		CohortService s=Context.getService(CohortService.class);
		List<CohortAttributeType> att=s.findCohortAttributes();
		model.addAttribute("attypes",att);
		String atype=request.getParameter("cohortAttributeTypeId");
		if(StringUtils.hasText(atype))
		{
		a=Integer.parseInt(atype);
		}
		List<CohortAttributeType> cat=s.findCohortAttType(a);
		if(cat.size()>0)
		{
			coat=cat.get(0);
		}
		/*if(cohortattribute!=null)
		{
		//cohortattribute=new CohortAttribute();
		//}*/
		List<CohortM> c=s.findCohort(id);
		if(c.size()>0)
		{
		cohort=c.get(0);
		}
		model.addAttribute("cohortmodule",cohort);
		cohortattribute.setCohort(cohort);
		cohortattribute.setCohortAttributeType(coat);
		model.addAttribute("cohortatt",cohortattribute);
		//}
		
		/*model.addAttribute("cohort",c.get(0));*/
		model.addAttribute("selectedvalue",request.getParameter("selectedvalue"));
		
	}
	 @RequestMapping(value = "/module/cohort/addcohortattributes.form", method = RequestMethod.POST)
	    public String onSubmit(WebRequest request, HttpSession httpSession, ModelMap model,
	    		@RequestParam(required=false, value="cohortAttributeTypeId")Integer cohort_attribute_type,
                @RequestParam(required = false, value = "selectedvalue") String description,
	                                   @ModelAttribute("cohortatt") CohortAttribute cohortattribute,BindingResult errors) {
	        CohortService departmentService = Context.getService(CohortService.class);
	        //PatientService patientService=Context.getService(PatientService.class);
		  // List<String> cohortm=new ArrayList<String>();
	       //CohortM cohort=null;
	       CohortAttributeType a=null;
	       Integer id=Integer.parseInt(request.getParameter("ca"));
	       List<CohortM> cohort1=departmentService.findCohort(id);
	        if(description=="")
	        {
	        	httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Values cannot be null");
	        }
	       List<CohortAttributeType> att=departmentService.findCohortAttType(cohort_attribute_type);
	    	for(int i=0;i<att.size();i++)
	    	{
	    			 a=att.get(i);
	    	}
	    	cohortattribute.setCohort(cohort1.get(0));
	        cohortattribute.setValue(description);
	    	cohortattribute.setCohortAttributeType(a);
	    	departmentService.saveCohortAttributes(cohortattribute); 
	        httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "insertion success");
	        if("Next".equalsIgnoreCase(request.getParameter("next")))
	        	{
	        		departmentService.saveCohortAttributes(cohortattribute); 
		        	httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,"insertion success");
		        	String redirectUrl="/module/cohort/cpatients.form?cpid="+cohortattribute.getCohort().getCohortId();
 		        	return "redirect:" + redirectUrl;	
	        	}
	        return null;
	     }
}