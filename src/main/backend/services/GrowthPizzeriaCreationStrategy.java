package services;

import interfaces.IPizzeriaCreationStrategy;
import models.Checkout;
import models.Pizzaiolo;
import models.Pizzeria;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class GrowthPizzeriaCreationStrategy implements IPizzeriaCreationStrategy {
    private List<Checkout> _checkouts;
    private List<Pizzaiolo> _pizzaiolos;
    private OrderGenerator _orderGenerator;
    private Duration _simulateDuration;

    public GrowthPizzeriaCreationStrategy()
    {

    }
    public GrowthPizzeriaCreationStrategy(List<Checkout> checkouts,
                                            List<Pizzaiolo> pizzaiolos,
                                            OrderGenerator orderGenerator,
                                            Duration _simulateDuration)
    {
        this._orderGenerator = orderGenerator;
        this._checkouts = checkouts;
        this._pizzaiolos = pizzaiolos;
        this._simulateDuration = _simulateDuration;
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
    public void generateOrdersWithDelay(Pizzeria pizzeria, long startTime, long endTime, OrderGenerator orderGenerator)
    {
        long firstThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.2);
        long secondThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.5);
        long thirdThreshold = startTime + Math.round(_simulateDuration.toMillis() * 0.75);

        while (System.currentTimeMillis() < endTime) {
            long currentTime = System.currentTimeMillis();
            Random random = new Random();
            if (currentTime < firstThreshold) {
                orderGenerator.Generate();
                long delayMillis = random.nextInt((int) (10000 - 5000 + 1)) + 5000;

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (currentTime < secondThreshold) {
                orderGenerator.Generate();
                long delayMillis = random.nextInt((int) (8000 - 4000 + 1)) + 4000;

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (currentTime < thirdThreshold) {
                orderGenerator.Generate();
                long delayMillis = random.nextInt((int) (6000 - 3000 + 1)) + 3000;

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                orderGenerator.Generate();
                long delayMillis = random.nextInt((int) (4500 - 3000 + 1)) + 3000;

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
