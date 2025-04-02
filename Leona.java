//Klass för att hålla koll på pennans tillstånd
class Leona {
    private double x;
    private double y;
    private double angle;
    boolean penDown;
    public Leona(double xValue, double yValue, double angleValue, boolean b) {
        this.penDown = b;
        this.x = xValue;
        this.y = yValue;
        this.angle = angleValue;
    }
    public boolean isPenDown() {
        return penDown;
    }
    public void setPenDown(boolean penDown) {
        this.penDown = penDown;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public void setAngle(double angle){
        this.angle = angle;
    }
    public double getAngle(){
        return this.angle;
    }
}