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

public class Pizzaiolo {
    private final int _id;
    private boolean _isAvailable = true;
    private Duration _delay;
    private LocalTime _availableTime = LocalTime.now();
    private boolean _isOnTechnicalStop = false;
    private IPizza pizza;

    public Pizzaiolo(int id, boolean isAvailable, Duration delay) {
        this._id = id;
        this._isAvailable = isAvailable;
        this._delay = delay;
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
        Random r =  new Random();
        if(_isAvailable)
        {
            /*if(r.nextInt(1000) == 1)
            {

                try {
                    Logger.log("Pizziolo " + _id + " on technical stop 1");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                _delay = Duration.ofMillis(0);
                _delay = _delay.plusMillis(r.nextInt(15000) + 15000);
                _availableTime = LocalTime.now().plus(_delay.getSeconds(), ChronoUnit.SECONDS);

                _isAvailable = false;
                _isOnTechnicalStop = true;
            }*/
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
          /*  if(r.nextInt(1000) == 1)
            {
                try {
                    Logger.log("Pizziolo " + _id + " on technical stop 2");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                _delay = Duration.ofMillis(0);
                _delay = _delay.plusMillis(r.nextInt(15000) + 15000);
                _availableTime = LocalTime.now().plus(_delay.getSeconds(), ChronoUnit.SECONDS);

                _isAvailable = false;
                _isOnTechnicalStop = true;
            }*/
        }
    }

    public void ReservePizzaiolo(long duration)
    {
        try {
            Logger.log("Pizziolo " + _id + " reserved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        _delay = Duration.ofMillis(duration);
        _availableTime = LocalTime.now().plusSeconds(duration / 1000);
        _isAvailable = false;
    }

    public void cook(IPizza pizza) {
        pizza.doNextCookingStage();
    }
}