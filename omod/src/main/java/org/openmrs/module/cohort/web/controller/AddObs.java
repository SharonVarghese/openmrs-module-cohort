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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.FormService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortEncounter;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortObs;
import org.openmrs.module.cohort.CohortVisit;
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
public class AddObs {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/cohort/addobs", method = RequestMethod.GET)
	public void manage(ModelMap map) {
		map.addAttribute("cohortobs", new CohortObs());
		List<String> cohortnames = new ArrayList<String>();
		CohortService cservice = Context.getService(CohortService.class);
		List<CohortM> list1 = cservice.findCohorts();
		for (int i = 0; i < list1.size(); i++) {
			CohortM c = list1.get(i);
			cohortnames.add(c.getName());
		}
		List<Integer> etype = new ArrayList<Integer>();
		List<CohortEncounter> enctypes = cservice.findCohortEncounters();
		for (int l = 0; l < enctypes.size(); l++) {
			CohortEncounter e = enctypes.get(l);
			etype.add(e.getId());
		}
		map.addAttribute("encids", etype);
		LocationService service = Context.getLocationService();
		List<Location> formats = service.getAllLocations();
		ConceptService cs = Context.getConceptService();
		List<Concept> concept = cs.getAllConcepts();
		map.addAttribute("concepts", concept);
		map.addAttribute("locations", formats);
		map.addAttribute("formats1", cohortnames);
	}
	
	@RequestMapping(value = "/module/cohort/addobs.form", method = RequestMethod.POST)
	public ModelAndView onSubmit(WebRequest request, HttpSession httpSession, ModelMap model,
	                             @RequestParam(required = false, value = "obsDateTime") String date,
	                             @ModelAttribute("cohortobs") CohortObs cobs, BindingResult errors) {
		CohortService departmentService = Context.getService(CohortService.class);
		if (!Context.isAuthenticated()) {
			errors.reject("Required");
		}
		
		Location l = new Location();
		Concept co = new Concept();
		LocationService service = Context.getLocationService();
		ConceptService cs = Context.getConceptService();
		List<Concept> concept = cs.getAllConcepts();
		List<String> cohortnames = new ArrayList<String>();
		CohortService cservice = Context.getService(CohortService.class);
		CohortM c1 = new CohortM();
		CohortEncounter ce = new CohortEncounter();
		
		List<CohortM> list1 = cservice.findCohorts();
		for (int d = 0; d < list1.size(); d++) {
			CohortM c = list1.get(d);
			cohortnames.add(c.getName());
		}
		List<Integer> etype = new ArrayList<Integer>();
		List<CohortEncounter> enctypes = cservice.findCohortEncounters();
		for (int a = 0; a < enctypes.size(); a++) {
			CohortEncounter e = enctypes.get(a);
			etype.add(e.getId());
		}
		List<Location> formats = service.getAllLocations();
		model.addAttribute("concepts", concept);
		model.addAttribute("locations", formats);
		model.addAttribute("encids", etype);
		
		String encounterid = request.getParameter("encid");
		Integer id = Integer.parseInt(encounterid);
		String location = request.getParameter("location");
		String cohort = request.getParameter("names");
		String concept1 = request.getParameter("concept");
		if (date == "") {
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Values cannot be null");
		} else {
			List<CohortM> cohort2 = cservice.findCohorts(cohort);
			for (int i = 0; i < cohort2.size(); i++) {
				c1 = cohort2.get(i);
			}
			List<CohortEncounter> enctypes1 = cservice.findCohortEnc(id);
			for (int g = 0; g < enctypes1.size(); g++) {
				ce = enctypes1.get(g);
			}
			List<Location> loc = service.getLocations(location);
			for (int j = 0; j < loc.size(); j++) {
				l = loc.get(j);
			}
			List<Concept> con = cs.getConceptsByName(concept1);
			for (int x = 0; x < con.size(); x++) {
				co = con.get(x);
			}
			cobs.setCohort(c1);
			//cobs.setVoided(false);
			//cobs.setConcept(co);
			cobs.setEncounterId(ce);
			cobs.setLocation(l);
			departmentService.saveCohortObs(cobs);
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "insertion success");
		}
		return null;
	}
	
}
