package com.excelsior.stats;


public interface StatsMBean {

    public int getMessagesPerSecond();

    public double getBytesOutThroughput();

    public double getBytesInThroughput();
}
