//Author: Are Haartveit

package logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import logic.Obstacle;
import logic.Vector2D;

public class Boid {
	
	//Protected for subclass visibility, assuming no publication of code
	protected Vector2D position;
	protected Vector2D velocity;
	protected Vector2D force;
	protected Settings settings;
	private int sizeRadius;
	
	private Random rng;

	//Only settings arg gives random boid
	public Boid(Settings settings) {
		rng = new Random();
		
		this.settings = settings;
		this.position = new Vector2D(rng.nextDouble()*800, rng.nextDouble()*800);
		this.velocity = new Vector2D(rng.nextDouble()*10, rng.nextDouble()*10);
		this.force = new Vector2D();
		this.sizeRadius = 5;
	}
	
	//Based on pseudocode from assignment
	public void calcSteer(ArrayList<Boid> boids, ArrayList<Obstacle> obstacles, ArrayList<Predator> predators) {
			ArrayList<Boid> neighbours = getLocalBoids(boids, settings.getViewRadius());
			ArrayList<Predator> closePreds = getLocalPredators(predators, settings.getViewRadius());
			
			this.force = new Vector2D();
			
			Vector2D avoid = calcAvoidObstacleForce2(obstacles).mul(settings.getObstacleAvoidanceFactor());
			Vector2D sep = calcSepForce(neighbours, settings.getSepRadius()).mul(settings.getSeparationFactor());
			
			if (closePreds.size() > 0) {
				Vector2D flee = calcFleeForce(closePreds).mul(10);
				
				this.force = this.force.add(flee);
			}
			else {
				Vector2D align = calcAlignForce(neighbours).mul(settings.getAlignmentFactor());
				Vector2D coh = calcCohForce(neighbours).mul(settings.getCohesionFactor());
				
				
				this.force = this.force.add(coh);
				this.force = this.force.add(align);
				this.force = this.force.add(avoid);
			}
			
			this.force = this.force.add(sep);
			this.force = this.force.add(avoid);
			this.force = this.force.limit(this.settings.getMaxForce());
		}
	
	public void updatePosition(ArrayList<Obstacle> obstacles) {
		wrap();
		
		this.velocity = this.velocity.mul(0.9);
		
		Vector2D newVelocity = this.velocity.add(this.force).limit(this.settings.getMaxSpeed());
		this.velocity = newVelocity;
		
		obstacleCollision(obstacles);
		this.position = this.position.add(this.velocity);
		
	}
	
	//Handles collisions (moves boid outside object if hit), same method as obstacle overlap
	public void obstacleCollision(ArrayList<Obstacle> obstacles) {
		for (Obstacle o : obstacles) {
			Vector2D direction = this.getPosition().sub(o.getPosition());
			double dist = direction.norm();
			double diff = (o.getRadius() + this.getSizeRadius()) - dist;
			if (diff > 0) {
				this.force = this.force.add(direction.unit().mul(diff)).limit(settings.getMaxForce());
				this.position = this.position.add(direction.unit().mul(diff));
			}
		}
	}
	
	//Helpers
	
	private Vector2D calcSepForce(ArrayList<Boid> localBoids, double sepRadius) {
		Vector2D res = new Vector2D();
		
		for (Boid b : localBoids) {
			Vector2D avoid = this.position.sub(b.getPosition());
			double r = avoid.norm();
			if (r > 0.01) {
				res = res.add(avoid.unit().div(r));					
			}
		}
		return res;
	}
	
	protected Vector2D calcCohForce(ArrayList<Boid> localBoids) {
		Vector2D target = averagePosition(localBoids);
		Vector2D res = target.sub(this.position).unit();
		
		if (res.isZero()) {
			return new Vector2D();
		}
		
		return res;
		
	}
	
	private Vector2D calcAlignForce(ArrayList<Boid> localBoids) {
		Vector2D res = averageVelocity(localBoids).unit();
		
		if (res == this.velocity) {
			return new Vector2D();
		}
		
		return res;
	}
	
