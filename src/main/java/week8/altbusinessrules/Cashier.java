package week8.altbusinessrules;

public class Cashier {
    private double discount;
    private double payment;

    public double getDiscount() {
        return discount;
    }

    @SuppressWarnings("unused") // Seda meetodit kasutatakse reeglites
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPayment() {
        return payment;
    }
    @SuppressWarnings("unused") // Seda meetodit kasutatakse reeglites
    public void setPayment(double payment) {
        this.payment = payment;
    }
}
