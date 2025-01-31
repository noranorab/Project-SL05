package demo;

public class ReadRequest{
    private int r;
    private boolean isFromRead; 

    public ReadRequest(int r, boolean isFromRead) {
        this.r = r;
        this.isFromRead = isFromRead; 
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
