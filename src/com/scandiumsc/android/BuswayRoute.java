package com.scandiumsc.android;


public class BuswayRoute implements Comparable<Object>, Cloneable
{
	private int nextNeighbourId;
	private int frontVertexId;
	private double cost;
	
	public BuswayRoute()
	{
		nextNeighbourId = 0;
		frontVertexId = 0;
		cost = 0;
	}

	public int getNextNeighbourId()
	{
		return nextNeighbourId;
	}
	public void setNextNeighbourId(int nextNeighbourId)
	{
		this.nextNeighbourId = nextNeighbourId;
	}
	public int getFrontVertexId()
	{
		return frontVertexId;
	}
	public void setFrontVertexId(int frontVertexId)
	{
		this.frontVertexId = frontVertexId;
	}
	public double getCost()
	{
		return cost;
	}
	public void setCost(double cost)
	{
		this.cost = cost;
	}

	@Override
	public BuswayRoute clone()
	{
		BuswayRoute bean = new BuswayRoute();
		try
		{
			bean = (BuswayRoute) super.clone();
		}
		catch (CloneNotSupportedException e1)
		{
			e1.printStackTrace();
		}
		return bean;
	}

	public void clean()
	{
		nextNeighbourId = 0;
		frontVertexId = 0;
		cost = 0;
	}
	@Override
	public int compareTo(Object another)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
