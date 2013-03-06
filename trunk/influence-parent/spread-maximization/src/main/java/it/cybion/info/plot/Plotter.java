package it.cybion.info.plot;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

public class Plotter extends JFrame {
	
	private static final long serialVersionUID = 2580402542323574380L;

	public Plotter(Map<Integer,Float> points) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        DataTable data = new DataTable(Double.class, Double.class);
        double y;
        for (int x=0; x<points.size(); x++)
        {
        	y = new Double(points.get(x));
        	data.add(new Double(x), y);
        }
        XYPlot plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderer(data, lines);
        Color color = new Color(0.0f, 0.3f, 1.0f);
        plot.getPointRenderer(data).setSetting(PointRenderer.COLOR, color);
        plot.getLineRenderer(data).setSetting(LineRenderer.COLOR, color);
    }

    public static void drawPlot(Map<Integer,Float> points)
    {
    	Plotter frame = new Plotter(points);
        frame.setVisible(true);
    }
    
    public static void main(String[] args)
	{
    	Map<Integer,Float> points = new HashMap<Integer,Float>();
    	points.put(0, 0.32F);
    	points.put(1, 0.62F);
    	points.put(2, 0.92F);
    	points.put(3, 1.32F);
    	drawPlot(points);
	}
   
}
