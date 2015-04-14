package fyoprojekt;

import java.awt.Graphics;
import static java.lang.Double.isNaN;
import static java.lang.Math.sqrt;

/**
 *
 * @author fast4shoot
 */
public class HyperbolicElement implements Element{
    private final Point center;
    private final double a;
    private final double b;
    private final double bottomClip;
    private final double topClip;
    private final double intensityMultiplier;

    public HyperbolicElement(Point center, double a, double b, double bottomClip, double topClip, double intensityMultiplier) {
        this.center = center;
        this.a = a;
        this.b = b;
        this.bottomClip = bottomClip;
        this.topClip = topClip;
        this.intensityMultiplier = intensityMultiplier;
    }
    
    public void paint(Graphics g, int w, int h) {
        double bottom = Math.max(0.0, center.y() + bottomClip);
        double top = Math.min(1.0, center.y() + topClip);
        int steps = Math.max(h / 8, 2);
        double step = (top - bottom) / steps;
        double X = center.x() - (b < 0.0 ? -Math.abs(a) : Math.abs(a));
        double Y = center.y();
        double a = Math.abs(this.a);
        double b = Math.abs(this.b);
        
        double prevx = 0.0;
        double prevy = 0.0;
        
        for (int i = 0; i <= steps; i++)
        {
            double y = bottom + step * i;
            double x1 = -(a*sqrt(Y*Y - 2*y*Y + y*y + b*b) - b*X)/b;
            double x2 =  (a*sqrt(Y*Y - 2*y*Y + y*y + b*b) + b*X)/b;
            
            double x;
            if (this.b < 0.0)
            {
                if (x1 <= center.x()) x = x1;
                else if (x2 <= center.x()) x = x2;
                else {
                    System.err.println("b < 0.0: x1: " + x1 + ", x2: " + x2);
                    return;
                }
            }
            else
            {
                if (x1 >= center.x()) x = x1;
                else if (x2 >= center.x()) x = x2;
                else {
                    System.err.println("b > 0.0: x1: " + x1 + ", x2: " + x2);
                    return;
                }
            }
            
            
            if (i != 0)
            {
                g.drawLine(
                    (int)Math.round(prevx * w),
                    (int)Math.round((1.0 - prevy) * h), 
                    (int)Math.round(x * w), 
                    (int)Math.round((1.0 - y) * h));
            }
            
            prevx = x;
            prevy = y;
        }
        
    }

    public Ray testHit(Ray ray) {
        double c = ray.getLineEq().a();
        double d = ray.getLineEq().b();
        double e = ray.getLineEq().c();
        double X = center.x() - (b < 0.0 ? -Math.abs(a) : Math.abs(a));
        double Y = center.y();
        
        double a = Math.abs(this.a);
        double b = Math.abs(this.b);
        
        double x1 = -( a*b*d*sqrt(d*d*Y*Y + (2*c*d*X + 2*d*e)*Y + c*c*X*X + 2*c*e*X + e*e + b*b*d*d - a*a*c*c) - a*a*c*d*Y - b*b*d*d*X - a*a*c*e)/(b*b*d*d - a*a*c*c);
        double y1 = -(-a*b*c*sqrt(d*d*Y*Y + 2*c*d*X*Y + 2*d*e*Y + c*c*X*X + 2*c*e*X + e*e + b*b*d*d - a*a*c*c) + a*a*c*c*Y + b*b*c*d*X + b*b*d*e)/(b*b*d*d - a*a*c*c);
        
        double x2 =  ( a*b*d*sqrt(d*d*Y*Y + (2*c*d*X + 2*d*e)*Y + c*c*X*X + 2*c*e*X + e*e + b*b*d*d - a*a*c*c) + a*a*c*d*Y + b*b*d*d*X + a*a*c*e)/(b*b*d*d - a*a*c*c);
        double y2 = -( a*b*c*sqrt(d*d*Y*Y + 2*c*d*X*Y + 2*d*e*Y + c*c*X*X + 2*c*e*X + e*e + b*b*d*d - a*a*c*c) + a*a*c*c*Y + b*b*c*d*X + b*b*d*e)/(b*b*d*d - a*a*c*c);
        
        if (isNaN(x1) || isNaN(y1) || isNaN(x2) || isNaN(y2)) return null;
        
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        
        boolean p1Eligible = testPointEligibility(p1, ray);
        boolean p2Eligible = testPointEligibility(p2, ray);
        Point nearest = new Vector(ray.getPoint(), p1).length() < new Vector(ray.getPoint(), p2).length() ? p1 : p2;
        
        Point poi;
        
        if (p1Eligible && p2Eligible) poi = nearest;
        else if (p1Eligible) poi = p1;
        else if (p2Eligible) poi = p2;
        else return null;
        
        Vector normal = new Vector((poi.x() - X) / (a*a), -(poi.y() - Y) / (b*b));
        return ray.reflect(poi, normal, intensityMultiplier);
    }
    
    private boolean testPointEligibility(Point p, Ray ray)
    {
        Vector toP = new Vector(ray.point, p);
        if (toP.dot(ray.getDirection()) <= 0) return false;
        
        if (b < 0.0 && p.x() > center.x()) return false;
        else if (b > 0.0 && p.x() < center.x()) return false;
        else if (p.y() - center.y() < bottomClip) return false;
        return p.y() - center.y() <= topClip;
    }
    
}
