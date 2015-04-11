package fyoprojekt;

/**
 * A basic doubleing point vector.
 * @author xbiber00
 */
public class Vector {

    private final double x;
    private final double y;

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Point a) {
        this.x = a.x();
        this.y = a.y();
    }

    public Vector(Point a, Point b) {
        this.x = b.x() - a.x();
        this.y = b.y() - a.y();
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double length() {
        return (double) Math.sqrt(x * x + y * y);
    }

    public Vector normalized() {
        double length = length();
        return new Vector(x / length, y / length);
    }
    
    public Vector normal()
    {
        return new Vector(-y, x);
    }

    public Vector neg() {
        return new Vector(-x, -y);
    }

    public Point add(Point other) {
        return new Point(x + other.x(), y + other.y());
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector sub(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public Vector mul(double a) {
        return new Vector(x * a, y * a);
    }

    public Vector mul(Vector v) {
        return new Vector(x * v.x, y * v.y);
    }
    
    public double dot(Vector other)
    {
        return x() * other.x() + y() * other.y();
    }
    
    public Vector rotatedCcw(double angle) {
        double sin = Math.sin(-angle);
        double cos = Math.cos(-angle);
        
        return new Vector((double) (x * cos - y * sin), (double) (x * sin + y * cos));
    }
    
    public Point asPoint() {
        return new Point(Math.round(x), Math.round(y));
    }

    @Override
    public String toString() {
        return "Vector{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Point)) {
            return false;
        }
        return (this.x == ((Point) other).x()
                && this.y == ((Point) other).y());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }
}
