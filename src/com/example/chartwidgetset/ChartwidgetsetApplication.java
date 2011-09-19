package com.example.chartwidgetset;

import com.vaadin.Application;
import com.vaadin.charts.*;
import com.vaadin.charts.BarChart.Orientation;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ChartwidgetsetApplication extends Application {

	private static final long serialVersionUID = 1L;

	private static final String[] colors1 = new String[] { "#ee7c08",
			"#00b4f0", "#000000", "#40B527", "#ffffff", "#d8d2c6" };

	private static final String[] colors2 = new String[] { "red", "green",
			"black", "blue", "yellow", "white" };

	TabSheet tabSheet = new TabSheet();

	private Container browserMarketShareCont = new IndexedContainer();

	@Override
	public void init() {
		Window mainWindow = new Window("Charts Application");
		setMainWindow(mainWindow);
		mainWindow.addComponent(tabSheet);

		valuateBrowserMarketShareCont();
		tabSheet.addTab(new BarChartTab(), "Bar chart", null);
		tabSheet.addTab(new PieChartTab(), "Pie chart", null);
		tabSheet.addTab(new DonutChartTab(), "Donut chart", null);
	}

	private void valuateBrowserMarketShareCont() {
		browserMarketShareCont
				.addContainerProperty("value", Double.class, null);
		browserMarketShareCont.addContainerProperty("caption", String.class,
				null);

		Object itemId = browserMarketShareCont.addItem();
		browserMarketShareCont.getContainerProperty(itemId, "value").setValue(
				66.97);
		browserMarketShareCont.getContainerProperty(itemId, "caption")
				.setValue("Internet Explorer");

		itemId = browserMarketShareCont.addItem();
		browserMarketShareCont.getContainerProperty(itemId, "value").setValue(
				22.98);
		browserMarketShareCont.getContainerProperty(itemId, "caption")
				.setValue("Firefox");

		itemId = browserMarketShareCont.addItem();
		browserMarketShareCont.getContainerProperty(itemId, "value").setValue(
				4.07);
		browserMarketShareCont.getContainerProperty(itemId, "caption")
				.setValue("Safari");

		itemId = browserMarketShareCont.addItem();
		browserMarketShareCont.getContainerProperty(itemId, "value").setValue(
				2.84);
		browserMarketShareCont.getContainerProperty(itemId, "caption")
				.setValue("Chrome");

		itemId = browserMarketShareCont.addItem();
		browserMarketShareCont.getContainerProperty(itemId, "value").setValue(
				2.04);
		browserMarketShareCont.getContainerProperty(itemId, "caption")
				.setValue("Opera");

		itemId = browserMarketShareCont.addItem();
		browserMarketShareCont.getContainerProperty(itemId, "value").setValue(
				1.10);
		browserMarketShareCont.getContainerProperty(itemId, "caption")
				.setValue("Other");

	}

	private class BarChartTab extends VerticalLayout {

		private BarChart barChart = new BarChart();

		private Container cont1 = new IndexedContainer();

		private Container cont2 = new IndexedContainer();

		private VerticalLayout propertiesLayout = new VerticalLayout();

		private HorizontalLayout textFieldsLayout = new HorizontalLayout();

		public BarChartTab() {
			setCaption("Bar chart");
			addComponent(barChart);
			Button toggleButton = new Button("Show properties", false);
			toggleButton.setImmediate(true);
			toggleButton.addListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					propertiesLayout.setVisible(!propertiesLayout.isVisible());
				}
			});
			addComponent(toggleButton);

			propertiesLayout.addComponent(constructOrientationButton());
			propertiesLayout.addComponent(constructAnimationDelayTextField());
			propertiesLayout.addComponent(constructContainerSelect());
			propertiesLayout.addComponent(textFieldsLayout);
			propertiesLayout.addComponent(constructInfoLabel());
			propertiesLayout.setVisible(false);
			addComponent(propertiesLayout);

			valuateCont1();
			valuateCont2();
			barChart.setImmediate(true);
			barChart.setContainerDataSource(cont1);
			barChart.setItemCaptionPropertyId("caption");
			barChart.setItemValuePropertyId("value");
			barChart.setColors(colors1);
			fillTextFieldsLayout();
		}

		private Button constructOrientationButton() {
			Button b = new Button("Vertical", true);
			b.setImmediate(true);
			b.addListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					if ((Boolean) event.getButton().getValue()) {
						barChart.setOrientation(Orientation.VERTICAL);
					} else {
						barChart.setOrientation(Orientation.HORIZONTAL);
					}
				}
			});
			return b;
		}

		private Component constructInfoLabel() {
			final Label l = new Label();
			barChart.addListener(new ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					Object value = barChart.getValue();
					if (value != null) {
						l.setValue(barChart.getContainerDataSource().getItem(
								barChart.getValue()).getItemProperty("info"));
					} else {
						l.setValue("");
					}

				}

			});
			return l;
		}

		private TextField constructAnimationDelayTextField() {
			final TextField tf = new TextField("Animation");
			tf.setValue(1500);
			tf.setImmediate(true);
			tf.addListener(new ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					Object value = tf.getValue();
					try {
						barChart.setAnimationDelay(Integer
								.parseInt((String) value));
					} catch (NumberFormatException e) {
					}

				}

			});
			return tf;
		}

		private void fillTextFieldsLayout() {
			textFieldsLayout.removeAllComponents();
			for (Object o : barChart.getContainerDataSource().getItemIds()) {
				TextField tf = new TextField();
				tf.setPropertyDataSource(barChart.getContainerDataSource()
						.getItem(o).getItemProperty(
								barChart.getItemValuePropertyId()));
				tf.setReadThrough(true);
				tf.setReadOnly(true);
				textFieldsLayout.addComponent(tf);
			}
		}

		private Component constructContainerSelect() {
			final NativeSelect s = new NativeSelect("Container");
			s.setNullSelectionAllowed(false);
			s.addItem(cont1);
			s.setItemCaption(cont1, "cont1");
			s.setValue(cont1);
			s.addItem(cont2);
			s.setItemCaption(cont2, "cont2");
			s.setImmediate(true);
			s.addListener(new ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					Object value = s.getValue();
					barChart.setContainerDataSource((Container) s.getValue());
					if (cont1 == value) {
						barChart.setGridMaxValue(150);
						barChart.setGridMinValue(0);
						barChart.setGridMarkLineCount(2);
					} else if (cont2 == value) {
						barChart.setGridMaxValue(500);
						barChart.setGridMinValue(0);
						barChart.setGridMarkLineCount(4);
					}
					barChart.setValue(null);
					fillTextFieldsLayout();
				}

			});

			return s;
		}

		private void valuateCont1() {
			cont1.addContainerProperty("value", Double.class, null);
			cont1.addContainerProperty("caption", String.class, null);
			cont1.addContainerProperty("info", String.class, null);

			Object itemId = cont1.addItem();
			cont1.getContainerProperty(itemId, "value").setValue(50);
			cont1.getContainerProperty(itemId, "caption").setValue("A");
			cont1.getContainerProperty(itemId, "info").setValue("A info text");

			itemId = cont1.addItem();
			cont1.getContainerProperty(itemId, "value").setValue(100);
			cont1.getContainerProperty(itemId, "caption").setValue("B");
			cont1.getContainerProperty(itemId, "info").setValue("B info text");

			itemId = cont1.addItem();
			cont1.getContainerProperty(itemId, "value").setValue(75);
			cont1.getContainerProperty(itemId, "caption").setValue("C");
			cont1.getContainerProperty(itemId, "info").setValue("C info text.");

			itemId = cont1.addItem();
			cont1.getContainerProperty(itemId, "value").setValue(125);
			cont1.getContainerProperty(itemId, "caption").setValue("D");
			cont1.getContainerProperty(itemId, "info").setValue("D info text.");

		}

		private void valuateCont2() {
			cont2.addContainerProperty("value", Double.class, null);
			cont2.addContainerProperty("caption", String.class, null);
			cont2.addContainerProperty("info", String.class, null);

			Object itemId = cont2.addItem();
			cont2.getContainerProperty(itemId, "value").setValue(75);
			cont2.getContainerProperty(itemId, "caption").setValue("1");
			cont2.getContainerProperty(itemId, "info").setValue("1 info");

			itemId = cont2.addItem();
			cont2.getContainerProperty(itemId, "value").setValue(500);
			cont2.getContainerProperty(itemId, "caption").setValue("2");
			cont2.getContainerProperty(itemId, "info").setValue("2 info");

			itemId = cont2.addItem();
			cont2.getContainerProperty(itemId, "value").setValue(222);
			cont2.getContainerProperty(itemId, "caption").setValue("3");
			cont2.getContainerProperty(itemId, "info").setValue("3 info");

			itemId = cont2.addItem();
			cont2.getContainerProperty(itemId, "value").setValue(367);
			cont2.getContainerProperty(itemId, "caption").setValue("4");
			cont2.getContainerProperty(itemId, "info").setValue("4 info");

			itemId = cont2.addItem();
			cont2.getContainerProperty(itemId, "value").setValue(25);
			cont2.getContainerProperty(itemId, "caption").setValue("5");
			cont2.getContainerProperty(itemId, "info").setValue("5 info");

		}
	}

	private class DonutChartTab extends VerticalLayout {
		public DonutChartTab() {
			DonutChart c = new DonutChart();
			c.setWidth("200px");
			c.setHeight("200px");
			c.setColors(colors2);
			c.setItemValuePropertyId("value");
			c.setItemCaptionPropertyId("caption");
			c.setContainerDataSource(browserMarketShareCont);
			addComponent(c);
		}
	}

	private class PieChartTab extends VerticalLayout {

		public PieChartTab() {
			final PieChart c = new PieChart();
			c.setWidth("300px");
			c.setHeight("300px");
			c.setSelectable(true);
			c.setColors(colors1);
			c.setItemValuePropertyId("value");
			c.setItemCaptionPropertyId("caption");
			c.setContainerDataSource(browserMarketShareCont);
			c.setImmediate(true);
			c.addListener(new ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					if (c.getValue() == null) {
						return;
					}

					String caption = (String) browserMarketShareCont
							.getContainerProperty(c.getValue(), "caption")
							.getValue();
					String value = browserMarketShareCont.getContainerProperty(
							c.getValue(), "value").getValue()
							+ "%";
					getMainWindow().showNotification(
							"Selected: " + caption + " " + value);
				}
			});
			addComponent(c);
		}
	}

}
