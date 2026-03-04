package flight.reservation.payment.strategy;

import flight.reservation.order.FlightOrder;
import flight.reservation.payment.Paypal;

public class PaypalPaymentStrategy implements PaymentStrategy {
    private final String email;
    private final String password;

    public PaypalPaymentStrategy(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean pay(FlightOrder order) {
        if (email == null || password == null || !email.equals(Paypal.DATA_BASE.get(password))) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        System.out.println("Paying " + order.getPrice() + " using PayPal.");
        return true;
    }
}
