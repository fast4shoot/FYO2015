package fyoprojekt;

/**
 *
 * @author fast4shoot
 */
public class ElementFactory {
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public ElementFactory(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Element circle(double cx, double cy, double diameter, double minAngle, double maxAngle, double lightMult)
    {
        return oval(cx, cy, diameter, diameter, minAngle, maxAngle, lightMult);
    }
    
    public Element oval(double cx, double cy, double w, double h, double minAngle, double maxAngle, double lightMult)
    {
        return new OvalElement(new Point((cx + x) / width, (cy + y) / height), w / width, h / height, minAngle, maxAngle, lightMult);
    }
    
    public Element hyperbola(double cx, double cy, double a, double b, double bottomClip, double topClip, double intensityMultiplier)
    {
        return new HyperbolicElement(new Point((cx + x) / width, (cy + y) / height), a / width , b / height, bottomClip / height, topClip / height, intensityMultiplier);
    }
    
    public Element line(double ax, double ay, double bx, double by, double lightMult)
    {
        return new LineElement(new Point((ax + x) / width, (ay + y) / height), new Point((bx + x) / width, (by + y) / height), lightMult);
    }
}
