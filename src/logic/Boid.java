package logic;

import java.util.ArrayList;
import java.util.Random;

import logic.Vector2D;

public class Boid {
	
	private Vector2D position;
	private Vector2D velocity;
	private Vector2D force;
	private Settings settings;
	private int sizeRadius;
	
	private Random rng;
	
	//TODO: Max velocity variable and logic

	//Only settings arg gives random boid
	public Boid(Settings settings) {
		rng = new Random();
		
		this.settings = settings;
		this.position = new Vector2D(rng.nextDouble()*800, rng.nextDouble()*800);
		this.velocity = new Vector2D(rng.nextDouble()*10, rng.nextDouble()*10);
		this.force = new Vector2D(0, 0);
		this.sizeRadius = 5;
	}
	
	//Based on pseudocode from assignment
	public void calcSteer(ArrayList<Boid> boids, ArrayList<Obstacle> obstacles) {
			ArrayList<Boid> neighbours = getLocalBoids(boids);
			
			Vector2D sep = calcSepForce(neighbours, settings.getSepRadius()).mul(settings.getSeparationFactor());
			Vector2D align = calcAlignForce(neighbours).mul(settings.getAlignmentFactor());
			Vector2D coh = calcCohForce(neighbours).mul(settings.getCohesionFactor());
			
			this.force = sep;
			this.force = this.force.add(coh);
			this.force = this.force.add(align);
			this.force = this.force.limit(this.settings.getMaxForce());
			
			
			//pseudocode
			
			/*
			sep = separationWeight * calculateSeparationForce(neighbors)
			align = alignmentWeight * calculateAlignmentForce(neighbors)
			coh = cohesionWeight * calculateCohesionForce(neighbors)
			
			force = sep + align + coh
			velocity = velocity + force
			*/
		}
	
	public void updatePosition(ArrayList<Obstacle> obstacles) {
		wrap();
		
		this.velocity = this.velocity.add(this.force);
		this.velocity = this.velocity.limit(this.settings.getMaxSpeed());
		this.position = this.position.add(this.velocity);
		
	}
	
	//Helpers
	
	private Vector2D calcSepForce(ArrayList<Boid> localBoids, double sepRadius) {
		Vector2D res = new Vector2D();
		
		for (Boid b : localBoids) {
			if (inCircle(b, sepRadius)) {
				Vector2D avoid = this.position.sub(b.position).unit();
				double r = avoid.norm();
				try {
					res = res.add(avoid.unit().div(r));
					
				} catch (ArithmeticException e) {
					// div by 0
				}
				res.add(avoid);
			}
		}
		return res;
	}
	
	private Vector2D calcCohForce(ArrayList<Boid> localBoids) {
		Vector2D target = averagePosition(localBoids);
		Vector2D res = target.sub(this.position).unit();
		
		return res;
		
	}
	
	private Vector2D calcAlignForce(ArrayList<Boid> localBoids) {
		Vector2D res = averageVelocity(localBoids).unit();
		
		return res;
	}
	
	
	//Check if boid is within the local radius of focalBoid
	private boolean inCircle(Boid otherBoid, double radius) {
		
		//TODO: Shorten
		double center_x = this.position.getX();
		double center_y = this.position.getY();
		double x = otherBoid.position.getX();
		double y = otherBoid.position.getY();
		
		//Core of function		
	    double square_dist = Math.pow((center_x - x), 2) + Math.pow((center_y - y), 2);
	    
	    // <= vs <, if edge is included
	    return square_dist < Math.pow(radius, 2);
	}
	
	
	//Get list of local boids, ie boids within the local radius
	private ArrayList<Boid> getLocalBoids(ArrayList<Boid> allBoids) {
		ArrayList<Boid> res = new ArrayList<Boid>();
		for (Boid b : allBoids) {
			if (inCircle(b, this.settings.getViewRadius())) {
				res.add(b);
			}
		}
		return res;
		
	}
	
	
	private Vector2D averageVelocity(ArrayList<Boid> localBoids) {
		int size = localBoids.size();
		
		double x_res = 0;
		double y_res = 0;
		
		for (Boid b : localBoids) {
			x_res += b.velocity.getX();
			y_res += b.velocity.getY();
		}
		
		return new Vector2D((x_res / size), (y_res / size));
		
	}
	
	private Vector2D averagePosition(ArrayList<Boid> localBoids) {
		int size = localBoids.size();
		
		double x_res = 0;
		double y_res = 0;
		
		for (Boid b : localBoids) {
			x_res += b.position.getX();
			y_res += b.position.getY();
		}
		
		return new Vector2D((x_res / size), (y_res / size));	
	}
	
    private void wrap() {
        double x = this.position.getX();
        double y = this.position.getY();
      
        if (x < 0) {
            this.position.setX(this.settings.getAreaWidth() - x);
        }
        if (y < 0) {
            this.position.setY(this.settings.getAreaHeight() - y);
        }
        if (x > this.settings.getAreaWidth()) {
            this.position.setX(x - this.settings.getAreaWidth());
        }
        if (y > this.settings.getAreaHeight()) {
            this.position.setY(y - this.settings.getAreaHeight());
        }
    }
	
	public Vector2D getPosition() {
		return this.position;
	}
	
	public Vector2D getVelocity() {
		return this.velocity;
	}

	public Vector2D getForce() {
		return this.force;
	}

}
