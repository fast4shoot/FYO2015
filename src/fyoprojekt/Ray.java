package fyoprojekt;

/**
 *
 * @author fast4shoot
 */
public class Ray {
    public final Point point;
    public final Vector direction;
    public final double intensity;
    public final LineEquation lineEq;

    public Ray(Point point, Vector direction, double intensity) {
        this.point = point;
        this.direction = direction;
        this.intensity = intensity;
        lineEq = new LineEquation(point, direction);
    }
    
    public Point getPoint() {
        return point;
    }

    public Vector getDirection() {
        return direction;
    }

    public double getIntensity() {
        return intensity;
    }

    public LineEquation getLineEq() {
        return lineEq;
    }
    
    public Ray reflect(Point pointOfIncidence, Vector normal, double intensityMultiplier)
    {
        if (new Vector(point, pointOfIncidence).dot(direction) <0.0) return null;
        
        normal = normal.normalized();
        
        if (normal.dot(direction.normalized()) > 0.0) normal = normal.neg();
        
        Vector newDirection = direction.sub(normal.mul(2.0 * normal.dot(direction)));
        return new Ray(pointOfIncidence, newDirection, intensity * intensityMultiplier);
    }
}
