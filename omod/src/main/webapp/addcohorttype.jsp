<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js"/>
<openmrs:htmlInclude file="/scripts/jquery/jquery-1.3.2.min.js" />

<script type="text/javascript" charset="utf-8"></script>
<h3>Add Cohort Type</h3>
<form method="post">
<spring:bind path="cohorttype.name">
<spring:message code="cohort.cohorttypename" /> :<br/>
<input type= "text" name="name" id="name" size="25" value="${status.value}"/><br/>
<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
            </spring:bind>
<spring:bind path="cohorttype.description">
<spring:message code="cohort.cohorttypedescription" /> : <br/>
 <textarea rows="4" name="description" id="description" cols="50" value="${status.value}"></textarea><br/>
 <c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
            </spring:bind>
<input type="submit" value="Add Cohort Type" id="submit"/><br/>				   
</form>
<%@ include file="/WEB-INF/template/footer.jsp"%>