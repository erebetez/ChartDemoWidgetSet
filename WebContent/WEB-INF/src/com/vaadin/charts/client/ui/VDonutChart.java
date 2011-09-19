package com.vaadin.charts.client.ui;

import org.vaadin.gwtgraphics.client.shape.Circle;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDonutChart extends VPieChart {

	/** Set the tagname used to statically resolve widget from UIDL. */
	public static final String TAGNAME = "donutchart";

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-" + TAGNAME;

	public VDonutChart() {
		setStyleName(CLASSNAME);
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		int w = getCanvas().getWidth();
		int h = getCanvas().getHeight();
		int r = w < h ? w : h;
		Circle c = new Circle(w / 2, h / 2, r / 3);
		getCanvas().add(c);
	}
}
