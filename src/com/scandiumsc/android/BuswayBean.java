package com.scandiumsc.android;

import java.util.List;
import java.util.Vector;


public class BuswayBean
{
	private int id;
	private String name;
	private int nameId;
	private String neighbour;
	private int neighbourId;
	private double latitude;
	private double longitude;
	private double line;
	private double pole;
	private List<Edge> edge;
	
	public BuswayBean()
	{
		id = 0;
		name = null;
		latitude = 0.0d;
		longitude = 0.0d;
		line = 0;
		pole = 0;
		edge = null;
	}
	
	public BuswayBean(int id)
	{
		super();
		setId(id);
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getNameId()
	{
		return nameId;
	}

	public void setNameId(int nameId)
	{
		this.nameId = nameId;
	}

	public String getNeighbour()
	{
		return neighbour;
	}
	public void setNeighbour(String neighbour)
	{
		this.neighbour = neighbour;
	}
	public int getNeighbourId()
	{
		return neighbourId;
	}

	public void setNeighbourId(int neighbourId)
	{
		this.neighbourId = neighbourId;
	}

	public double getLatitude()
	{
		return latitude;
	}
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	public double getLongitude()
	{
		return longitude;
	}
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public double getLine()
	{
		return line;
	}

	public void setLine(double line)
	{
		this.line = line;
	}

	public double getPole()
	{
		return pole;
	}

	public void setPole(double pole)
	{
		this.pole = pole;
	}

	public List<Edge> getEdge()
	{
		if(edge == null)
			edge = new Vector<Edge>(32);
		return edge;
	}
	public void setEdge(List<Edge> edge)
	{
		this.edge = edge;
	}
}
