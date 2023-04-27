package edu.duke.ece568.team24.miniups.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.ece568.team24.miniups.dto.OrderDto;
import edu.duke.ece568.team24.miniups.dto.PackageDto;
import edu.duke.ece568.team24.miniups.model.*;
import edu.duke.ece568.team24.miniups.repository.*;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDto createOrder(int id, int destinationX, int destinationY, String ownerUsername) {
        return OrderDto
                .mapper(orderRepository.save(new OrderEntity(id, destinationX, destinationY, ownerUsername)));
    }

    public OrderDto findById(int id) {
        return OrderDto
                .mapper(orderRepository.findById(id).orElse(null));
    }

    public List<OrderDto> findByOwner(String username) {
        return orderRepository.findByOwnerUsername(username).stream().map((o) -> {
            List<PackageDto> packages = o.getPackages().stream().map(PackageDto::mapper).toList();
            OrderDto orderDto = OrderDto.mapper(o);
            orderDto.setPackages(packages);
            return orderDto;
        }).toList();
    }

    public OrderDto updateStatus(int id, String status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Order: " + id));
        order.setStatus(status);
        return OrderDto.mapper(orderRepository.save(order));
    }

    public OrderDto updateDestination(int id, int destinationX, int destinationY) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Order: " + id));
        order.setDestinationX(destinationX);
        order.setDestinationY(destinationY);
        return OrderDto.mapper(orderRepository.save(order));
    }
}
