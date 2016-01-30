package logic;

public class Settings {
	
	private double maxSpeed;
	private double maxForce;
	private double separationFactor;
	private double alignmentFactor;
	private double cohesionFactor;
	private double borderAvoidanceFactor;
	private int obstacleRadius;
	private boolean wrapArea;
	private int areaHeight;
	private int areaWidth;
	private double viewRadius;
	private double sepRadius;
	
	public Settings() {
		this.maxSpeed = 4;
		this.maxForce = 2;
		this.separationFactor = 1;
		this.sepRadius = 10;
		this.alignmentFactor = 1;
		this.cohesionFactor = 0.01;
		this.borderAvoidanceFactor = 1;
		this.setViewRadius(25);
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
	
}