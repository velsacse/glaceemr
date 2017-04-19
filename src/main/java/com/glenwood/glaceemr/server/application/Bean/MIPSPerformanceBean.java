package com.glenwood.glaceemr.server.application.Bean;

public class MIPSPerformanceBean {

	String cmsId;
	String title;
	String measureId;
	int criteria;
	int reportingYear;
	
	long ippCount;
	String ippPatientsList;
	
	long denominatorCount;
	String denominatorPatientsList;
	
	long denominatorExclusionCount;
	String denominatorExclusionPatientsList;
	
	long numeratorCount;
	String numeratorPatientsList;
	
	long numeratorExclusionCount;
	String numeratorExclusionPatientsList;
	
	long denominatorExceptionCount;
	String denominatorExceptionPatientsList;

	double performanceRate;
	double reportingRate;
	
	String npi;
	String tin;
	
	/*long notMetPatients;
	String notMetPatientsList;*/
	
	public MIPSPerformanceBean(){
		
	}
	
	public MIPSPerformanceBean(String measureId, int criteria, int reportingYear,
			int ippCount, String ippPatientsList, int denominatorCount,
			String denominatorPatientsList, int denominatorExclusionCount,
			String denominatorExclusionPatientsList, int numeratorCount,
			String numeratorPatientsList, int numeratorExclusionCount,
			String numeratorExclusionPatientsList,
			int denominatorExceptionCount,
			String denominatorExceptionPatientsList, double performanceRate,
			double reportingRate) {
		
		this.measureId = measureId;
		this.criteria = criteria;
		this.reportingYear = reportingYear;
		this.ippCount = ippCount;
		this.ippPatientsList = ippPatientsList;
		this.denominatorCount = denominatorCount;
		this.denominatorPatientsList = denominatorPatientsList;
		this.denominatorExclusionCount = denominatorExclusionCount;
		this.denominatorExclusionPatientsList = denominatorExclusionPatientsList;
		this.numeratorCount = numeratorCount;
		this.numeratorPatientsList = numeratorPatientsList;
		this.numeratorExclusionCount = numeratorExclusionCount;
		this.numeratorExclusionPatientsList = numeratorExclusionPatientsList;
		this.denominatorExceptionCount = denominatorExceptionCount;
		this.denominatorExceptionPatientsList = denominatorExceptionPatientsList;
		this.performanceRate = performanceRate;
		this.reportingRate = reportingRate;
		
	}
	
	public MIPSPerformanceBean(String tin,String measureId, int criteria, int reportingYear,
			long ippCount, String ippPatientsList, long denominatorCount,
			String denominatorPatientsList, long denominatorExclusionCount,
			String denominatorExclusionPatientsList, long numeratorCount,
			String numeratorPatientsList, long numeratorExclusionCount,
			String numeratorExclusionPatientsList,
			long denominatorExceptionCount,
			String denominatorExceptionPatientsList) {
		
		this.tin = tin;
		this.measureId = measureId;
		this.criteria = criteria;
		this.reportingYear = reportingYear;
		this.ippCount = ippCount;
		this.ippPatientsList = ippPatientsList;
		this.denominatorCount = denominatorCount;
		this.denominatorPatientsList = denominatorPatientsList;
		this.denominatorExclusionCount = denominatorExclusionCount;
		this.denominatorExclusionPatientsList = denominatorExclusionPatientsList;
		this.numeratorCount = numeratorCount;
		this.numeratorPatientsList = numeratorPatientsList;
		this.numeratorExclusionCount = numeratorExclusionCount;
		this.numeratorExclusionPatientsList = numeratorExclusionPatientsList;
		this.denominatorExceptionCount = denominatorExceptionCount;
		this.denominatorExceptionPatientsList = denominatorExceptionPatientsList;
		
	}
	
	public MIPSPerformanceBean(String measureId, int criteria, int reportingYear,
			long ippCount, String ippPatientsList, long denominatorCount,
			String denominatorPatientsList, long denominatorExclusionCount,
			String denominatorExclusionPatientsList, long numeratorCount,
			String numeratorPatientsList, long numeratorExclusionCount,
			String numeratorExclusionPatientsList,
			long denominatorExceptionCount,
			String denominatorExceptionPatientsList) {
		
		this.measureId = measureId;
		this.criteria = criteria;
		this.reportingYear = reportingYear;
		this.ippCount = ippCount;
		this.ippPatientsList = ippPatientsList;
		this.denominatorCount = denominatorCount;
		this.denominatorPatientsList = denominatorPatientsList;
		this.denominatorExclusionCount = denominatorExclusionCount;
		this.denominatorExclusionPatientsList = denominatorExclusionPatientsList;
		this.numeratorCount = numeratorCount;
		this.numeratorPatientsList = numeratorPatientsList;
		this.numeratorExclusionCount = numeratorExclusionCount;
		this.numeratorExclusionPatientsList = numeratorExclusionPatientsList;
		this.denominatorExceptionCount = denominatorExceptionCount;
		this.denominatorExceptionPatientsList = denominatorExceptionPatientsList;
		
	}

