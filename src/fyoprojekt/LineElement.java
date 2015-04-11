/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyoprojekt;

import java.awt.Graphics;


public class LineElement implements Element {
    
    private final Point pa, pb;
    private final Vector direction;
    private final double lightMult;
    private final LineEquation eq;
    
    public LineElement(Point pa, Point pb, double lightMult)
    {
        this.pa = pa;
        this.pb = pb;
        this.lightMult = lightMult;
        
        direction = new Vector(pa, pb);
        eq = new LineEquation(pa, direction);
    }
    
    public void paint(Graphics g, int w, int h) {
        g.drawLine((int)(pa.x() * w), (int)((1.0 - pa.y()) * h), (int)(pb.x() * w), (int)((1.0 - pb.y()) * h));
    }

    public Ray testHit(Ray ray) {
        LineEquation rayEq = ray.getLineEq();
        double denominator = eq.b() * rayEq.a() - eq.a() * rayEq.b();
        
        if (denominator == 0.0) return null;
        
        double numerator = eq.c() * rayEq.b() - eq.b() * rayEq.c();
        double x = numerator / denominator;
        double y;
        
        if (eq.b() == 0.0) y = (-rayEq.a() * x - rayEq.c()) / rayEq.b();
        else y = (-eq.a() * x - eq.c()) / eq.b();
        
        Point poi = new Point(x, y);
        
        double abdist = new Vector(pa, pb).length();
        if (new Vector(poi, pa).length() <= abdist && new Vector(poi, pb).length() <= abdist)
        {
            return ray.reflect(poi, direction.normal(), lightMult);
        }
        else
        {
            return null;
        }
    }
}
