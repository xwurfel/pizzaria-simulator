package com.teamworkcpp.pizzariasimulator.backend.models;

import com.teamworkcpp.pizzariasimulator.backend.interfaces.IPizza;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Random;

public class Pizzaiolo {
    private final int _id;
    private boolean _isAvailable;
    private Duration _delay;
    private LocalTime _availableTime;
    private boolean _isOnTechnicalStop;

    public Pizzaiolo(int id, boolean isAvailable, Duration delay) {
        this._id = id;
        this._isAvailable = isAvailable;
        this._delay = delay;
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
            if(r.nextInt(100) < 5)
            {
                _delay = Duration.ofMillis(0);
                _delay.plusMillis(r.nextInt(15000) + 15000);
                LocalTime currentTime = LocalTime.now();
                _availableTime = currentTime.plus(_delay);
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
            return;
        }

        if (!_isOnTechnicalStop) {
            if(r.nextInt(100) < 5)
            {
                _delay = Duration.ofMillis(0);
                _delay.plusMillis(r.nextInt(15000) + 15000);
                LocalTime currentTime = LocalTime.now();
                _availableTime = currentTime.plus(_delay);
                _isAvailable = false;
                _isOnTechnicalStop = true;
            }
        }
    }

    public void ReservePizzaiolo(long duration)
    {
        _delay = Duration.ofMillis(duration);
        _isAvailable = false;
    }

    public void cook(IPizza pizza) {
        pizza.doNextCookingStage();
    }
}