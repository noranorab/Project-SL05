package demo;

public class HashMapObject {
    private int counterReadResponse;
    private int counterWriteResponse;
    private int tmax;
    private int vmax;
    private Integer newValue; 

    public HashMapObject() {
        this.counterReadResponse = 0;
        this.counterWriteResponse = 0;
        this.tmax = Integer.MIN_VALUE;
        this.vmax = Integer.MIN_VALUE;
        this.newValue = null; 
    }

  
    public Integer getNewValue() {
        return newValue;
    }

    public void setNewValue(Integer newValue) {
        this.newValue = newValue;
    }





    public int getCounterReadResponse() {
        return counterReadResponse;
    }

    public void setCounterReadResponse(int counterReadResponse) {
        this.counterReadResponse = counterReadResponse;
    }

    public int getCounterWriteResponse() {
        return counterWriteResponse;
    }

    public void setCounterWriteResponse(int counterWriteResponse) {
        this.counterWriteResponse = counterWriteResponse;
    }

    public int getTmax() {
        return tmax;
    }

    public void setTmax(int tmax) {
        this.tmax = tmax;
    }

    public int getVmax() {
        return vmax;
    }

    public void setVmax(int vmax) {
        this.vmax = vmax;
    }
}