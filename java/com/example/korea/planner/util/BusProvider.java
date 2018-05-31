package com.example.korea.planner.util;

import com.squareup.otto.Bus;

/**
 * Created by korea on 2017-03-23.
 */

public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
    }
}
