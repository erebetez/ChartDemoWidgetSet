package com.vaadin.charts.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.animation.Animate;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VPieChart extends Composite implements Paintable {

	/** Set the tagname used to statically resolve widget from UIDL. */
	public static final String TAGNAME = "piechart";

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-" + TAGNAME;

	private static final double opacity = 1;

	private static final double selectedOpacity = 0.7;

	private static final double mouseOverOpacity = 0.8;

	/** Component identifier in UIDL communications. */
	String uidlId;

	/** Reference to the server connection object. */
	ApplicationConnection client;

	private boolean immediate = false;

	private boolean selectable = false;

	private DrawingArea canvas;

	private int width = 0;

	private int height = 0;

	List<PieInfo> pieInfos = new ArrayList<PieInfo>();

	private PieInfo selectedPie;

	private String borderColor;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VPieChart() {
		canvas = new DrawingArea(width, height);
		initWidget(canvas);
		setStyleName(CLASSNAME);
	}

	private class PieInfo {

		private double value;

		private int angle;

		private String color;

		private String key;

		private Path pie;

		public void setValue(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public void setAngle(int angle) {
			this.angle = angle;
		}

		public int getAngle() {
			return angle;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getColor() {
			return color;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public void setPie(Path pie) {
			this.pie = pie;
		}

		public Path getPie() {
			return pie;
		}
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		// This call should be made first. Ensure correct implementation,
		// and let the containing layout manage caption, etc.
		if (client.updateComponent(this, uidl, true)) {
			return;
		}

		// Save reference to server connection object to be able to send
		// user interaction later
		this.client = client;

		// Save the UIDL identifier for the component
		uidlId = uidl.getId();

		canvas.clear();
		pieInfos.clear();
		selectedPie = null;

		immediate = uidl.getBooleanAttribute("immediate");
		selectable = uidl.getBooleanAttribute("selectable");
		borderColor = uidl.getStringAttribute("bordercolor");

		canvas.setWidth(width);
		canvas.setHeight(height);
		int r = (width < height ? width : height) / 2 - 2;

		final Iterator<Object> options = uidl.getChildUIDL(0)
				.getChildIterator();
		final Iterator<Object> values = uidl.getChildUIDL(1).getChildIterator();
		double total = 0;
		while (options.hasNext()) {
			PieInfo info = new PieInfo();
			pieInfos.add(info);
			final UIDL optionUidl = (UIDL) options.next();
			final UIDL valueUidl = (UIDL) values.next();
			final String key = optionUidl.getStringAttribute("key");
			total += valueUidl.getDoubleVariable("value_" + key);
			info.setKey(key);
			info.setValue(valueUidl.getDoubleVariable("value_" + key));
			info.setColor(valueUidl.getStringAttribute("color"));
			// optionUidl.getStringAttribute("caption");
			if (selectable && optionUidl.hasAttribute("selected")) {
				selectedPie = info;
			}
		}
		int angleTot = 0;
		for (PieInfo info : pieInfos) {
			int angle = (int) (info.getValue() / total * 360);
			info.setAngle(angle);
			angleTot += angle;
		}

		int a = 0;
		for (PieInfo info : pieInfos) {
			if (angleTot < 360) {
				info.setAngle(info.getAngle() + 1);
				angleTot++;
			}

			final Path p = draw(width / 2, height / 2, r, a, info.getAngle());
			p.setStrokeColor(borderColor);
			p.setFillColor(info.getColor());
			info.setPie(p);
			a += info.getAngle();

			if (selectable) {
				if (selectedPie != null && selectedPie.equals(info)) {
					p.setFillOpacity(selectedOpacity);
				}
				p.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if (isSelected(p)) {
							selectedPie = null;
							p.setFillOpacity(opacity);
							updateSelectedPie(null);
						} else {
							if (selectedPie != null) {
								selectedPie.getPie().setFillOpacity(opacity);
							}
							for (PieInfo pi : pieInfos) {
								if (pi.getPie().equals(p)) {
									selectedPie = pi;
									break;
								}
							}
							p.setFillOpacity(selectedOpacity);
							updateSelectedPie(selectedPie.getKey());
						}
					};
				});
				p.addMouseOverHandler(new MouseOverHandler() {
					public void onMouseOver(MouseOverEvent event) {
						if (!isSelected(p)) {
							new Animate(p, "fillopacity", opacity,
									mouseOverOpacity, 300).start();
						}
					}
				});
				p.addMouseOutHandler(new MouseOutHandler() {
					public void onMouseOut(MouseOutEvent event) {
						if (!isSelected(p)) {
							new Animate(p, "fillopacity", mouseOverOpacity,
									opacity, 300).start();
						}
					}
				});
			}
		}
		int k = (int) (200 / Math.cos(Math.PI / 4));
		int s = k - r;

		Circle c1 = new Circle(width / 2, height / 2, r);
		c1.setFillColor(null);
		c1.setStrokeColor(borderColor);
		canvas.add(c1);

		Circle c2 = new Circle(width / 2, height / 2, k);
		c2.setFillColor(null);
		c2.setStrokeColor("white");
		c2.setStrokeWidth(s * 2 - 1);
		canvas.add(c2);
	}

	private boolean isSelected(Path p) {
		return selectedPie != null && selectedPie.getPie().equals(p);
	}

	private void updateSelectedPie(String key) {
		if (key == null) {
			key = "";
		}
		client.updateVariable(uidlId, "selected", new String[] { key },
				immediate);
	}

	private Path draw(int x, int y, int radius, double startAngle, double angle) {
		Path p = new Path(x, y);
		int x1 = (int) (x + (radius * Math.cos(startAngle * Math.PI / 180)));
		int y1 = (int) (y + (radius * Math.sin(startAngle * Math.PI / 180)));
		int x2 = (int) (x + (radius * Math.cos((startAngle + angle) * Math.PI
				/ 180)));
		int y2 = (int) (y + (radius * Math.sin((startAngle + angle) * Math.PI
				/ 180)));
		p.lineTo(x1, y1);
		double a = startAngle;
		double a2 = startAngle + angle;
		if (a <= 45 || a > 315) {
			p.lineTo(width, y1);
			if (a2 <= 45) {
				p.lineTo(width, y2);
				p.lineTo(x2, y2);
			} else if (a2 > 45 && a2 <= 135) {
				p.lineTo(width, height);
				p.lineTo(x2, height);
				p.lineTo(x2, y2);
			} else if (a2 > 135 && a2 <= 225) {
				p.lineTo(width, height);
				p.lineTo(0, height);
				p.lineTo(0, y2);
				p.lineTo(x2, y2);
			} else if (a2 > 225 && a2 <= 315) {
				p.lineTo(width, height);
				p.lineTo(0, height);
				p.lineTo(0, 0);
				p.lineTo(x2, 0);
				p.lineTo(x2, y2);
			} else if (a2 > 315) {
				p.lineTo(width, y2);
				p.lineTo(x2, y2);
			}
		} else if (a > 45 && a <= 135) {
			p.lineTo(x1, height);
			if (a2 > 45 && a > 315) {
				p.lineTo(x2, height);
				p.lineTo(x2, y2);
			} else if (a2 > 135 && a2 <= 225) {
				p.lineTo(0, height);
				p.lineTo(0, y2);
				p.lineTo(x2, y2);
			} else if (a2 > 225 && a2 <= 315) {
				p.lineTo(0, height);
				p.lineTo(0, 0);
				p.lineTo(x2, 0);
				p.lineTo(x2, y2);
			} else if (a2 > 315) {
				p.lineTo(0, height);
				p.lineTo(0, 0);
				p.lineTo(width, 0);
				p.lineTo(width, y2);
				p.lineTo(x2, y2);
			}
		} else if (a > 135 && a <= 225) {
			p.lineTo(0, y1);
			if (a2 > 135 && a2 <= 225) {
				p.lineTo(0, y2);
				p.lineTo(x2, y2);
			} else if (a2 > 225 && a2 <= 315) {
				p.lineTo(0, 0);
				p.lineTo(x2, 0);
				p.lineTo(x2, y2);
			} else if (a2 > 315) {
				p.lineTo(0, 0);
				p.lineTo(width, 0);
				p.lineTo(width, y2);
				p.lineTo(x2, y2);
			}
		} else if (a > 225 && a <= 315) {
			p.lineTo(x1, 0);
			if (a2 > 225 && a2 <= 315) {
				p.lineTo(x2, 0);
				p.lineTo(x2, y2);
			} else if (a2 > 315) {
				p.lineTo(width, 0);
				p.lineTo(width, y2);
				p.lineTo(x2, y2);
			}
		}

		p.close();
		canvas.add(p);
		return p;
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		if (width != null & width.endsWith("px")) {
			this.width = Integer.parseInt(width
					.substring(0, width.length() - 2));
		} else {
			this.width = 400;
		}
		ApplicationConnection.getConsole().log("" + this.width);
		ApplicationConnection.getConsole().log("setWidth: " + width);
	}

	@Override
	public void setHeight(String height) {
		super.setHeight(height);
		if (height != null & height.endsWith("px")) {
			this.height = Integer.parseInt(height.substring(0,
					height.length() - 2));
		} else {
			this.height = 400;
		}
	}

	protected DrawingArea getCanvas() {
		return canvas;
	}

}
