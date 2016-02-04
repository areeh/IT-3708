//Author: Are Haartveit

package logic;

import java.util.ArrayList;
import java.util.Random;

public class Obstacle {
	
	private Vector2D position;
	private int radius;
	private Random rng;

	//Round obstacle in a random position
	public Obstacle(Settings settings) {
		rng = new Random();
		
		//Limit position to simulation size		
		double x = settings.getObstacleRadius() + (800-settings.getObstacleRadius() -settings.getObstacleRadius())*rng.nextDouble();
		double y = settings.getObstacleRadius() + (800-settings.getObstacleRadius() -settings.getObstacleRadius())*rng.nextDouble();
		
		this.position = new Vector2D(x, y);
		this.radius = Math.round(settings.getObstacleRadius() + (settings.getObstacleRadius()-5 - settings.getObstacleRadius())*rng.nextFloat());
		
		System.out.println(this.radius);
	}
	
	public boolean anyOverlap(ArrayList<Obstacle> obstacles) {
		for (Obstacle o : obstacles) {
			if (this.isOverlap(o)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOverlap(Obstacle other) {
		if (this.position.dist(other.getPosition()) < this.radius + other.getRadius() + 16) {
			return true;
		}
		return false;
	}
	
	public synchronized Vector2D getPosition() {
		return this.position;
	}
	
	public synchronized int getRadius() {
		return this.radius;
	}

}
