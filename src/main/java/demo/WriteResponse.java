package demo;

public class WriteResponse {

    private int v;
    private int t;
    private int r;
    private boolean ack;
    

    public WriteResponse(int v, int t, int r, boolean ack) {
        this.v = v;
        this.t = t;
        this.r = r;
        this.ack = ack;
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

    public boolean getAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }
}
