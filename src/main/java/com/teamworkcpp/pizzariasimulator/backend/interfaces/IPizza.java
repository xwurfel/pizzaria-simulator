package com.teamworkcpp.pizzariasimulator.backend.interfaces;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public interface IPizza {
    public String getName();
    public double getPrice();
    public String getId();
    public Duration getMinTimeDoughKneeding();
    public Duration getMinTimeFillingBeforeBaking();
    public Duration getMinTimeBaking();
    public Duration getMinTimeFillingAfterBaking();
    public Duration getMinTimePackaging();
    public LocalTime getNextTime();
    public void setNextTime(LocalTime nextTime);
    public void changeStatus(IPizzaStatus status);
    public IPizzaStatus getStoppedAtStatus();
    public void setStoppedAtStatus(IPizzaStatus stoppedAtStatus);
    public Duration getStoppedWithTimeLeft();
    public void setStoppedWithTimeLeft(Duration stoppedWithTimeLeft);
    // public List<Pizzaiolo> getPizzaioloList();
    public void doNextCookingStage();
    public void stopCookingStage();
    public IPizzaStatus getCurrentStatus();
    public List<Pizzaiolo> getPizzaioloList();
    public void addPizzaiolo(Pizzaiolo pizzaiolo);
}