package com.scandiumsc.android;

public class Edge
{
	private BuswayBean vertexBegin;
	private BuswayBean vertexEnd;
	
	public Edge()
	{
		vertexBegin = null;
		vertexEnd = null;
	}
	
	public BuswayBean getVertexBegin()
	{
		return vertexBegin;
	}
	public void setVertexBegin(BuswayBean vertexBegin)
	{
		this.vertexBegin = vertexBegin;
	}
	public BuswayBean getVertexEnd()
	{
		return vertexEnd;
	}
	public void setVertexEnd(BuswayBean vertexEnd)
	{
		this.vertexEnd = vertexEnd;
	}

}

