package flight.reservation.payment.strategy;

import flight.reservation.order.FlightOrder;

public interface PaymentStrategy {
    boolean pay(FlightOrder order);
}
