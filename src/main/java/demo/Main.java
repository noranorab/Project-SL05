package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Configuration Parameters
        final int N = 100; // Total number of processes
        final int f = 49; // Number of processes to crash (f < N/2)
        final int M = 3; // Number of put/get operations per process

        // Initialize Actor System
        ActorSystem system = ActorSystem.create("DistributedHashMapSystem");
        LoggingAdapter log = Logging.getLogger(system, "Main");
        TimingTracker.init((N-f)*M*2,system);
       
        // Create N Process actors
        List<ActorRef> allProcesses = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            ActorRef process = system.actorOf(Process.props(i), "Process-" + i);
            allProcesses.add(process);
        }
        
        for(ActorRef actor:allProcesses) {
        	actor.tell(allProcesses, null);
        }

        // Wait briefly to ensure all actors are created
        Thread.sleep(2000);
        
       
        // Select f processes at random to crash
        List<ActorRef> processesToCrash = new ArrayList<>(allProcesses);
        Collections.shuffle(processesToCrash);
        processesToCrash = processesToCrash.subList(0, f);
       
        log.info("Crashing {} processes.", f);
        for (ActorRef process : processesToCrash) {
            process.tell("CRASH", ActorRef.noSender());
        }


        // Send Launch message to remaining processes
        log.info("Launching remaining {} processes.", N - f);
        Launch launchMessage = new Launch(M);
        for (ActorRef process : allProcesses) {
            if (!processesToCrash.contains(process)) {
                process.tell(launchMessage, ActorRef.noSender());
            }
        }
        


        TimeUnit.SECONDS.sleep(30);
        
     /*   for (ActorRef process : allProcesses) {
            if (!processesToCrash.contains(process)) {
                process.tell("read", ActorRef.noSender());
            }
        }*/
        // Shutdown the Actor System
        system.terminate();
    }
}
