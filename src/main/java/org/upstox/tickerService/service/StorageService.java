package org.upstox.tickerService.service;

import org.upstox.tickerService.model.Tick;

public interface StorageService {

    void onTick(Tick tick);
}
