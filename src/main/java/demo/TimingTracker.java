package demo;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorSystem;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TimingTracker {
    private static volatile long startTime = 0L;
    private static volatile long endTime = 0L;
    private static AtomicBoolean started = new AtomicBoolean(false);
    private static volatile int totalOperations = 0;
    private static AtomicInteger completedOperations = new AtomicInteger(0);

    // Logging adapter
    private static LoggingAdapter log;

    // Initialize with logging from ActorSystem
    public static void init(int totalOps, ActorSystem system) {
        totalOperations = totalOps;
        completedOperations.set(0);
        started.set(false);
        startTime = 0L;
        endTime = 0L;

        // Initialize logging
        log = Logging.getLogger(system, "TimingTracker");
        log.info("TimingTracker initialized. Expecting {} total operations.", totalOps);
    }

    // Mark the start time only once, when the first write happens
    public static void markStart() {
        if (started.compareAndSet(false, true)) {
            startTime = System.currentTimeMillis();
            log.info("=== First WRITE operation detected. Timing started at {} ms ===", startTime);
        }
    }

    // Called when an operation (read or write) is completed
    public static void operationDone() {
        int currentCount = completedOperations.incrementAndGet();
      //  log.info("Operation {} completed out of {} expected.", currentCount, totalOperations);

        if (currentCount == totalOperations) {
            endTime = System.currentTimeMillis();
            long totalExecutionTime = endTime - startTime;
            log.info("=== All operations completed! ===");
            log.info("Total execution time: {} ms", totalExecutionTime);
        }
    
          
        }
    }

