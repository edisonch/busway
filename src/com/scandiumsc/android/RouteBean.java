package com.scandiumsc.android;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;

public class RouteBean 
{
  private String name;
  private final List<GeoPoint> points;
  private List<Segment> segments;
  private String copyright;
  private String warning;
  private String country;
  private int length;
  private String txtLength;
  private int durationSecond;
  private String txtDuration;
  private String polyline;

  public RouteBean() {
          points = new ArrayList<GeoPoint>();
          segments = new ArrayList<Segment>();
  }

  public void addPoint(final GeoPoint p) {
          points.add(p);
  }

  public void addPoints(final List<GeoPoint> points) {
          this.points.addAll(points);
  }

  public List<GeoPoint> getPoints() {
          return points;
  }

  public void addSegment(final Segment segment) {
          segments.add(segment);
  }

  public List<Segment> getSegments() {
          return segments;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
          this.name = name;
  }

  /**
   * @return the name
   */
  public String getName() {
          return name;
  }

  /**
   * @param copyright the copyright to set
   */
  public void setCopyright(String copyright) {
          this.copyright = copyright;
  }

  /**
   * @return the copyright
   */
  public String getCopyright() {
          return copyright;
  }

  /**
   * @param warning the warning to set
   */
  public void setWarning(String warning) {
          this.warning = warning;
  }

  /**
   * @return the warning
   */
  public String getWarning() {
          return warning;
  }

  /**
   * @param country the country to set
   */
  public void setCountry(String country) {
          this.country = country;
  }

  /**
   * @return the country
   */
  public String getCountry() {
          return country;
  }

  /**
   * @param length the length to set
   */
  public void setLength(int length) {
          this.length = length;
  }

  /**
   * @return the length
   */
  public int getLength() {
          return length;
  }

  public String getTxtLength()
	{
		return txtLength;
	}

	public void setTxtLength(String txtLength)
	{
		this.txtLength = txtLength;
	}

	/**
   * @param durationSecond the durationSecond to set
   */
  public void setDurationSecond(final int durationSecond) {
          this.durationSecond = durationSecond;
  }

  /**
   * @return the durationSecond
   */
  public int getDurationSecond() {
          return durationSecond;
  }

  public String getTxtDuration()
	{
		return txtDuration;
	}

	public void setTxtDuration(String txtDuration)
	{
		this.txtDuration = txtDuration;
	}

	/**
   * @param polyline the polyline to set
   */
  public void setPolyline(String polyline) {
          this.polyline = polyline;
  }

  /**
   * @return the polyline
   */
  public String getPolyline() {
          return polyline;
  }

}