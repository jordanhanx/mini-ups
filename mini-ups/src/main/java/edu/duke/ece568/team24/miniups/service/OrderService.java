package edu.duke.ece568.team24.miniups.service;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.ece568.team24.miniups.dto.OrderDto;
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
        if (orderRepository.existsById(id)) {
            throw new EntityExistsException("Order with ID:" + id + " already exists");
        }
        return OrderDto
                .mapper(orderRepository.save(new OrderEntity(id, destinationX, destinationY, ownerUsername)));
    }

    public OrderDto findById(int id) {
        return OrderDto
                .mapper(orderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Not found Order with ID:" + id)));
    }

    public List<OrderDto> findByOwner(String username) {
        return orderRepository.findByOwnerUsername(username).stream()
                .map(OrderDto::mapper)
                .sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
                .toList();
    }

    public OrderDto updateDestination(int id, int destinationX, int destinationY) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Order with ID:" + id));
        if (order.getStatus().equalsIgnoreCase("delivered")) {
            throw new EntityNotFoundException("Order with ID:" + id + "is delivered, cannot change address");
        }
        order.setDestinationX(destinationX);
        order.setDestinationY(destinationY);
        return OrderDto.mapper(orderRepository.save(order));
    }

    public void updateAllOrdersStatus() {
        List<OrderEntity> allOrders = orderRepository.findAll();
        allOrders.stream()
                .filter(o -> o.getStatus().equalsIgnoreCase("created"))
                .filter(o -> o.getPackages().size() > 0)
                .forEach(o -> o.setStatus("out for delivery"));
        allOrders.stream()
                .filter(o -> o.getStatus().equalsIgnoreCase("out for delivery"))
                .filter(o -> o.getPackages().stream().allMatch(p -> p.getStatus().equalsIgnoreCase("delivered")))
                .forEach(o -> o.setStatus("delivered"));
    }
}
