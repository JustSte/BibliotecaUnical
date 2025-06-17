package com.Stefan.BibliotecaUnical.event;

import org.springframework.context.ApplicationEvent;

public class MapLayoutUpdatedEvent extends ApplicationEvent {
    public MapLayoutUpdatedEvent(Object source)
    {
        super(source);
    }
}
