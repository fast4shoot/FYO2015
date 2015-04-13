/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyoprojekt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import static java.lang.Math.sqrt;
import static java.lang.Math.acos;
import static java.lang.Math.PI;
import static java.lang.Double.isNaN;

/**
 *
 * @author fast4shoot
 */
public class OvalElement implements Element {

    private final Point center;
    private final double width;
    private final double height;
    private final double a;
    private final double b;
    
    private final double minAngle;
    private final double maxAngle;

    private final double intensityMultiplier;

    /**
     * center je pochopitelne stred elipsy a width a height jsou jeji rozmery
     * minAngle a maxAngle urcuji jakesi pseudo uhly (jako kdyby toto byl kruh a ne elipsa),
     * ktere urcuji, ktera vysec teto elipsy je funkcni; prehozenim techto dvou parametru se 
     * invertuje funkci cast elipsy
     * 
     * takze treba v pripade hubbla chceme mit aktivni cast vpravo, od cca 340 do 20 stupnu,
     * ovsem v radianech (bacha, jeste se to korektne nekresli)
     * 
     * no a intensitymultiplier samozrejme urcuje, kolik svetla se odrazi
     * pro zrcadla nastavit neco kolem jednicky a pro obal nastavit neco kolem nuly
     */
    public OvalElement(Point center, double width, double height, double minAngle, double maxAngle, double intensityMultiplier) {
        this.center = center;
        this.width = width;
        this.height = height;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.intensityMultiplier = intensityMultiplier;
        
        a = this.width * 0.5;
        b = this.height * 0.5;
    }
    
    public void paint(Graphics g, int w, int h) {
        Vector size = new Vector(width, height);
        Vector minLimit = new Vector(1, 0).rotatedCcw(minAngle);
        Vector maxLimit = new Vector(1, 0).rotatedCcw(maxAngle);
        
        int shapeStepCount = 8;
        double shapeAngleStep = (minAngle < maxAngle ? (maxAngle - minAngle) : (maxAngle - minAngle + 2 * PI)) / shapeStepCount;
        
        int[] shapeXs = new int[shapeStepCount + 2];
        int[] shapeYs = new int[shapeStepCount + 2];
        
        Point minLimitPt = minLimit.mul(size).add(center);
        Point maxLimitPt = maxLimit.mul(size).add(center);
        
        shapeXs[0] = (int)Math.round(minLimitPt.x() * w);
        shapeYs[0] = (int)Math.round((1.0 - minLimitPt.y()) * h);
        
        shapeXs[shapeStepCount] = (int)Math.round(maxLimitPt.x() * w);
        shapeYs[shapeStepCount] = (int)Math.round((1.0 - maxLimitPt.y()) * h);
        
        shapeXs[shapeStepCount + 1] = (int)Math.round(center.x() * w);
        shapeYs[shapeStepCount + 1] = (int)Math.round((1.0 - center.y()) * h);
        
        for (int i = 1; i < (shapeStepCount); i++)
        {
            Vector offset = minLimit.rotatedCcw(shapeAngleStep * i).mul(size);
            Point point = offset.add(center);
            
            shapeXs[i] = (int)Math.round(point.x() * w);
            shapeYs[i] = (int)Math.round((1.0 - point.y()) * h);
        }
        
        Shape shape = new Polygon(shapeXs, shapeYs, shapeStepCount + 2);
        
        Shape oldClip = g.getClip();
        g.setClip(shape);
        g.drawOval(
            (int)Math.round((center.x() - width * 0.5) * w),
            (int)Math.round((1.0 - center.y() - height * 0.5) * h),
            (int)Math.round(width * w),
            (int)Math.round(height * h)
        );
        g.setClip(oldClip);
    }

    public Ray testHit(Ray ray) {
        double X = center.x();
        double Y = center.y();
        double c = ray.getLineEq().a();
        double d = ray.getLineEq().b();
        double e = ray.getLineEq().c();
        
        // HERE BE DRAGONS
        double x1 = -(a*b*d*sqrt(-d*d*Y*Y + (-2*c*d*X - 2*d*e)*Y - c*c*X*X - 2*c*e*X - e*e + b*b*d*d + a*a*c*c) + a*a*c*d*Y - b*b*d*d*X + a*a*c*e)/(b*b*d*d + a*a*c*c);
        double y1 =  (a*b*c*sqrt(-d*d*Y*Y - 2*c*d*X*Y - 2*d*e*Y  - c*c*X*X - 2*c*e*X - e*e + b*b*d*d + a*a*c*c) + a*a*c*c*Y - b*b*c*d*X - b*b*d*e)/(b*b*d*d + a*a*c*c);
        
        double x2 =  (a*b*d*sqrt(-d*d*Y*Y + (-2*c*d*X - 2*d*e)*Y - c*c*X*X - 2*c*e*X - e*e + b*b*d*d + a*a*c*c) - a*a*c*d*Y + b*b*d*d*X - a*a*c*e)/(b*b*d*d + a*a*c*c);
        double y2 = (-a*b*c*sqrt(-d*d*Y*Y - 2*c*d*X*Y - 2*d*e*Y  - c*c*X*X - 2*c*e*X - e*e + b*b*d*d + a*a*c*c) + a*a*c*c*Y - b*b*c*d*X - b*b*d*e)/(b*b*d*d + a*a*c*c);
        
        if (isNaN(x1) || isNaN(y1) || isNaN(x2) || isNaN(y2)) return null;
        
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        
        Vector toP1 = new Vector(ray.point, p1);
        Vector toP2 = new Vector(ray.point, p2);
        
        boolean p1Eligible = testPointEligibility(p1, ray);
        boolean p2Eligible = testPointEligibility(p2, ray);
        Point nearest = toP1.length() < toP2.length() ? p1 : p2;
        
        Point finalPoi;
        
        if (p1Eligible && p2Eligible) finalPoi = nearest;
        else if (p1Eligible) finalPoi = p1;
        else if (p2Eligible) finalPoi = p2;
        else return null;
        
        Vector normal = new Vector((finalPoi.x() - center.x()) / (a*a), (finalPoi.y() - center.y()) / (b*b));
        return ray.reflect(finalPoi, normal, intensityMultiplier);
    }
    
    private boolean testPointEligibility(Point p, Ray ray)
    {
        Vector toP = new Vector(ray.point, p);
        if (toP.dot(ray.getDirection()) <= 0) return false;
        Vector fromCenter = new Vector(center, p).mul(new Vector(1.0/width, 1.0/height));
        double angle = acos(fromCenter.normalized().x());
        if (fromCenter.y() < 0) angle = 2 * PI - angle;
        
        if (minAngle < maxAngle) {
            return minAngle <= angle && angle <= maxAngle;
        } else {
            return minAngle <= angle || angle <= maxAngle;
        }
    }
}
