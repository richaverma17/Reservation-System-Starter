package flight.reservation.payment.strategy;

import flight.reservation.order.FlightOrder;
import flight.reservation.payment.CreditCard;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    private final CreditCard card;

    public CreditCardPaymentStrategy(CreditCard card) {
        this.card = card;
    }

    @Override
    public boolean pay(FlightOrder order) {
        if (card == null || !card.isValid()) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        System.out.println("Paying " + order.getPrice() + " using Credit Card.");
        double remainingAmount = card.getAmount() - order.getPrice();
        if (remainingAmount < 0) {
            System.out.printf("Card limit reached - Balance: %f%n", remainingAmount);
            throw new IllegalStateException("Card limit reached");
        }
        card.setAmount(remainingAmount);
        return true;
    }
}
