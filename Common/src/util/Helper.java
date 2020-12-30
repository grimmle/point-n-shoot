package util;

public class Helper {
	
	public static double map(double value, double start1, double stop1, double start2, double stop2) {
		return start2 + (stop2 - start2) * (value - start1) / (stop1 - start1);
	}

}
