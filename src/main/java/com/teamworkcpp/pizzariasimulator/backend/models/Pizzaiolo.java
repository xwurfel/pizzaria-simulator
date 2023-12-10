package com.teamworkcpp.pizzariasimulator.backend.models;

import com.teamworkcpp.pizzariasimulator.backend.helpers.Logger;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Pizzaiolo {
    private final int _id;
    private boolean _isAvailable = true;
    private Duration _delay;
    private LocalTime _availableTime = LocalTime.now();
    private boolean _isOnTechnicalStop = false;
    private IPizza pizza;
    private final Random r;
    public Pizzaiolo(int id, boolean isAvailable, Duration delay) {
        this._id = id;
        this._isAvailable = isAvailable;
        this._delay = delay;
        r =  new Random((long) LocalTime.now().getNano() % 1000 + _id * 120L);

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
        return _id;
    }

    public boolean isAvailable()
    {
        return _isAvailable;
    }

    public boolean isOnTechnicalStop()
    {
        return _isOnTechnicalStop;
    }

    public void CheckAvailable()
    {
        if(_isAvailable)
        {

            if(r.nextInt(1000) == r.nextInt(100))
            {

                try {
                    Logger.log("Pizziolo " + _id + " on technical stop 1");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                _delay = Duration.ofMillis(0);
                _delay = _delay.plusMillis(r.nextInt(15000) + 15000);
                _availableTime = LocalTime.now().plusSeconds(_delay.getSeconds());

                _isAvailable = false;
                _isOnTechnicalStop = true;
            }
            return;
        }

        if (System.currentTimeMillis() > _availableTime
                .atDate(LocalDate.now())
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli())
        {
            _isAvailable = true;
            _isOnTechnicalStop = false;
            if(pizza != null) {
                pizza.setCookingNow(false);
                this.pizza = null;
            }

            return;
        }

        if (!_isOnTechnicalStop) {
            if(r.nextInt(1000) == r.nextInt(100))
            {
                try {
                    Logger.log("Pizzaiolo " + _id + " on technical stop 2");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                _delay = Duration.ofMillis(0);
                _delay = _delay.plusMillis(r.nextInt(15000) + 15000);
                _availableTime = LocalTime.now().plusSeconds(_delay.getSeconds());

                _isAvailable = false;
                _isOnTechnicalStop = true;
            }
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
            Logger.log("Pizziolo " + _id + " reserved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void SetReserve(Duration duration)
    {
        _delay = duration;
        _availableTime = LocalTime.now().plus(duration);
        _isAvailable = false;
    }

    public void cook(IPizza pizza) {
        pizza.doNextCookingStage();
    }
}