package org.upstox.tickerService.service;

public interface DataService extends Runnable{
    void onLine(String line);
}
