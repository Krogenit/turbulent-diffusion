package diffuse;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ThreadRlay implements Runnable {

	public static NumberFormat formatter = new DecimalFormat("#0.00");
	RLAYTWODIM rlay;
	double x, y, z;
	double x0, c, z0;
	
	double result;
	
	public ThreadRlay(RLAYTWODIM rlay, double x, double y, double z, double x0, double c, double z0) {
		this.rlay = rlay;
		this.x = x;
		this.y = y;
		this.z = z;
		this.x0 = x0;
		this.c = c;
		this.z0 = z0;
	}
	
	@Override
	public void run() {
		rlay.setX(x);
		rlay.setY(y);
		rlay.setZ(z);
		rlay.setC(c);
		rlay.setX0(x0);
		rlay.setY0(0);
		rlay.setZ0(z0);
		long time1 = System.currentTimeMillis();
		result = rlay.eval();
		long diff = System.currentTimeMillis() - time1;
		System.out.println(" X = " + formatter.format(x) + " Y = " + formatter.format(y) + " ReF = " + result + " Time = " + diff + "ms");
	}
	
	public double getResult() {
		return this.result;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
}
