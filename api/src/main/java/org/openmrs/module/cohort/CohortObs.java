package org.openmrs.module.cohort;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNumeric;
import org.openmrs.Drug;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.obs.ComplexData;
import org.openmrs.util.Format;
import org.openmrs.util.Format.FORMAT_TYPE;

public class CohortObs extends  BaseOpenmrsData {
	
	private Integer obsId;
	private CohortM cohort;
	protected Concept concept;
	private CohortEncounter encounterId;
	private Location location;
	private Date obsDateTime;
	
	protected Set<CohortObs> groupMembers;
	
	private static final Log log = LogFactory.getLog(CohortObs.class);
	
	protected Obs obsGroup;
	
	protected String comment;
	
    protected Concept valueCoded;
	
	protected ConceptName valueCodedName;
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	protected Integer valueGroupId;
	
	public Integer getValueGroupId() {
		return valueGroupId;
	}
	public void setValueGroupId(Integer valueGroupId) {
		this.valueGroupId = valueGroupId;
	}
	public String getValueModifier() {
		return valueModifier;
	}
	public void setValueModifier(String valueModifier) {
		this.valueModifier = valueModifier;
	}

	protected Date valueDatetime;
	
	protected Double valueNumeric;
	
	protected String valueModifier;
	
	protected String valueText;
	
	protected transient ComplexData complexData;
	
	protected String valueComplex;
	
