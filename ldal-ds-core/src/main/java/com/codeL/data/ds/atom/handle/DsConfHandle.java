package com.codeL.data.ds.atom.handle;

import com.google.common.eventbus.EventBus;
import com.codeL.data.ds.atom.DataSourceWrapper;


public interface DsConfHandle extends DataSourceWrapper {

    EventBus eventBus = new EventBus();

    default void register() {
        eventBus.register(this);
    }

}
