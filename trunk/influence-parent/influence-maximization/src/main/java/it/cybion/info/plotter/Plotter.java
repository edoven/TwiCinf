package it.cybion.info.plotter;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Plotter extends JFrame {
	
	private static final long serialVersionUID = 2580402542323574380L;

	private Plotter(Map<Double,Double> points) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        DataTable data = new DataTable(Double.class, Double.class);
        List<Double> xList = new ArrayList<Double>(points.keySet());
        Collections.sort(xList);
        for (Double x : xList)
        	data.add(x, points.get(x));
        XYPlot plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderer(data, lines);
        Color color = new Color(0.0f, 0.3f, 1.0f);
        plot.getPointRenderer(data).setSetting(PointRenderer.COLOR, color);
        plot.getLineRenderer(data).setSetting(LineRenderer.COLOR, color);
    }

    public static void drawPlot(Map<Double,Double> points, String title)
    {
    	Plotter frame = new Plotter(points);
        frame.setVisible(true);
        frame.setTitle(title);
    }
    
    public static void main(String[] args)
	{
    	Map<Double,Double> points = new HashMap<Double,Double>();
    	points.put(0.0, 0.32);
    	points.put(1.0, 0.62);
    	points.put(2.0, 0.92);
    	points.put(3.0, 1.32);
    	drawPlot(points, "titolo");
	}
   
}
