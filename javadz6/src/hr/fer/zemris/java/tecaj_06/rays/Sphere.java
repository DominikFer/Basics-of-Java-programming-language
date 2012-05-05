package hr.fer.zemris.java.tecaj_06.rays;

/**
 * Class which defines Sphere object for raytracing/raycasting. 
 */
public class Sphere extends GraphicalObject {

    private Point3D center;
    private double radius;
    private double kdr;
    private double kdg;
    private double kdb;
    private double krr;
    private double krg;
    private double krb;
    private double krn;
    
    /**
     * Creates new Sphere object with provided arguments.
     * 
     * @param center	Sphere center.
     * @param radius	Sphere radius.
     * @param kdr		Diffuse component coefficient for RED color.
     * @param kdg		Diffuse component coefficient for GREEN color.
     * @param kdb		Diffuse component coefficient for BLUE color.
     * @param krr		Reflective component coefficient for RED color.
     * @param krg		Reflective component coefficient for GREEN color.
     * @param krb		Reflective component coefficient for BLUE color.
     * @param krn		Exponent to power cos(n) in Phong illumination model.
     */
    public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb, double krn) {
        this.center = center;
        this.radius = radius;
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.krr = krr;
        this.krg = krg;
        this.krb = krb;
        this.krn = krn;
    }

    /**
     * Finds closest ray intersection between provided ray and Sphere object.
     * 
     * @return	Closest intersection between ray and Sphere, <code>null</code> if it doesn't exist.
     */
    public RayIntersection findClosestRayIntersection(Ray ray) {
    	Point3D s = ray.start;
    	Point3D c = this.center;
    	Point3D v = s.sub(c);
    	Point3D d = ray.direction;
    	double r = this.radius;
    	
    	double A = d.scalarProduct(d);
    	double B = v.scalarMultiply(2.0).scalarProduct(d);
    	double C = v.scalarProduct(v) - Math.pow(r, 2);
    	
    	double discriminant = Math.pow(B, 2) - (4*A*C);
    	
    	if (discriminant < 0)
    		return null;
    	
    	double t1 = (-B + Math.sqrt(discriminant)) / (2*A);
    	double t2 = (-B - Math.sqrt(discriminant)) / (2*A);
    	
    	if (t1 <= 0 && t2 <= 0)
    		return null;
    	
    	Point3D firstIntersection = s.add(d.scalarMultiply(t1));
    	Point3D secondIntersection = s.add(d.scalarMultiply(t2));
    	
    	double firstIntersectionDistance = firstIntersection.sub(s).norm();
    	double secondIntersectionDistance = secondIntersection.sub(s).norm();
    	
    	Point3D closerIntersection = firstIntersection;
    	double closerIntersectionDistance = firstIntersectionDistance;
    	if (firstIntersectionDistance > secondIntersectionDistance) {
    		closerIntersection = secondIntersection;
    		closerIntersectionDistance = secondIntersectionDistance;
    	}
    	
    	boolean outerIntersection = closerIntersection.sub(getCenter()).norm() > radius;
    	
    	RayIntersection closestIntersection = new RayIntersection(closerIntersection, closerIntersectionDistance, outerIntersection) {

    		@Override
            public Point3D getNormal() {
                return this.getPoint().sub(getCenter()).normalize();
            }

            @Override
            public double getKdr() {return kdr;}
            @Override
            public double getKdg() {return kdg;}
            @Override
            public double getKdb() {return kdb;}
            @Override
            public double getKrr() {return krr;}
            @Override
            public double getKrg() {return krg;}
            @Override
            public double getKrb() {return krb;}
            @Override
            public double getKrn() {return krn;}
        };
    	
        return closestIntersection;
    }

    /**
     * @return Returns sphere center.
     */
	public Point3D getCenter() {
		return center;
	}

    /**
     * @return Returns sphere radius.
     */
	public double getRadius() {
		return radius;
	}

    /**
     * @return Returns diffuse component coefficient for RED color.
     */
	public double getKdr() {
		return kdr;
	}

    /**
     * @return Returns diffuse component coefficient for GREEN color.
     */
	public double getKdg() {
		return kdg;
	}

    /**
     * @return Returns diffuse component coefficient for BLUE color.
     */
	public double getKdb() {
		return kdb;
	}

    /**
     * @return Returns reflective component coefficient for RED color.
     */
	public double getKrr() {
		return krr;
	}
	
    /**
     * @return Returns reflective component coefficient for GREEN color.
     */
	public double getKrg() {
		return krg;
	}

    /**
     * @return Returns reflective component coefficient for BLUE color.
     */
	public double getKrb() {
		return krb;
	}

    /**
     * @return Returns exponent to power cos(n) in Phong illumination model.
     */
	public double getKrn() {
		return krn;
	}
}