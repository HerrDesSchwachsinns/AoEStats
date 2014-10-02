package chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.entity.TitleEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ratio.Ratio;
import ratio.Ratios;
import storage.Game;

@SuppressWarnings("serial")
public class RatioChartPanel extends ChartPanel implements ChartMouseListener {
	public RatioChartPanel(List<String> players, List<Game> games, Ratio ratio,
			boolean smooth) {
		super(createChart(players, games, ratio, smooth));
		setPreferredSize(new Dimension(1280, 720));
		addChartMouseListener(this);
		plot = (XYPlot) this.getChart().getPlot();
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent e) {
		ChartEntity entity = e.getEntity();
		if (entity == null) return;
		XYPlot plot = (XYPlot) e.getChart().getPlot();
		int index;
		if (entity instanceof XYItemEntity) {
			index = ((XYItemEntity) entity).getSeriesIndex();
			toggleSeriesSelectionState(plot.getRenderer(), index);
		} else if (entity instanceof LegendItemEntity) {
			index = plot.getDataset()
					.indexOf(((LegendItemEntity) entity).getSeriesKey());
			toggleSeriesSelectionState(plot.getRenderer(), index);
		} else if (entity instanceof TitleEntity) {
			toggleUnselectedSeriesState();
		}
	}

	private static final Stroke STROKE = new BasicStroke(3f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private static final Stroke STROKE_SELECTED = new BasicStroke(5f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	private static Map<String, Paint> mapping = new HashMap<>();
	static {
		mapping.put("Joe", Color.red);
		mapping.put("Nesto", Color.blue);
		mapping.put("GrandJM", Color.gray);
		mapping.put("HDS", Color.green);
		mapping.put("Der Bayer", Color.magenta);
		mapping.put("MeiMuada", Color.magenta);
		mapping.put("Alex", Color.yellow);
	}

	private static JFreeChart createChart(List<String> players, List<Game> games, Ratio ratio, boolean smooth) {
		XYDataset dataset = createDataset(games, players, ratio);
		final JFreeChart chart = ChartFactory.createXYLineChart(Ratios
				.getLongName(ratio) + " Developement", // chart title
				"Games", // x axis label
				"Ratio", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		if (dataset.getSeriesCount() == 2) plot
				.setRenderer(new XYDifferenceRenderer());
		else if (smooth) plot.setRenderer(new XYSplineRenderer());
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.addRangeMarker(new ValueMarker(Ratios.getZeroMarkerValue(ratio),
				Color.black, new BasicStroke(2f)));
		plot.getDomainAxis(0)
				.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		XYItemRenderer renderer = plot.getRenderer();
		for (int i = 0; i < plot.getDataset().getSeriesCount(); ++i) {
			renderer.setSeriesStroke(i, new BasicStroke(3f,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			Object o = plot.getDataset().getSeriesKey(i);
			if (mapping.containsKey(o)) {
				renderer.setSeriesPaint(i, mapping.get(o));
			}
		}
		if (plot.getDataset().getSeriesCount() == 2) {
			((XYDifferenceRenderer) renderer).setPositivePaint(renderer
					.getSeriesPaint(0));
			((XYDifferenceRenderer) renderer).setNegativePaint(renderer
					.getSeriesPaint(1));
		}
		//		addPreferredColorsDO_CHANGE_THIS_METHOD(chart);//TODO

		return chart;

	}
	private static XYDataset createDataset(List<Game> games, List<String> players, Ratio ratio) {
		Map<String, List<Double>> ratios = ratio.getHistory(games, players);
		Map<String, XYSeries> series = new HashMap<>();
		players.forEach(p -> series.put(p, new XYSeries(p)));
		ratios.forEach((p, l) -> {
			XYSeries s = series.get(p);
			for (int i = 0; i < l.size(); ++i) {
				s.add(i, l.get(i));
			}
		});
		XYSeriesCollection dataset = new XYSeriesCollection();
		series.forEach((p, s) -> dataset.addSeries(s));
		return dataset;
	}

	private XYPlot plot;

	private void toggleSeriesSelectionState(XYItemRenderer renderer, int i) {
		BasicStroke stroke = (BasicStroke) renderer.getSeriesStroke(i);
		if (stroke.equals(STROKE)) renderer.setSeriesStroke(i, STROKE_SELECTED);
		else renderer.setSeriesStroke(i, STROKE);
	}
	private void toggleUnselectedSeriesState() {
		XYItemRenderer renderer = plot.getRenderer();
		for (int i = 0; i < plot.getSeriesCount(); ++i) {
			if (renderer.getSeriesStroke(i).equals(STROKE)) {
				Boolean b = renderer.getSeriesVisible(i);
				if (b == null) b = true;
				renderer.setSeriesVisible(i, !b);
			}
		}
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent arg0) {}

}
