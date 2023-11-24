package interfaces;
import models.Pizzeria;
import services.OrderGenerator;

public interface IPizzeriaCreationStrategy {
    Pizzeria createPizzeria();
    void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime,  OrderGenerator orderGenerator);
}
