package com.teamworkcpp.pizzariasimulator.backend.models;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class Packaging implements IPizzaStatus{
    IPizza context;
    @Override
    public void next() {
        IPizzaStatus status = new Done();
        status.setContext(context);
        context.changeStatus(status);
    }

    @Override
    public void stop() {
        IPizzaStatus status = new Waiting();
        status.setContext(context);
        context.changeStatus(status);
        context.setStoppedAtStatus(this);
        context.setStoppedWithTimeLeft(Duration.between(LocalTime.now(), context.getNextTime()));
    }

    @Override
    public void setContext(IPizza p) {
        this.context = p;
    }

    @Override
    public String getStatusName() {
        return "Packaging";
    }
}
