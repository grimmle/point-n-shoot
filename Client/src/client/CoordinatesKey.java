package client;

public class CoordinatesKey {
	private final int x;
    private final int y;

    public CoordinatesKey(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CoordinatesKey)) {
            return false;
        }

        CoordinatesKey otherKey = (CoordinatesKey) object;
        return this.x == otherKey.x && this.y == otherKey.y;
    }

    @Override
    public int hashCode() {
        int result = 17; // any prime number
        result = 31 * result + Integer.valueOf(this.x).hashCode();
        result = 31 * result + Integer.valueOf(this.y).hashCode();
        return result;
    }
}
