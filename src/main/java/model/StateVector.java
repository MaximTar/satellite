package model;

/**
 * Created by Maxim Tarasov on 31.08.2017.
 *
 */
public class StateVector {

    //coordinates
    public double x;
    public double y;
    public double z;
    // velocity projections
    public double vx;
    public double vy;
    public double vz;

    public StateVector(double x, double y, double z, double vx, double vy, double vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public StateVector() {
    }

    @Override
    public String toString() {
        return "StateVector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", vx=" + vx +
                ", vy=" + vy +
                ", vz=" + vz +
                '}';
    }

    public static StateVector sum(StateVector a, StateVector b) {
        StateVector c = new StateVector(0, 0, 0, 0, 0, 0);
        c.x = a.x + b.x;
        c.y = a.y + b.y;
        c.z = a.z + b.z;
        c.vx = a.vx + b.vx;
        c.vy = a.vy + b.vy;
        c.vz = a.vz + b.vz;
        return c;
    }

    public static StateVector sub(StateVector a, StateVector b) {
        StateVector c = new StateVector(0, 0, 0, 0, 0, 0);
        c.x = a.x - b.x;
        c.y = a.y - b.y;
        c.z = a.z - b.z;
        c.vx = a.vx - b.vx;
        c.vy = a.vy - b.vy;
        c.vz = a.vz - b.vz;
        return c;
    }

    public static StateVector mult(double a, StateVector b) {
        StateVector c = new StateVector(0, 0, 0, 0, 0, 0);
        c.x = a * b.x;
        c.y = a * b.y;
        c.z = a * b.z;
        c.vx = a * b.vx;
        c.vy = a * b.vy;
        c.vz = a * b.vz;
        return c;
    }

    public static StateVector mult(StateVector b, double a) {
        StateVector c = new StateVector(0, 0, 0, 0, 0, 0);
        c.x = a * b.x;
        c.y = a * b.y;
        c.z = a * b.z;
        c.vx = a * b.vx;
        c.vy = a * b.vy;
        c.vz = a * b.vz;
        return c;
    }
}
