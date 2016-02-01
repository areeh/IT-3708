//Inspired by https://github.com/nablaa/boids-simulation

package logic;

public class Settings {
	
	private double maxSpeed;
	private double maxPredatorSpeed;
	private double maxForce;
	private double separationFactor;
	private double alignmentFactor;
	private double cohesionFactor;
	private double borderAvoidanceFactor;
	private double obstacleAvoidanceFactor;
	private int obstacleRadius;
	private boolean wrapArea;
	private int areaHeight;
	private int areaWidth;
	private double viewRadius;
	private double predViewRadius;
	private double sepRadius;
	private double chaseFactor;
	private double avoidanceDistance;
	
	public Settings() {
		this.maxSpeed = 1.5;
		this.maxPredatorSpeed = 3;
		this.maxForce = 0.5;
		this.separationFactor = 1;
		this.obstacleRadius = 15;
		this.sepRadius = 60;
		this.alignmentFactor = 0.5;
		this.cohesionFactor = 0.1;
		this.chaseFactor = 1;
		this.avoidanceDistance = 50;
		this.borderAvoidanceFactor = 1;
		this.obstacleAvoidanceFactor = 5;
		this.viewRadius = 60;
		this.predViewRadius = 70;
		this.wrapArea = true;
		this.setAreaHeight(800);
		this.setAreaWidth(800);
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public void setMaxSpeed(double speed) {
		this.maxSpeed = speed;
		
	}

	public double getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(double maxForce) {
		this.maxForce = maxForce;
	}

	public double getSeparationFactor() {
		return separationFactor;
	}

	public void setSeparationFactor(double separationFactor) {
		this.separationFactor = separationFactor;
	}

	public double getAlignmentFactor() {
		return alignmentFactor;
	}

	public void setAlignmentFactor(double alignmentFactor) {
		this.alignmentFactor = alignmentFactor;
	}

	public double getCohesionFactor() {
		return cohesionFactor;
	}

	public void setCohesionFactor(double cohesionFactor) {
		this.cohesionFactor = cohesionFactor;
	}
	
	public boolean isWrapArea() {
		return this.wrapArea;
	}
	
	public void setWrapArea(boolean wrap) {
		this.wrapArea = wrap;
	}

	public int getAreaHeight() {
		return areaHeight;
	}

	public void setAreaHeight(int areaHeight) {
		this.areaHeight = areaHeight;
	}

	public int getAreaWidth() {
		return areaWidth;
	}

	public void setAreaWidth(int areaWidth) {
		this.areaWidth = areaWidth;
	}

	public double getBorderAvoidanceFactor() {
		return borderAvoidanceFactor;
	}

	public void setBorderAvoidanceFactor(double borderAvoidanceFactor) {
		this.borderAvoidanceFactor = borderAvoidanceFactor;
	}

	public double getViewRadius() {
		return viewRadius;
	}

	public void setViewRadius(double viewRadius) {
		this.viewRadius = viewRadius;
	}

	public double getSepRadius() {
		return sepRadius;
	}
	
	public void setSepRadius(double sepRadius) {
		this.sepRadius = sepRadius;
	}

	public int getObstacleRadius() {
		return obstacleRadius;
	}

	public void setObstacleRadius(int obstacleRadius) {
		this.obstacleRadius = obstacleRadius;
	}

	public double getMaxPredatorSpeed() {
		return maxPredatorSpeed;
	}

	public void setMaxPredatorSpeed(double maxPredatorSpeed) {
		this.maxPredatorSpeed = maxPredatorSpeed;
	}

	public double getChaseFactor() {
		return chaseFactor;
	}

	public double getPredViewRadius() {
		return predViewRadius;
	}

	public void setPredViewRadius(double predViewRadius) {
		this.predViewRadius = predViewRadius;
	}

	public double getObstacleAvoidanceFactor() {
		return obstacleAvoidanceFactor;
	}

	public double getAvoidanceDistance() {
		return avoidanceDistance;
	}
	
}