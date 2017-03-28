package com.akexorcist.knoxactivator;

import com.squareup.otto.Bus;

/**
 * Created by Akexorcist on 4/21/2016 AD.
 */
public class KnoxActivationBus {
    private static KnoxActivationBus knoxActivationBus;

    public static KnoxActivationBus getInstance() {
        if (knoxActivationBus == null) {
            knoxActivationBus = new KnoxActivationBus();
        }
        return knoxActivationBus;
    }

    private Bus bus;

    public Bus getBus() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }
}
