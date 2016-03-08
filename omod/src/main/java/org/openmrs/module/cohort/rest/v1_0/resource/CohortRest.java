package org.openmrs.module.cohort.rest.v1_0.resource;

import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_1 +CohortRest.COHORT_NAMESPACE)
public class CohortRest extends MainResourceController {
	/**
	 * * @see org.openmrs.module.webservices.rest.web.v1_0.controller.
	 * BaseRestController#getNamespace()
	 */
	public static final String COHORT_NAMESPACE = "/cohortm";
	@Override
	public String getNamespace() {
		return RestConstants.VERSION_1 + COHORT_NAMESPACE;
	}
}