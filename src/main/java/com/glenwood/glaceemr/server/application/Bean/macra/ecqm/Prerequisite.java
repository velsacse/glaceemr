package com.glenwood.glaceemr.server.application.Bean.macra.ecqm;

import java.util.ArrayList;
import java.util.List;

public class Prerequisite
{
	private List<EMeasure> measureList = new ArrayList<EMeasure>();

	public List<EMeasure> getMeasureList()
	{
		return measureList;
	}

	public void setMeasureList(List<EMeasure> measureList)
	{
		this.measureList = measureList;
	}
	
}
