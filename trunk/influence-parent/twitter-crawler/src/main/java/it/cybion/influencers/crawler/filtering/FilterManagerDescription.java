package it.cybion.influencers.crawler.filtering;

import java.util.Map;

public class FilterManagerDescription
{
	private String filterManagerName;
	private Map<String,Object> parameterName2ParameterValue;
	
	public FilterManagerDescription(String filterManagerName,
									Map<String, Object> parameterName2ParameterValue)
	{
		this.setFilterManagerName(filterManagerName);
		this.setParameterName2ParameterValue(parameterName2ParameterValue);
	}

	public String getFilterManagerName(){return filterManagerName;}
	public void setFilterManagerName(String filterManagerName){	this.filterManagerName = filterManagerName;	}
	public Map<String,Object> getParameterName2ParameterValue(){return parameterName2ParameterValue;}
	public void setParameterName2ParameterValue(Map<String,Object> parameterName2ParameterValue){this.parameterName2ParameterValue = parameterName2ParameterValue;}
	
	
}
