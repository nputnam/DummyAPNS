package com.excelsior.stats;

import com.excelsior.handler.traffic.GlobalTrafficShapingHandler;
import com.excelsior.push.EnhancedNotification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class StatsManager {

    private static final Logger log = LogManager.getLogger(StatsManager.class);

    private static StatsManager instance = new StatsManager();
    private static long count = 0;
    private static AtomicLong currentCount = new AtomicLong(0);
    private static final long startTime = new Date().getTime();
    private static GlobalTrafficShapingHandler trafficShapingHandler = new GlobalTrafficShapingHandler(Executors.newSingleThreadExecutor());

    static {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count = currentCount.getAndSet(0);
            }
        },0, TimeUnit.SECONDS.toMillis(1));
    }

    private StatsManager() {
        log.info("Starting stats manager");
        try {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      ObjectName name = new ObjectName("com.excelsior:type=Stats");
      StatsMBean mbean = new Stats();
      mbs.registerMBean(mbean, name);
        }
        catch (Exception e)  {
            log.error("Error starting stats manager.",e);
        }
    }

    public static void incr(EnhancedNotification notification) {
        currentCount.getAndAdd(1);
    }

    public static int getMessagesPerSecond() {
        return currentCount.intValue();
    }

    public static GlobalTrafficShapingHandler getGlobalTrafficHandler() {
        return trafficShapingHandler;
    }
}
