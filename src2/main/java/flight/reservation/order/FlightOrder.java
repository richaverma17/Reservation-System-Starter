package flight.reservation.order;

import flight.reservation.Customer;
import flight.reservation.flight.ScheduledFlight;
import flight.reservation.payment.CreditCard;
import flight.reservation.payment.strategy.CreditCardPaymentStrategy;
import flight.reservation.payment.strategy.PaymentStrategy;
import flight.reservation.payment.strategy.PaypalPaymentStrategy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FlightOrder extends Order {
    private final List<ScheduledFlight> flights;
    static List<String> noFlyList = Arrays.asList("Peter", "Johannes");

    public FlightOrder(List<ScheduledFlight> flights) {
        this.flights = flights;
    }

    public static List<String> getNoFlyList() {
        return noFlyList;
    }

    public List<ScheduledFlight> getScheduledFlights() {
        return flights;
    }

    private boolean isOrderValid(Customer customer, List<String> passengerNames, List<ScheduledFlight> flights) {
        boolean valid = true;
        valid = valid && !noFlyList.contains(customer.getName());
        valid = valid && passengerNames.stream().noneMatch(passenger -> noFlyList.contains(passenger));
        valid = valid && flights.stream().allMatch(scheduledFlight -> scheduledFlight.getAvailableCapacity() >= passengerNames.size());
        return valid;
    }

    public boolean processOrderWithCreditCardDetail(String number, Date expirationDate, String cvv) throws IllegalStateException {
        CreditCard creditCard = new CreditCard(number, expirationDate, cvv);
        return processOrderWithCreditCard(creditCard);
    }

    public boolean processOrderWithCreditCard(CreditCard creditCard) throws IllegalStateException {
        return processPayment(new CreditCardPaymentStrategy(creditCard));
    }

    public boolean processOrderWithPayPal(String email, String password) throws IllegalStateException {
        return processPayment(new PaypalPaymentStrategy(email, password));
    }

    public boolean payWithCreditCard(CreditCard card, double amount) throws IllegalStateException {
        return processPayment(new CreditCardPaymentStrategy(card));
    }

    public boolean payWithPayPal(String email, String password, double amount) throws IllegalStateException {
        return processPayment(new PaypalPaymentStrategy(email, password));
    }

    public boolean processPayment(PaymentStrategy strategy) {
        if (isClosed()) {
            // Payment is already proceeded
            return true;
        }
        boolean isPaid = strategy.pay(this);
        if (isPaid) {
            this.setClosed();
        }
        return isPaid;
    }
}
