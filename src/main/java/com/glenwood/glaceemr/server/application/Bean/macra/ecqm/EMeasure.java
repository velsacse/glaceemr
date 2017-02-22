package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.util.ArrayList;
import java.util.List;

public class EMeasure
{
	private int id;
	private String cmsId = new String();
	private String title = new String();
	private String description = new String();
	private String NQFNumber = new String();
	private String NQSDomain = new String();
	private String type = new String();
	private boolean isHighPriority = false;
	private String guid = new String();
	private String scoring = new String();
	private String rationale = new String();
	private String clinicalRecomendationStatement = new String();
	private String improvementNotation = new String();
	private List<String> references = new ArrayList<String>();
	private String definition = new String();
	private String guidance = new String();
	private String ipp = new String();
	private String denominator = new String();
	private String denominator_exclusion = new String();
	private String numerator = new String();
	private String numerator_exclusion = new String();
	private String denominator_exception = new String();
	private String suplemental_data_elements = new String();
	private CQMSpecification specification = new CQMSpecification();
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getCmsId()
	{
		return cmsId;
	}
	public void setCmsId(String cmsId)
	{
		this.cmsId = cmsId;
	}
	public String getNQFNumber()
	{
		return NQFNumber;
	}
	public void setNQFNumber(String nQFNumber)
	{
		NQFNumber = nQFNumber;
	}
	public String getGuid()
	{
		return guid;
	}
	public void setGuid(String guid)
	{
		this.guid = guid;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getScoring()
	{
		return scoring;
	}
	public void setScoring(String scoring)
	{
		this.scoring = scoring;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getRationale()
	{
		return rationale;
	}
	public void setRationale(String rationale)
	{
		this.rationale = rationale;
	}
	public String getClinicalRecomendationStatement()
	{
		return clinicalRecomendationStatement;
	}
	public void setClinicalRecomendationStatement(String clinicalRecomendationStatement)
	{
		this.clinicalRecomendationStatement = clinicalRecomendationStatement;
	}
	public String getImprovementNotation()
	{
		return improvementNotation;
	}
	public void setImprovementNotation(String improvementNotation)
	{
		this.improvementNotation = improvementNotation;
	}
	public List<String> getReferences()
	{
		return references;
	}
	public void setReferences(List<String> references)
	{
		this.references = references;
	}
	public String getDefinition()
	{
		return definition;
	}
	public void setDefinition(String definition)
	{
		this.definition = definition;
	}
	public String getGuidance()
	{
		return guidance;
	}
	public void setGuidance(String guidance)
	{
		this.guidance = guidance;
	}
	public String getIpp()
	{
		return ipp;
	}
	public void setIpp(String ipp)
	{
		this.ipp = ipp;
	}
	public String getDenominator()
	{
		return denominator;
	}
	public void setDenominator(String denominator)
	{
		this.denominator = denominator;
	}
	public String getDenominator_exclusion()
	{
		return denominator_exclusion;
	}
	public void setDenominator_exclusion(String denominator_exclusion)
	{
		this.denominator_exclusion = denominator_exclusion;
	}
	public String getNumerator()
	{
		return numerator;
	}
	public void setNumerator(String numerator)
	{
		this.numerator = numerator;
	}
	public String getNumerator_exclusion()
	{
		return numerator_exclusion;
	}
	public void setNumerator_exclusion(String numerator_exclusion)
	{
		this.numerator_exclusion = numerator_exclusion;
	}
	public String getDenominator_exception()
	{
		return denominator_exception;
	}
	public void setDenominator_exception(String denominator_exception)
	{
		this.denominator_exception = denominator_exception;
	}
	public String getSuplemental_data_elements()
	{
		return suplemental_data_elements;
	}
	public void setSuplemental_data_elements(String suplemental_data_elements)
	{
		this.suplemental_data_elements = suplemental_data_elements;
	}
	public CQMSpecification getSpecification()
	{
		return specification;
	}
	public void setSpecification(CQMSpecification specification)
	{
		this.specification = specification;
	}
	public String getNQSDomain()
	{
		return NQSDomain;
	}
	public void setNQSDomain(String nQSDomain)
	{
		NQSDomain = nQSDomain;
	}
	public boolean isHighPriority()
	{
		return isHighPriority;
	}
	public void setHighPriority(boolean isHighPriority)
	{
		this.isHighPriority = isHighPriority;
	}
}
