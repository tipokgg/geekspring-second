package com.geekbrains.geekspring.services;


import com.geekbrains.geekspring.entities.Order;
import com.geekbrains.geekspring.entities.OrderStatus;
import com.geekbrains.geekspring.entities.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderLoggingAspect {

    private OrderStatusService orderStatusService;

    @Autowired
    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Before("execution(public * com.geekbrains.geekspring.services.OrderService.makeOrder(..))")
    public void beforeMakerOrder(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        User user = (User) args[1]; // получаем объект пользователя, который вызывает создание заказа

        LOGGER.info(String.format("Пользователь %s начинает создание заказа", user.getUserName()));
    }

    @After("execution(public * com.geekbrains.geekspring.services.OrderService.makeOrder(..))")
    public void afterMakerOrder(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        User user = (User) args[1]; // получаем объект пользователя, который вызывает создание заказа

        LOGGER.info(String.format("Пользователь %s завершает предварительное создание заказа", user.getUserName()));
    }

    @AfterReturning(
            pointcut = "execution(public * com.geekbrains.geekspring.services.OrderService.saveOrder(..))",
            returning = "order")
    public void afterSaveOrder(JoinPoint joinPoint, Order order) {
        LOGGER.info(String.format("Пользователь %s подверждает создание заказа №%s. Заказ сохранён в базу", order.getUser().getUserName(), order.getId()));
        ;
    }

    @After("execution(public * com.geekbrains.geekspring.services.OrderService.changeOrderStatus(..))")
    public void afterChangeStatus(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Order order = (Order) args[0]; // получаем объект заказа
        OrderStatus orderStatus = orderStatusService.getStatusById((Long) args[1]); // получаем статус заказа, на который меняем

        LOGGER.info(String.format("Статус заказа №%s изменён на %s", order.getId(), orderStatus.getTitle()));
    }

    @Before("execution(public * com.geekbrains.geekspring.services.OrderService.findById(..))")
    public void beforeFindById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0]; // получаем айдишник по которому выполняется поиск

        LOGGER.info(String.format("Выполняется поиск заказа №%s", id));
    }

    @AfterReturning(
            pointcut = "execution(public * com.geekbrains.geekspring.services.OrderService.findById(..))",
            returning = "order")
    public void afterFindById(JoinPoint joinPoint, Order order) {
        if (order != null) LOGGER.info(String.format("Найден заказ №%s", order.getId()));
    }

    @After("execution(public * com.geekbrains.geekspring.services.OrderService.getAllOrders())")
    public void afterFindAllOrders(JoinPoint joinPoint) {
        LOGGER.info("Выполняется поиск всех заказов");
    }
}
