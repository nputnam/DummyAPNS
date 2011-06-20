package com.excelsior.stats;

import com.excelsior.push.EnhancedNotification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class StatsManager {

    private static final Logger log = LogManager.getLogger(StatsManager.class);

    private static StatsManager instance = new StatsManager();
    private static AtomicLong count = new AtomicLong(0);
    private static final long startTime = new Date().getTime();

    private StatsManager() {
    }

    public static void incr(EnhancedNotification notification) {
        count.getAndAdd(1);
        if (count.longValue() % 100000 == 0) {
            long delta = ((new Date().getTime() - startTime) / 1000) + 1;

            log.info("Current rate is " + (count.longValue() / delta));

            log.info(count.longValue() + " : " + new String(notification.getPayload()) + " " + count.longValue() / delta);
        }
    }
}
