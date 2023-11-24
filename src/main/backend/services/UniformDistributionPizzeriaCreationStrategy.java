package services;

import interfaces.IPizzeriaCreationStrategy;
import models.Checkout;
import models.Pizzaiolo;
import models.Pizzeria;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class UniformDistributionPizzeriaCreationStrategy implements IPizzeriaCreationStrategy {
    private List<Checkout> _checkouts;
    private List<Pizzaiolo> _pizzaiolos;
    private OrderGenerator _orderGenerator;
    private Duration _simulateDuration;

    public UniformDistributionPizzeriaCreationStrategy()
    {

    }
    public UniformDistributionPizzeriaCreationStrategy(List<Checkout> checkouts,
                                                       List<Pizzaiolo> pizzaiolos,
                                                       OrderGenerator orderGenerator,
                                                       Duration simulateDuration)
    {
        this._orderGenerator = orderGenerator;
        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._simulateDuration = simulateDuration;
    }
    @Override
    public Pizzeria createPizzeria() {
        return new Pizzeria(_checkouts,
                _pizzaiolos,
                _orderGenerator,
                _simulateDuration,
                this );
    }

    @Override
    public void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime, OrderGenerator orderGenerator) {
        Random random = new Random();

        while (System.currentTimeMillis() < endTime) {
            orderGenerator.Generate(); // Припускається, що OrderGenerator має статичний метод generate()

            long delayMillis = random.nextInt((int) (10000 - 5000 + 1)) + 5000; // Випадкова затримка від 5 до 10 секунд

            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