	public MIPSPerformanceBean(String measureId, int criteria,int reportingYear,
			long ippCount, long denominatorCount,
			long denominatorExclusionCount, long numeratorCount,
			long numeratorExclusionCount, long denominatorExceptionCount) {
		
		this.measureId = measureId;
		this.criteria = criteria;
		this.reportingYear = reportingYear;
		this.ippCount = ippCount;
		this.denominatorCount = denominatorCount;
		this.denominatorExclusionCount = denominatorExclusionCount;
		this.numeratorCount = numeratorCount;
		this.numeratorExclusionCount = numeratorExclusionCount;
		this.denominatorExceptionCount = denominatorExceptionCount;
		
	}

	public String getNpi() {
		return npi;
	}

	public void setNpi(String npi) {
		this.npi = npi;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public int getCriteria() {
		return criteria;
	}

	public void setCriteria(int criteria) {
		this.criteria = criteria;
	}

	public double getReportingRate() {
		return reportingRate;
	}

	public void setReportingRate(double reportingRate) {
		this.reportingRate = reportingRate;
	}

	public String getCmsId() {
		return cmsId;
	}

	public void setCmsId(String cmsId) {
		this.cmsId = cmsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMeasureId() {
		return measureId;
	}
	
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	
	public int getReportingYear() {
		return reportingYear;
	}
	
	public void setReportingYear(int reportingYear) {
		this.reportingYear = reportingYear;
	}
	
	/*public long getNotMetPatients() {
		return notMetPatients;
	}

	public void setNotMetPatients(long notMetPatients) {
		this.notMetPatients = notMetPatients;
	}

	public String getNotMetPatientsList() {
		return notMetPatientsList;
	}

	public void setNotMetPatientsList(String notMetPatientsList) {
		this.notMetPatientsList = notMetPatientsList;
	}*/

	public long getIppCount() {
		return ippCount;
	}
	
	public double getPerformanceRate() {
		return performanceRate;
	}

	public void setPerformanceRate(double performanceRate) {
		this.performanceRate = performanceRate;
	}

	public void setIppCount(long ippCount) {
		this.ippCount = ippCount;
	}
	
	public long getDenominatorCount() {
		return denominatorCount;
	}
	
	public void setDenominatorCount(long denominatorCount) {
		this.denominatorCount = denominatorCount;
	}
	
	public long getDenominatorExclusionCount() {
		return denominatorExclusionCount;
	}
	
	public void setDenominatorExclusionCount(long denominatorExclusionCount) {
		this.denominatorExclusionCount = denominatorExclusionCount;
	}
	
	public long getNumeratorCount() {
		return numeratorCount;
	}
	
	public void setNumeratorCount(long numeratorCount) {
		this.numeratorCount = numeratorCount;
	}
	
	public long getNumeratorExclusionCount() {
		return numeratorExclusionCount;
	}
	
	public void setNumeratorExclusionCount(long numeratorExclusionCount) {
		this.numeratorExclusionCount = numeratorExclusionCount;
	}
	
	public long getDenominatorExceptionCount() {
		return denominatorExceptionCount;
	}
	
	public void setDenominatorExceptionCount(long denominatorExceptionCount) {
		this.denominatorExceptionCount = denominatorExceptionCount;
	}

	public String getIppPatientsList() {
		return ippPatientsList;
	}

	public void setIppPatientsList(String ippPatientsList) {
		this.ippPatientsList = ippPatientsList;
	}

	public String getDenominatorPatientsList() {
		return denominatorPatientsList;
	}

	public void setDenominatorPatientsList(String denominatorPatientsList) {
		this.denominatorPatientsList = denominatorPatientsList;
	}

	public String getDenominatorExclusionPatientsList() {
		return denominatorExclusionPatientsList;
	}

	public void setDenominatorExclusionPatientsList(
			String denominatorExclusionPatientsList) {
		this.denominatorExclusionPatientsList = denominatorExclusionPatientsList;
	}

	public String getNumeratorPatientsList() {
		return numeratorPatientsList;
	}

	public void setNumeratorPatientsList(String numeratorPatientsList) {
		this.numeratorPatientsList = numeratorPatientsList;
	}

	public String getNumeratorExclusionPatientsList() {
		return numeratorExclusionPatientsList;
	}

	public void setNumeratorExclusionPatientsList(
			String numeratorExclusionPatientsList) {
		this.numeratorExclusionPatientsList = numeratorExclusionPatientsList;
	}

	public String getDenominatorExceptionPatientsList() {
		return denominatorExceptionPatientsList;
	}

	public void setDenominatorExceptionPatientsList(
			String denominatorExceptionPatientsList) {
		this.denominatorExceptionPatientsList = denominatorExceptionPatientsList;
	}
	
}