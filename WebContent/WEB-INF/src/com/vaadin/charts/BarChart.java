package com.vaadin.charts;

import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.charts.client.ui.VBarChart;

@ClientWidget(VBarChart.class)
public class BarChart extends AbstractChart {


	private static final long serialVersionUID = -3863509137029890612L;

	public enum Orientation {
		VERTICAL, HORIZONTAL
	}

	private Orientation orientation = Orientation.VERTICAL;

	private int animationDelay = 1500;

	private double gridMaxValue = 150;

	private double gridMinValue = 0;

	private int gridMarkLineCount = 2;


	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("gridmaxvalue", gridMaxValue);
		target.addAttribute("gridminvalue", gridMinValue);
		target.addAttribute("gridmarklinecount", gridMarkLineCount);
		target
				.addAttribute("orientation", orientation.toString()
						.toLowerCase());
		target.addAttribute("animationdelay", animationDelay);
	}

	@Override
	public void changeVariables(Object source, Map variables) {
		super.changeVariables(source, variables);
		for (Object itemId : getItemIds()) {
			String variableName = "value_" + itemIdMapper.key(itemId);
			if (variables.containsKey(variableName)) {
				final Object value = variables.get(variableName);
				final Double newValue = new Double(value.toString());

				getContainerDataSource().getItem(itemId).getItemProperty(
						"value").setValue(newValue);
			}
		}
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		requestRepaint();
	}

	public Orientation getOrientation(Orientation orientation) {
		return this.orientation;
	}

	public void setAnimationDelay(int animationDelay) {
		this.animationDelay = animationDelay;
		requestRepaint();
	}

	public int getAnimationDelay() {
		return animationDelay;
	}

	public void setGridMaxValue(double gridMaxValue) {
		this.gridMaxValue = gridMaxValue;
		requestRepaint();
	}

	public double getGridMaxValue() {
		return gridMaxValue;
	}

	public void setGridMinValue(double gridMinValue) {
		this.gridMinValue = gridMinValue;
		requestRepaint();
	}

	public double getGridMinValue() {
		return gridMinValue;
	}

	public void setGridMarkLineCount(int gridMarkLineCount) {
		this.gridMarkLineCount = gridMarkLineCount;
		requestRepaint();
	}

	public int getGridMarkLineCount() {
		return gridMarkLineCount;
	}

}