	protected String accessionNumber;
	
private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	private static DateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public Integer getId() {
		return getObsId();
	}
	@Override
	public void setId(Integer id) {
		setObsId(id);
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public CohortM getCohort() {
		return cohort;
	}
	public void setCohort(CohortM cohort) {
		this.cohort = cohort;
	}
	public Integer getObsId() {
		return obsId;
	}
	public void setObsId(Integer obsId) {
		this.obsId = obsId;
	}
	public CohortEncounter getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(CohortEncounter encounterId) {
		this.encounterId = encounterId;
	}
	public Date getObsDateTime() {
		return obsDateTime;
	}
	public void setObsDateTime(Date obsDateTime) {
		this.obsDateTime = obsDateTime;
	}
	
	public boolean hasGroupMembers(boolean includeVoided) {
		// ! symbol used because if it's not empty, we want true
		return !org.springframework.util.CollectionUtils.isEmpty(getGroupMembers(includeVoided));
	}
	public boolean hasGroupMembers() {
		return hasGroupMembers(false);
	}
	
	public Set<CohortObs> getGroupMembers() {
		return getGroupMembers(false); //same as just returning groupMembers
	}
	
	public Obs getObsGroup() {
		return obsGroup;
	}
	
	public boolean isObsGrouping() {
		return hasGroupMembers(true);
	}
	
	public void setObsGroup(Obs obsGroup) {
		this.obsGroup = obsGroup;
	}
	
	public Set<CohortObs> getGroupMembers(boolean includeVoided) {
		if (includeVoided) {
			//just return all group members
			return groupMembers;
		}
		if (groupMembers == null) {
			//Empty set so return null
			return null;
		}
		Set<CohortObs> nonVoided = new LinkedHashSet<CohortObs>(groupMembers);
		Iterator<CohortObs> i = nonVoided.iterator();
		while (i.hasNext()) {
			CohortObs obs = i.next();
			if (obs.isVoided()) {
				i.remove();
			}
		}
		return nonVoided;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment;
	}
	
	public Concept getConcept() {
		return concept;
	}
	
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public String getValueAsString(Locale locale) {
		// formatting for the return of numbers of type double
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		DecimalFormat df = (DecimalFormat) nf;
		df.applyPattern("#0.0#####"); // formatting style up to 6 digits
		//branch on hl7 abbreviations
		if (getConcept() != null) {
			String abbrev = getConcept().getDatatype().getHl7Abbreviation();
			if ("BIT".equals(abbrev)) {
				return getValueAsBoolean() == null ? "" : getValueAsBoolean().toString();
			} else if ("CWE".equals(abbrev)) {
				if (getValueCoded() == null) {
					return "";
				}
			   else {
					ConceptName valueCodedName = getValueCodedName();
					if (valueCodedName != null) {
						return getValueCoded().getName(locale, false).getName();
					} else {
						ConceptName fallbackName = getValueCoded().getName();
						if (fallbackName != null) {
							return fallbackName.getName();
						} else {
							return "";
						}
						
					}
				}
			} else if ("NM".equals(abbrev) || "SN".equals(abbrev)) {
				if (getValueNumeric() == null) {
					return "";
				} else {
					if (getConcept() instanceof ConceptNumeric) {
						ConceptNumeric cn = (ConceptNumeric) getConcept();
						if (!cn.isPrecise()) {
							double d = getValueNumeric();
							int i = (int) d;
							return Integer.toString(i);
						} else {
							df.format(getValueNumeric());
						}
					}
				}
			} else if ("DT".equals(abbrev)) {
				return (getValueDatetime() == null ? "" : dateFormat.format(getValueDatetime()));
			} else if ("TM".equals(abbrev)) {
				return (getValueDatetime() == null ? "" : Format.format(getValueDatetime(), locale, FORMAT_TYPE.TIME));
			} else if ("TS".equals(abbrev)) {
				return (getValueDatetime() == null ? "" : Format.format(getValueDatetime(), locale, FORMAT_TYPE.TIMESTAMP));
			} else if ("ST".equals(abbrev)) {
				return getValueText();
			} else if ("ED".equals(abbrev) && getValueComplex() != null) {
				String[] valueComplex = getValueComplex().split("\\|");
				for (int i = 0; i < valueComplex.length; i++) {
					if (StringUtils.isNotEmpty(valueComplex[i])) {
						return valueComplex[i].trim();
					}
				}
			}
		}
		
		// if the datatype is 'unknown', default to just returning what is not null
		if (getValueNumeric() != null) {
			return df.format(getValueNumeric());
		} else if (getValueCoded() != null) {
			ConceptName valudeCodedName = getValueCodedName();
				if (valudeCodedName != null) {
					return valudeCodedName.getName();
				} else {
					return "";
				}
		} else if (getValueDatetime() != null) {
			return Format.format(getValueDatetime(), locale, FORMAT_TYPE.DATE);
		} else if (getValueText() != null) {
			return getValueText();
		} else if (hasGroupMembers()) {
			// all of the values are null and we're an obs group...so loop
			// over the members and just do a getValueAsString on those
			// this could potentially cause an infinite loop if an obs group
			// is a member of its own group at some point in the hierarchy
			StringBuilder sb = new StringBuilder();
			for (CohortObs groupMember : getGroupMembers()) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(groupMember.getValueAsString(locale));
			}
			return sb.toString();
		}
		
		// returns the title portion of the valueComplex
		// which is everything before the first bar '|' character.
		if (getValueComplex() != null) {
			String[] valueComplex = getValueComplex().split("\\|");
			for (int i = 0; i < valueComplex.length; i++) {
				if (StringUtils.isNotEmpty(valueComplex[i])) {
					return valueComplex[i].trim();
				}
			}
		}
		
		return "";
	}
	
public Boolean getValueAsBoolean() {
		
		if (getValueCoded() != null) {
			if (getValueCoded().equals(Context.getConceptService().getTrueConcept())) {
				return Boolean.TRUE;
			} else if (getValueCoded().equals(Context.getConceptService().getFalseConcept())) {
				return Boolean.FALSE;
			}
		} else if (getValueNumeric() != null) {
			if (getValueNumeric() == 1) {
				return Boolean.TRUE;
			} else if (getValueNumeric() == 0) {
				return Boolean.FALSE;
			}
		}
		//returning null is preferred to defaulting to false to support validation of user input is from a form
		return null;
	}
	
	/**
	 * Returns the boolean value if the concept of this obs is of boolean datatype
	 * 
	 * @return true or false if value is set otherwise null
	 * @should return true if value coded answer concept is true concept
	 * @should return false if value coded answer concept is false concept
	 */
	public Boolean getValueBoolean() {
		if (getConcept() != null && valueCoded != null && getConcept().getDatatype().isBoolean()) {
			Concept trueConcept = Context.getConceptService().getTrueConcept();
			return trueConcept != null && valueCoded.getId().equals(trueConcept.getId());
		}
		
		return null;
	}
	
	/**
	 * @return Returns the valueCoded.
	 */
	public Concept getValueCoded() {
		return valueCoded;
	}
	
