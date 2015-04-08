package fyoprojekt;

/**
 *
 * @author fast4shoot
 */
public class LineEquation {
    private final double a, b, c;

    public LineEquation(Point p, Vector dir)
    {
        Vector normal = dir.normal();
        
        a = normal.x();
        b = normal.y();
        c = -a * p.x() - b * p.y();
    }
    
    public double a() {
        return a;
    }

    public double b() {
        return b;
    }

    public double c() {
        return c;
    }
}
