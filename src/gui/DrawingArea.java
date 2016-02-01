//Based on  https://github.com/nablaa/boids-simulation


package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import logic.Boid;
import logic.Predator;
import logic.Obstacle;
import logic.Simulation;
import logic.Vector2D;

/**
 * This class draws the simulation.
 */
public class DrawingArea extends JPanel implements Runnable {
    private Simulation sim;
    private long sleepTime;
    private double boidSize;
    private double boidPredatorSize;
    private Color boidColor;
    private Color boidPredatorColor;
    private Color boidEdgeColor;
    private Color boidSteeringColor;
    private Color boidVelocityColor;
    private Color sightColor;
    private Color obstacleColor;
    private int width;
    private int height;
    private boolean stopped;
    private boolean showControlVector;
    private boolean showBoidSight;
    private boolean showBoidVelocity;
    private boolean antiAliasing;
    private Graphics2D gbuffer;
    private BufferedImage buffer;
    
    /**
     * Creates a new drawing area with the given size.
     * 
     * @param simulation simulation object
     * @param w width of the area
     * @param h height of the area
     */
    public DrawingArea(final Simulation simulation, int w, int h) {
        super();
        this.sim = simulation;
        this.sleepTime = 10;
        this.boidSize = 10;
        this.boidPredatorSize = 15;
        this.boidColor = Color.BLUE;
        this.boidPredatorColor = Color.RED;
        this.boidEdgeColor = Color.BLACK;
        this.boidSteeringColor = Color.DARK_GRAY;
        this.boidVelocityColor = Color.RED;
        this.sightColor = Color.GRAY;
        this.obstacleColor = Color.GRAY;
        this.width = w;
        this.height = h;
        this.stopped = false;
        this.showControlVector = false;
        this.showBoidSight = false;
        this.showBoidVelocity = false;
        this.antiAliasing = true;
        
        // create a buffer to allow double buffering
        this.buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        this.gbuffer = (Graphics2D) this.buffer.getGraphics();
        
    }

