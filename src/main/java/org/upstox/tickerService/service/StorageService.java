package org.upstox.tickerService.service;

import org.upstox.tickerService.model.Tick;

public interface StorageService extends Runnable {

    void onTick(Tick tick);
}
