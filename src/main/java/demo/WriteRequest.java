package demo;

public class WriteRequest {

    private int v;
    private int t;
    private int r; 

    public WriteRequest(int v, int t, int r) {
        this.v = v;
        this.t = t;
        this.r = r;
    }


    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
