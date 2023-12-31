package com.teamworkcpp.pizzariasimulator.backend.models;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.*;
public class Done implements IPizzaStatus{
    IPizza context;

    @Override
    public void next() { }

    @Override
    public void stop() { }

    @Override
    public void setContext(IPizza p) {
        this.context = p;
    }

    @Override
    public String getStatusName() {
        return "Done";
    }
}
