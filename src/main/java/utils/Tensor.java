package utils;

public final class Tensor {
    public double ix;
    public double iy;
    public double iz;

    public Tensor(double ix, double jy, double kz) {
        this.ix = ix;
        this.iy = jy;
        this.iz = kz;
    }

    public Tensor() {
    }

    @Override
    public String toString() {
        return "Tensor{" +
                "ix=" + ix +
                ", iy=" + iy +
                ", iz=" + iz +
                '}';
    }
}
