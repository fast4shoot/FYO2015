package fyoprojekt;

/**
 * Representation of one podouble in the coordinate system
 * @author xbiber00
 */
public class Point {

    private final double x;
    private final double y;

    public Point() {
        x = 0.0;
        y = 0.0;
    }
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double x() {
        return x;
    }

    public double y() {
        return y;
    }
    
    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
    
    @Override
    public boolean equals( Object other ) {
        if ( ! ( other instanceof Point ) ) return false;
        return ( this.x == ( ( Point ) other ).x() &&
                 this.y == ( ( Point ) other ).y() );
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 13 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

}
