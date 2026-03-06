package ru.coffee.loyalty.service;

public interface PointTransactionSender {
    void sendPointsDelta(Integer delta);
}