	private Vector2D calcFleeForce(ArrayList<Predator> closePreds) {
		Vector2D res = new Vector2D();
		
		for (Predator p : closePreds) {
				Vector2D avoid = this.position.sub(p.position).unit();
				res = res.add(avoid.unit());

				res.add(avoid);
		}
		return res;
		
	}
	
	//Simple obstacle avoidance, bit jumpy
	protected Vector2D calcAvoidObstacleForce1(ArrayList<Obstacle> obstacles) {
		if (obstacles.size() < 1) {
			return new Vector2D();
		}
		
		//Obstacle sight directly forward
		Vector2D sight = this.position.add(this.velocity.unit().mul(60));
		boolean ret = false;
		Obstacle closest = obstacles.get(0);
		double closestDist = this.getPosition().dist(obstacles.get(0).getPosition());
		
		//Find closest obstacle on collision course
		for (Obstacle o : obstacles) {
			double currentDist = this.getPosition().dist(o.getPosition());
			if (currentDist <= closestDist) {
				if (
					(sight.dist(o.getPosition()) < o.getRadius() + 3)
					|| 
					(sight.mul(0.5).dist(o.getPosition()) < o.getRadius() + 3)
					||
					(sight.mul(0.2).dist(o.getPosition()) < o.getRadius()+ 3)
					){
					ret = true;
					closest = o;
					closestDist = currentDist;
					}
				}
			}
		if (ret) {
			return sight.sub(closest.getPosition()).unit();
		}
		else {
			return new Vector2D();
		}
	}
	
	//nablaa version
    protected Vector2D calcAvoidObstacleForce2(ArrayList<Obstacle> obstacles) {
        Vector2D nearest = null; // the position of the nearest obstacle
        
        // new orthogonal basis
        Vector2D x = this.velocity.unit();
        Vector2D y = this.velocity.perpendicular().unit();
        
        synchronized (obstacles) {
            for (Obstacle o : obstacles) {
                Vector2D t = o.getPosition().sub(this.position).inOrthogonalBasis(x, y);
                
                // the obstacle is either behind the boid or too far away
                if (t.getX() <= 0
                        || t.getX() - o.getRadius() > this.settings.getAvoidanceDistance()
                        || Math.abs(t.getY()) > this.getSizeRadius() + o.getRadius()) {
                    continue;
                }
                
                if (nearest == null || t.getX() < nearest.getX()) {
                    nearest = t;
                }
            }
        }
        
        if (nearest != null) {
            return y.unit().mul(nearest.getY()).limit(-this.settings.getMaxForce() / 2.0);
        }
        
        return new Vector2D();
    }
	
	
	//Check if boid is within the local radius of focalBoid
	private boolean inCircle(Boid otherBoid, double radius) {

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
	protected ArrayList<Boid> getLocalBoids(ArrayList<Boid> allBoids, double viewRadius) {
		ArrayList<Boid> res = new ArrayList<Boid>();
		for (Boid b : allBoids) {
			if (inCircle(b, viewRadius)) {
				res.add(b);
			}
		}
		return res;
		
	}
	
	private ArrayList<Predator> getLocalPredators(ArrayList<Predator> allPredators, double viewRadius) {
		synchronized (allPredators) {
			ArrayList<Predator> res = new ArrayList<Predator>();
			for (Predator p : allPredators) {
				if (inCircle(p, viewRadius)) {
					res.add(p);
				}
			}
			return res;			
		}
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
	
	protected Vector2D averagePosition(ArrayList<Boid> localBoids) {
		int size = localBoids.size();
		
		double x_res = 0;
		double y_res = 0;
		
		for (Boid b : localBoids) {
			x_res += b.position.getX();
			y_res += b.position.getY();
		}
		if (size > 0) {
			return new Vector2D((x_res / size), (y_res / size));				
		}
		else {
			return new Vector2D();
		}
	}
	
    protected void wrap() {
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
	
	private int getSizeRadius() {
		return this.sizeRadius;
	}

}
