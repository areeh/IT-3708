//Author: Are Haartveit

package logic;

import java.util.ArrayList;

public class Predator extends Boid {

	public Predator(Settings settings) {
		super(settings);
		
	}
	
	public void addRandomPredator() {
		
	}
	
	public void calcPredSteer(ArrayList<Boid> boids, ArrayList<Obstacle> obstacles, ArrayList<Predator> predators) {
		ArrayList<Boid> neighbours = getLocalBoids(boids, settings.getPredViewRadius());
		
		
		Vector2D chase = calcChaseForce(neighbours).mul(settings.getChaseFactor());
		Vector2D avoid = calcAvoidObstacleForce3(obstacles);
		Vector2D res = chase.add(avoid);
		
		this.force = res.limit(this.settings.getMaxForce());
		
	}
	
	public Vector2D calcChaseForce(ArrayList<Boid> boids) {
		
		if (boids.size() < 1) {
			return new Vector2D();
		}
		
		double minDistance;
		Vector2D minVector;
		boolean ret = false;
		
		minVector = boids.get(0).getPosition().sub(this.position);
		minDistance = minVector.norm();
		
		for (Boid b : boids) {
			Vector2D vector = b.getPosition().sub(this.position);
			double distance = vector.norm();
			
			if (distance < minDistance) {
				minDistance = distance;
				minVector = vector;
				ret = true;
			}
			
		}
		
		if (ret) {
			Vector2D steering = minVector.sub(this.velocity).unit();
			return steering;
		}
		else {
			return new Vector2D();
		}
		
	}
	
	@Override
	public void updatePosition(ArrayList<Obstacle> obstacles) {
		wrap();
		
		
		this.velocity = this.velocity.mul(0.99);
		
		
		this.velocity = this.velocity.add(this.force).limit(this.settings.getMaxPredatorSpeed());
		obstacleCollision(obstacles);
		this.position = this.position.add(this.velocity);
		
	}

}