    /**
     * Runs the thread. Updates the simulation state and draws everything.
     * If the simulation is stopped, the simulation state won't be updated.
     */
    public void run() {
        while (true) {
            if (!this.stopped) {
                synchronized (this.sim.getBoids()) {
                    this.sim.updateBoids();
                }
                synchronized (this.sim.getPredators()) {
                	this.sim.updatePredators();
                }
            }
            
            this.draw();
            
            try {
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * Draws the simulation state. Draws all obstacles and boids.
     * Draws the dragging hints if the mouse is dragged.
     */
    private void draw() {
        int w = this.getSize().width;
        int h = this.getSize().height;

        // set anti-aliasing
        if (this.antiAliasing) {
            gbuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            gbuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        
        // clear the buffer
        gbuffer.setBackground(Color.WHITE);
        gbuffer.clearRect(0, 0, w, h);
        
        synchronized (this.sim.getBoids()) {
            this.drawBoids(gbuffer);
        }
        
        synchronized (this.sim.getPredators()) {
        	this.drawPredators(gbuffer);
        }
        
        synchronized (this.sim.getObstacles()) {
            this.drawObstacles(gbuffer);
        }
        
        
        // draw the back buffer on screen
        this.getGraphics().drawImage(buffer, 0, 0, w, h, 0, 0, w, h, null);
    }
    
    /**
     * Draws a circle with radius text.
     * 
     * @param g graphics
     */

    /**
     * Draws the boids. Draws also their sights and steering vectors if they are
     * enabled.
     * 
     * @param g graphics
     */
    private void drawBoids(Graphics2D g) {
        for (Boid b : this.sim.getBoids()) {
            
        	if (this.showBoidSight) {
                this.drawBoidSight(b, g, this.sim.getSettings().getViewRadius());
            }
        	
            if (this.showControlVector) {
                this.drawBoidSteering(b, g);
            }
            if (this.showBoidVelocity) {
                this.drawBoidVelocity(b, g);
            }
            this.drawBoid(b, g, this.boidColor, this.boidSize);
        }
    }
    
    private void drawPredators(Graphics2D g) {
        for (Predator p : this.sim.getPredators()) {
        	if (this.showBoidSight) {
        		this.drawBoidSight(p, g, this.sim.getSettings().getPredViewRadius());
        	}
        	if (this.showControlVector) {
        		this.drawBoidSteering(p, g);
        	}
        	if (this.showBoidVelocity) {
        		this.drawBoidVelocity(p, g);
        	}
        	this.drawBoid(p, g, this.boidPredatorColor, this.boidPredatorSize);
        }
    }
    
    /**
     * Draw the velocity vector of a boid.
     * @param b boid
     * @param g graphics
     */
    private void drawBoidVelocity(Boid b, Graphics2D g) {
        Vector2D p = b.getPosition();
        Vector2D v = b.getVelocity().mul(10); // times 10 to make the line long enough to see it
        g.setColor(this.boidVelocityColor);
        g.drawLine((int)p.getX(), (int)p.getY(), (int)(p.getX() + v.getX()), (int)(p.getY() + v.getY()));
    }

    /**
     * Draws the obstacles.
     * 
     * @param g graphics
     */
    private void drawObstacles(Graphics2D g) {
        g.setColor(this.obstacleColor);
        synchronized (this.sim.getObstacles()) {
            for (Obstacle o : this.sim.getObstacles()) {
                g.drawOval((int)(o.getPosition().getX() - o.getRadius()), (int)(o.getPosition().getY() - o.getRadius()), 2 * o.getRadius(), 2 * o.getRadius());
            }
        }
    }
    
    /**
     * Stops updating the simulation state. Does not stop the drawing.
     */
    public void stop() {
        this.stopped = true;
    }
    
    /**
     * Is the simulation stopped.
     * @return true if the simulation is stopped, false otherwise
     */
    public boolean isStopped() {
        return this.stopped;
    }
    
    /**
     * Starts the simulation.
     */
    public synchronized void start() {
        this.stopped = false;
    }

    /**
     * Draws a boid. The boid is represented by a triangle.
     * 
     * @param b boid to draw
     * @param g graphics
     */
    private void drawBoid(Boid b, Graphics2D g, Color boidColor, double boidSize) {
        g.setColor(boidColor);
        Vector2D t = b.getVelocity().unit().mul(boidSize); // front vector
        
        // if the boid is stopped, draw the boid facing right
        if (t.isZero()) {
            t = new Vector2D(boidSize, 0);
        }
        
        Vector2D r = t.perpendicular().unit().mul(boidSize / 2.5); // left vector
        Vector2D s = r.mul(-1); // right vector
        Vector2D p1 = b.getPosition().add(r); // right point
        Vector2D p2 = b.getPosition().add(s); // left point
        Vector2D p3 = b.getPosition().add(t); // front point
        
        // draw a polygon
        Polygon poly = new Polygon();
        poly.addPoint((int)p1.getX(), (int)p1.getY());
        poly.addPoint((int)p2.getX(), (int)p2.getY());
        poly.addPoint((int)p3.getX(), (int)p3.getY());
        g.fillPolygon(poly);
        
        // draw edges
        g.setColor(this.boidEdgeColor);
        g.drawPolygon(poly);
    }
    
    /**
     * Draws the boid sight.
     * 
     * @param b boid
     * @param g graphics
     */
    
    private void drawBoidSight(Boid b, Graphics2D g, double viewRadius) {
        g.setColor(this.sightColor);
        
        // calculate bounding box for the view distance circle
        int x0 = (int)(b.getPosition().getX() - viewRadius);
        int y0 = (int)(b.getPosition().getY() - viewRadius);
        int w = (int)(viewRadius) * 2;
        int h = (int)(viewRadius) * 2;
        g.drawOval(x0, y0, w, h);
    }
    
    /**
     * Draws the boid steering force.
     * 
     * @param b boid
     * @param g graphics
     */
    private void drawBoidSteering(Boid b, Graphics2D g) {
        g.setColor(this.boidSteeringColor);
        Vector2D end = b.getPosition().add(b.getForce().mul(30));
        g.drawLine((int)b.getPosition().getX(), (int)b.getPosition().getY(), (int)end.getX(), (int)end.getY());
    }
    
    @Override
    /**
     * Gets the preferred size.
     * 
     * @return preferred size
     */
    public Dimension getPreferredSize() {
        return new Dimension(this.width, this.height);
    }
    
    @Override
    /**
     * Gets the minimum size.
     * 
     * @return minimum size
     */
    public Dimension getMinimumSize() {
        return new Dimension(this.width, this.height);
    }
    
    @Override
    /**
     * Gets the maximum size.
     * 
     * @return maximum size
     */
    public Dimension getMaximumSize() {
        return new Dimension(this.width, this.height);
    }
        
    /**
     * Gets the thread sleep time.
     * 
     * @return thread sleep time
     */
    public long getSleepTime() {
        return this.sleepTime;
    }
    
    /**
     * Sets the thread sleep time.
     * 
     * @param time thread sleep time
     */
    public void setSleepTime(long time) {
        this.sleepTime = time;
    }

    /**
     * Should we draw the steering vectors.
     * 
     * @param showControlVector true draws the vectors, false doesn't
     */
    public void setShowControlVector(boolean showControlVector) {
        this.showControlVector = showControlVector;
    }

    /**
     * Are we drawing the steering vectors.
     * 
     * @return true if the vectors are drawn, false otherwise
     */
    public boolean isShowControlVector() {
        return showControlVector;
    }
    
    /**
     * Should we draw the velocity vectors.
     * @param show true draws the vectors, false doesn't
     */
    public void setShowVelocityVector(boolean show) {
        this.showBoidVelocity = show;
    }
    
    /**
     * Are we drawing the velocity vectors.
     * @return true if the vectors are drawn, false otherwise
     */
    public boolean isShowVelocityVector() {
        return this.showBoidVelocity;
    }

    /**
     * Should we draw the boid sight.
     * 
     * @param showBoidSight true draws the sight, false doesn't
     */
    public void setShowBoidSight(boolean showBoidSight) {
        this.showBoidSight = showBoidSight;
    }

    /**
     * Are we drawing the boid sight.
     * 
     * @return true if the sight is drawn, false otherwise
     */
    public boolean isShowBoidSight() {
        return showBoidSight;
    }

    /**
     * Sets the simulation. Must be called after the simulation is loaded from a
     * file.
     * 
     * @param sim simulation
     */
    
    /*
    public void setSim(Simulation sim) {
        this.sim = sim;
        this.mouseDragger.setSimulation(sim);
    }
    */
    
    /**
     * Set anti-aliasing on/off.
     * 
     * @param value anti-aliasing
     */
    public void setAntiAliasing(boolean value) {
        this.antiAliasing = value;
    }
    
    /**
     * Is the anti-aliasing enabled.
     * 
     * @return true is anti-aliasing is enabled, false otherwise
     */
    public boolean isAntiAliasing() {
        return this.antiAliasing;
    }

}
