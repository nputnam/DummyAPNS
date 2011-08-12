package com.excelsior.stats;


public interface StatsMBean {

    public int getMessagesPerSecond();

    public double getMegaBytesOutThroughput();

    public double getMegaBytesInThroughput();

    public int getConnectionCount();
}
