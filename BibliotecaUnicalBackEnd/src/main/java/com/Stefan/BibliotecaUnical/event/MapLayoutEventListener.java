package com.Stefan.BibliotecaUnical.event;

import com.Stefan.BibliotecaUnical.DTO.MapLayoutDTO;
import com.Stefan.BibliotecaUnical.service.MapLayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MapLayoutEventListener  {

    private final MapLayoutService mapLayoutService;

    @TransactionalEventListener
    @EventListener
    @CachePut(value = "mapLayout", key = "'layout'")
    public MapLayoutDTO handleMapLayoutUpdate(MapLayoutUpdatedEvent event)
    {
        mapLayoutService.refreshMapLayout();
        MapLayoutDTO updatedLayout = mapLayoutService.getMapLayout();
        return updatedLayout;
    }

}
