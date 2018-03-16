package model;
// QCHA CODE
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter

import utils.Tensor;

public class Satellite {
    // state vector
    public StateVector stateVector = new StateVector();
    public Tensor tensor = new Tensor();
    // tensor projections
    public double ix;
    public double iy;
    public double iz;
    // mass
    public double m;
    // cross-sectional area
    public double area;
    // drag cofficient
    public double c;
    // ballistic coefficient
    public double bb;

    public Satellite() {
    }

    public Satellite(double x, double y, double z, double vx, double vy, double vz, double mass) {
        this.stateVector.x = x;
        this.stateVector.y = y;
        this.stateVector.z = z;
        this.stateVector.vx = vx;
        this.stateVector.vy = vy;
        this.stateVector.vz = vz;
        this.m = mass;
    }

    public Satellite(double x, double y, double z, double vx, double vy, double vz, double ix, double iy, double iz,
                     double mass, double area, double c) {
        this.stateVector.x = x;
        this.stateVector.y = y;
        this.stateVector.z = z;
        this.stateVector.vx = vx;
        this.stateVector.vy = vy;
        this.stateVector.vz = vz;
        this.tensor.ix = ix;
        this.tensor.iy = iy;
        this.tensor.iz = iz;
        this.m = mass;
        this.area = area;
        this.c = c;
        this.bb = mass / (c * area);
    }

    public Satellite(Satellite satellite) {
        this.stateVector = satellite.stateVector;
        this.tensor = satellite.tensor;
        this.m = satellite.m;
        this.area = satellite.area;
        this.c = satellite.c;
        this.bb = satellite.m / (satellite.c * satellite.area);
    }

    public Satellite(StateVector stateVector, Tensor tensor, double mass, double area, double c) {
        this.stateVector = stateVector;
        this.tensor = tensor;
        this.m = mass;
        this.area = area;
        this.c = c;
        this.bb = mass / (c * area);
    }
}