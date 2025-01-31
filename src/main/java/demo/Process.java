package demo;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.*;


public class Process extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final int id;
    private List<ActorRef> allProcesses;

    private int localValue = 0;
    private int localTS = 0;
    private int r = 0; 
    private HashMap<Integer, HashMapObject> map = new HashMap<>();
    private boolean isCrashed = false;
    private int nbOfOperations=0;
    int counter=0;
    public Process(int id) {
        this.id = id;
    }

    public static Props props(int id) {
        return Props.create(Process.class, () -> new Process(id));
    }

    public void read() { 
        r++; 
        int currentR = r; 

        map.put(currentR, new HashMapObject()); 
        allProcesses.forEach(p -> p.tell(new ReadRequest(currentR, true), getSelf()));
        log.info("Process {} initiated READ with request ID {}", id, currentR);
    } 

    public void write(int newValue) {
        r++; 
        int currentR = r;
        TimingTracker.markStart();
        HashMapObject hmo = new HashMapObject();
        hmo.setNewValue(newValue);

        map.put(currentR, hmo);

        allProcesses.forEach(p -> p.tell(new ReadRequest(currentR, false), getSelf()));
        log.info("Process {} initiated WRITE with request ID {} and value {}", id, currentR, newValue);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, msg -> {
                if (msg.equals("CRASH")) {
                    log.info("Process {} is crashing.", id);
                    isCrashed = true;
                } else {
                    for (int i = 0; i < 5; i++) {
                        read();
                    }
                }
            })
            .match(List.class, msg ->{
                allProcesses = msg;
            })
            .match(Launch.class, msg -> {
                if (!isCrashed) {
                    log.info("Process {} launched with {} operations.", id, msg.getM());
                   /* for (int i = 0; i < msg.getM(); i++) {
                        int valueToWrite = 1 + i * allProcesses.size() + id;
                        write(valueToWrite);
                    }*/
                    nbOfOperations=msg.getM()*2;
                    int valueToWrite = 1 + counter * allProcesses.size() + id;
                    counter++;
                    nbOfOperations--;
                    write(valueToWrite);
                    
                }
            })
            .match(ReadResponse.class, msg -> {
                if (!isCrashed ) {
                    HashMapObject object = map.get(msg.getR());
                    if (object == null || object.getCounterReadResponse() < 0) {
                        return;
                    }

                    object.setCounterReadResponse(object.getCounterReadResponse() + 1);

                    if (msg.getT() > object.getTmax()) {
                        object.setTmax(msg.getT());
                        object.setVmax(msg.getV());
                    } else if (msg.getT() == object.getTmax()) {
                        object.setVmax(Math.max(object.getVmax(), msg.getV()));
                    }

                    map.put(msg.getR(), object);

                    if (object.getCounterReadResponse() > allProcesses.size() / 2) {
                        if (msg.isFromRead()) {
                            allProcesses.forEach(p -> p.tell(new WriteRequest(object.getVmax(), object.getTmax(), msg.getR()), getSelf()));
                           // log.info("Process {} sending WRITEBACK for READ request ID {}", id, msg.getR());
                        } else {
                            allProcesses.forEach(p -> p.tell(new WriteRequest(object.getNewValue(), object.getTmax() + 1, msg.getR()), getSelf()));
                            //log.info("Process {} sending WRITE for WRITE request ID {} with value {}", id, msg.getR(), object.getNewValue());
                        }
                        object.setCounterReadResponse(-1);
                        map.put(msg.getR(), object);
                    }
                }
            })
            .match(WriteResponse.class, msg -> {
                if (!isCrashed) {
                    HashMapObject object = map.get(msg.getR());
                    if (object == null || object.getCounterWriteResponse() < 0) {
                        return;
                    }
                    object.setCounterWriteResponse(object.getCounterWriteResponse() + 1);
                    map.put(msg.getR(), object);
                   // log.info("Process received WriteResponse");
                    if (object.getCounterWriteResponse() > allProcesses.size() / 2) {
                        if(object.getNewValue() == null)
                       {
                        	log.info("Process {} completed Read operation:  {} with timestamp {}", id, msg.getV(),msg.getT());
                        	 TimingTracker.operationDone();
                        	 int valueToWrite = 1 + counter * allProcesses.size() + id;
                             counter++;
                            if(nbOfOperations>0) {
                            	write(valueToWrite);
                            	nbOfOperations--;
                            }
                            
                       }
                        else {
                        	 TimingTracker.operationDone();
                            log.info("Process {} completed WRITE operation: {} with timestamp {}", id, object.getNewValue(),object.getTmax());
                            if(nbOfOperations>0) {
                            	read();
                            	nbOfOperations--;
                            }
                        }
                        object.setCounterWriteResponse(-1);
                        map.put(msg.getR(), object);
                    }
                }
            })
            .match(WriteRequest.class, msg -> {
                if (!isCrashed) {
                    if (msg.getT() > localTS || (msg.getT() == localTS && msg.getV() > localValue)) {
                        localValue = msg.getV();
                        localTS = msg.getT();
                      //  log.info("Process {} updated localValue to {} with localTS {}", id, localValue, localTS);
                    }
                    getSender().tell(new WriteResponse(msg.getV(), msg.getT(), msg.getR(), true), getSelf());
                }
            })
            .match(ReadRequest.class, msg -> {
                if (!isCrashed) {
             
                    getSender().tell(new ReadResponse(localValue, localTS, msg.getR(), msg.isFromRead()), getSelf());
                  //  log.info("Process {} responded to ReadRequest ID {} with value {} and timestamp {}", id, msg.getR(), localValue, localTS);
                }
            })
            .build();
    }

}   


