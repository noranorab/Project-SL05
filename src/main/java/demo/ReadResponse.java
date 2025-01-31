package demo;

public class ReadResponse {

    private int v;
    private int t;
    private int r;
    private boolean isFromRead; 

    public ReadResponse(int v, int t, int r, boolean isFromRead) {
        this.r = r;
        this.v = v;
        this.t = t;
        this.isFromRead = isFromRead;
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

    public boolean isFromRead() {
        return isFromRead;
    }

    public void setFromRead(boolean isFromRead) {
        this.isFromRead = isFromRead;
    }
}
