package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.entities.OrderItem;
import com.geekbrains.geekspring.entities.ShoppingCart;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OrderStatusService orderStatusService;


    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }




    @Transactional
    public Order makeOrder(ShoppingCart cart, User user) {

        // создаем новый заказ
        Order order = new Order();

        // заполняем поле order у всех товаров в корзине
        cart.getItems().forEach(item -> item.setOrder(order));

        // заполняем поле с коллекцией товаров у заказа
        order.setOrderItems(cart.getItems());

        // уставливаем стоимость заказа, пользователя и статус
        order.setPrice(cart.getTotalCost());
        order.setUser(user);
        order.setStatus(orderStatusService.getStatusById(1L)); // начальный статус "Сформирован"

        // если это свежий заказ, то устанаиваем ему флаг confirmed в false
        if (order.getConfirmed() == null) {
            order.setConfirmed(false);
        }

        return order;
    }

    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).get();
    }

    public Order saveOrder(Order order) {

        // если это новый заказ, то перед сохраняем меняем ему флаг confirmed на true
        if (!order.getConfirmed()) order.setConfirmed(true);

        // и сохраняем в БД
        orderRepository.save(order);
        return order;
    }

    public Order changeOrderStatus(Order order, Long statusId) {
        order.setStatus(orderStatusService.getStatusById(statusId));
        return saveOrder(order);
    }
}