	/**
	 * @param valueCoded The valueCoded to set.
	 */
	public void setValueCoded(Concept valueCoded) {
		this.valueCoded = valueCoded;
	}
	
	/**
	 * Gets the specific name used for the coded value.
	 * 
	 * @return the name of the coded value
	 */
	public ConceptName getValueCodedName() {
		return valueCodedName;
	}
	
	/**
	 * Sets the specific name used for the coded value.
	 * 
	 * @param valueCodedName the name of the coded value
	 */
	public void setValueCodedName(ConceptName valueCodedName) {
		this.valueCodedName = valueCodedName;
	}
	
	public Double getValueNumeric() {
		return valueNumeric;
	}
	
	/**
	 * @param valueNumeric The valueNumeric to set.
	 */
	public void setValueNumeric(Double valueNumeric) {
		this.valueNumeric = valueNumeric;
	}
	
	public String getValueComplex() {
		return this.valueComplex;
	}
	
	/**
	 * Set the value for the ComplexData. This method is used by the ComplexObsHandler. The
	 * valueComplex has two parts separated by a bar '|' character: part A) the title; and part B)
	 * the URI. The title is the readable description of the valueComplex that is returned by
	 * Obs.getValueAsString(). The URI is the location where the ComplexData is stored.
	 * 
	 * @param valueComplex readable title and URI for the location of the ComplexData binary object.
	 * @since 1.5
	 */
	public void setValueComplex(String valueComplex) {
		this.valueComplex = valueComplex;
	}	
	
	public Date getValueDatetime() {
		return valueDatetime;
	}
	
	/**
	 * @param valueDatetime The valueDatetime to set.
	 */
	public void setValueDatetime(Date valueDatetime) {
		this.valueDatetime = valueDatetime;
	}
	
	public String getValueText() {
		return valueText;
	}
	
	/**
	 * @param valueText The valueText to set.
	 */
	public void setValueText(String valueText) {
		this.valueText = valueText;
	}
	
	public String getAccessionNumber() {
		return accessionNumber;
	}
	
	/**
	 * @param accessionNumber The accessionNumber to set.
	 */
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	
}
	
	public void setValueAsString(String s) throws ParseException {
		if (log.isDebugEnabled()) {
			log.debug("getConcept() == " + getConcept());
		}
		
		if (getConcept() != null && !StringUtils.isBlank(s)) {
			String abbrev = getConcept().getDatatype().getHl7Abbreviation();
			if ("BIT".equals(abbrev)) {
				setValueBoolean(Boolean.valueOf(s));
			} else if ("CWE".equals(abbrev)) {
				throw new RuntimeException("Not Yet Implemented");
			} else if ("NM".equals(abbrev) || "SN".equals(abbrev)) {
				setValueNumeric(Double.valueOf(s));
			} else if ("DT".equals(abbrev)) {
				setValueDatetime(dateFormat.parse(s));
			} else if ("TM".equals(abbrev)) {
				setValueDatetime(timeFormat.parse(s));
			} else if ("TS".equals(abbrev)) {
				setValueDatetime(datetimeFormat.parse(s));
			} else if ("ST".equals(abbrev)) {
				setValueText(s);
			} else {
				throw new RuntimeException("Don't know how to handle " + abbrev);
			}
			
		} else {
			throw new RuntimeException("concept is null for " + this);
		}
	}
	
	@Deprecated
	public Map<Locale, String> getValueAsString() {
		Map<Locale, String> localeMap = new HashMap<Locale, String>();
		Locale[] locales = Locale.getAvailableLocales(); // ABKTODO: get actual available locales
		for (int i = 0; i < locales.length; i++) {
			localeMap.put(locales[i], getValueAsString(locales[i]));
		}
		return localeMap;
	}
	
	public void setValueBoolean(Boolean valueBoolean) {
		if (valueBoolean != null && getConcept() != null && getConcept().getDatatype().isBoolean()) {
			setValueCoded(valueBoolean.booleanValue() ? Context.getConceptService().getTrueConcept() : Context
			        .getConceptService().getFalseConcept());
		} else if (valueBoolean == null) {
			setValueCoded(null);
		}
	}
	
	public void setComplexData(ComplexData complexData) {
		this.complexData = complexData;
	}
	
	public ComplexData getComplexData() {
		return this.complexData;
	}
}
	
	
	
	
	
