package com.vaadin.charts;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.charts.client.ui.VPieChart;

@SuppressWarnings("serial")
@ClientWidget(VPieChart.class)
public class PieChart extends AbstractChart {

	private String borderColor = "#000";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.virtuallypreinstalled.hene.charts.AbstractChart#paintContent(com.
	 * vaadin.terminal.PaintTarget)
	 */
	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("bordercolor", borderColor);
	}

	public void setBorderColor(String borderColor) {
		if (borderColor == null) {
			borderColor = "#000";
		}
		this.borderColor = borderColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

}
