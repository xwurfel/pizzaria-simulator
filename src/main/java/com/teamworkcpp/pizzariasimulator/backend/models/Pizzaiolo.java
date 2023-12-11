package com.teamworkcpp.pizzariasimulator.backend.models;

import com.teamworkcpp.pizzariasimulator.backend.helpers.Logger;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Random;

public class Pizzaiolo {
    private final int id;
    private boolean isAvailable = true;
    private Duration delay;
    private LocalTime availableTime = LocalTime.now();
    private boolean isOnTechnicalStop = false;
    private IPizza pizza;
    private final Random r;
    public Pizzaiolo(int id, boolean isAvailable, Duration delay) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.delay = delay;
        r =  new Random((long) LocalTime.now().getNano() % 10000 * this.id * 1200L);

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPizza(IPizza pizza)
    {
        this.pizza = pizza;
        pizza.setCookingNow(true);
    }

    public int GetId()
    {
        return id;
    }

    public boolean isAvailable()
    {
        return isAvailable;
    }

    public boolean isOnTechnicalStop()
    {
        return isOnTechnicalStop;
    }

    public void CheckAvailable()
    {
        if(isAvailable)
        {
            if(r.nextInt(1000) == r.nextInt(100)*r.nextInt(10))
            {

                try {
                    Logger.log("Pizziolo " + id + " on technical stop 1");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                delay = Duration.ofMillis(0);
                delay = delay.plusMillis(r.nextInt(15000) + 15000);
                availableTime = LocalTime.now().plusSeconds(delay.getSeconds());

                isAvailable = false;
                isOnTechnicalStop = true;
            }
            return;
        }

        if (System.currentTimeMillis() > availableTime
                .atDate(LocalDate.now())
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli())
        {
            isAvailable = true;
            isOnTechnicalStop = false;
            if(pizza != null) {
                pizza.setCookingNow(false);
                this.pizza = null;
            }

            return;
        }
    }

    public void ReservePizzaiolo()
    {

        switch (pizza.getCurrentStatus().getStatusName())
        {
            case "Dough Kneeding":
                SetReserve(pizza.getMinTimeDoughKneeding());
                break;
            case "Filling After Baking":
                SetReserve(pizza.getMinTimeFillingAfterBaking());
                break;
            case "Filling Before Baking":
                SetReserve(pizza.getMinTimeFillingBeforeBaking());
                break;
            case "Packaging":
                SetReserve(pizza.getMinTimePackaging());
                break;
            case "Baking":
                SetReserve(pizza.getMinTimeBaking());
                break;
            default:
                SetReserve(Duration.ofMillis(5000));
        }

        try {
            Logger.log("Pizziolo " + id + " reserved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void SetReserve(Duration duration)
    {
        delay = duration;
        availableTime = LocalTime.now().plus(duration);
        isAvailable = false;
    }

    public void cook(IPizza pizza) {
        pizza.doNextCookingStage();
    }
}