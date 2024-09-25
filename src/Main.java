import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener, Runnable {

	int res = 1;
	double l = -1, r = 1, t = -1, b = 1;
	Polynomial poly;
	int precision = 10;
	
	Point start = null;
	C[] zeros = { new C(2, 3), new C(-1, 2), new C(1.5, -0.3), new C(0, 0) };
	Color[] colors = new Color[zeros.length];
	double lStart, rStart, tStart, bStart;
	
	static BufferedImage img = null;
	
	public Main() {
		poly = new Polynomial(new C[] { new C(-3.1415, 2.71828), new C(1.414, -1.6180339), new C(0, 0), new C(3.1415, 2.71828) });
		for(int i = 0; i < colors.length; i++) {
			colors[i] = new Color(Color.HSBtoRGB((float) Math.random(), (float) Math.random(), 1));
		}
		
		setTitle(poly.toString());
		setSize(700, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
		Thread t = new Thread(this);
		t.start();
		update();
	}

	public static void main(String[] args) {
		new Main();
	}
	
	@Override
	public void paint(Graphics g0) {
		g0.drawImage(img, 0, 0, null);
	}
	
	private void getImage() {
		Polynomial derivative = poly.getDerivative();
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
		
		for(int x = 0; x < getWidth() / res; x++) {
			for(int y = 0; y < getHeight() / res; y++) {
				double real = map(x, 0, getWidth() / res, l, r);
				double imaginary = map(y, 0, getHeight() / res, t, b);
				C z = new C(real, imaginary);
				for(int i = 0; i < precision; i++) {
					z = z.minus(poly.evaluate(z).div(derivative.evaluate(z)));
				}
				int closest = 0;
				double dist = Double.MAX_VALUE;
				for(int i = 1; i < zeros.length; i++) {
					if(z.diff(zeros[i]) < dist) closest = i;
				}
				g.setColor(colors[closest]);
				g.fillRect(x * res, y * res, res, res);
			}
		}
		Main.img = img;
	}
	
	private double map(double x, double min, double max, double l, double r) {
		return (x - min) / (max - min) * (r - l) + l;
	}
	
	private Color toColor(C c) {
		return new Color((int) c.real * 100 % 256, (int) c.imaginary * 100 % 256, 127);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		start = e.getPoint();
		lStart = l;
		rStart = r;
		tStart = t;
		bStart = b;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		start = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		double w = r - l;
		double h = b - t;
		l = lStart - (e.getX() - start.getX()) * w / 1000;
		r = rStart - (e.getX() - start.getX()) * w / 1000;
		t = tStart - (e.getY() - start.getY()) * h / 1000;
		b = bStart - (e.getY() - start.getY()) * h / 1000;
		update();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double d = e.getPreciseWheelRotation() / 10;
		double m = (l + r) / 2;
		l = map(d, 0, 1, l, m);
		r = map(1 - d, 0, 1, m, r);
		double c = (t + b) / 2;
		t = map(d, 0, 1, t, c);
		b = map(1 - d, 0, 1, c, b);
		update();
	}
	
	boolean update = false;
	long lastUpdate = System.currentTimeMillis();
	private void update() {
		if(System.currentTimeMillis() - lastUpdate < 33) return;
		precision = 10;
		getImage();
		repaint();
		update = true;
		lastUpdate = System.currentTimeMillis();
	}

	@Override
	public void run() {
		int finishedPrecision = precision;
		while(true) {
			if(update) {
				finishedPrecision = precision;
				update = false;
			}
			if(precision == finishedPrecision) {
				precision++;
				update = false;
				getImage();
				repaint();
				finishedPrecision++;
			}
			try {
				Thread.sleep(5);
			} catch(Exception e) {}
		}
	}
	
}
