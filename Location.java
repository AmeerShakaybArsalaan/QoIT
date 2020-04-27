
public class Location {
		double x, y;

		Location(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		Location getMidpoint(Location pt2) {
			Location mPt = new Location((x+pt2.x)/2.0, (y+pt2.y)/2.0);
			return mPt;
		}
		
		public String toString() {
			String result = Double.toString(x) + ":" + Double.toString(y);
			return result;
		}
}
