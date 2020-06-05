package com.order.track.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.order.track.entity.Order;
import com.order.track.model.TrackOrder;

@Mapper(componentModel="spring")
@DecoratedWith(OrderTrackingMapperDecorator.class)
public interface OrderTrackingMapper {

    OrderTrackingMapper INSTANCE = Mappers.getMapper(OrderTrackingMapper.class);

    @Mapping(target = "data.id", source = "orderId")
    TrackOrder mapOrderToTrackOrder(final Order order);

}
