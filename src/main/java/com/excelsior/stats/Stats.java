package com.excelsior.stats;

public class Stats implements StatsMBean {
    public int getMessagesPerSecond() {
        return StatsManager.getMessagesPerSecond();
    }

    public double getMegaBytesOutThroughput() {
        // Return Megabytes a second
        return StatsManager.getGlobalTrafficHandler().getTrafficCounter().getLastWriteThroughput() / 1048576;
    }

    public double getMegaBytesInThroughput() {
        return StatsManager.getGlobalTrafficHandler().getTrafficCounter().getLastReadThroughput() / 1048576;
    }

    public int getConnectionCount() {
        return StatsManager.getConnectionCount();
    }
}
