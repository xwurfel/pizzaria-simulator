package com.teamworkcpp.pizzariasimulator.backend.services;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.*;
import com.teamworkcpp.pizzariasimulator.backend.models.Pizzaiolo;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class PizzaBaseDecorator implements IPizza{
    private IPizza wrappee;

    public PizzaBaseDecorator(IPizza wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public void setId(String id) {
        wrappee.setId(id);
    }

    public String getName() {
        return wrappee.getName();
    }

    public double getPrice() {
        return wrappee.getPrice();
    }

    public String getId() {
        return wrappee.getId();
    }
    public LocalTime getNextTime(){
        return wrappee.getNextTime();
    }
    public void setNextTime(LocalTime nextTime){
        wrappee.setNextTime(nextTime);
    }
    public void changeStatus(IPizzaStatus status){
        wrappee.changeStatus(status);
    }
    public IPizzaStatus getStoppedAtStatus() {
        return wrappee.getStoppedAtStatus();
    }

    public void setStoppedAtStatus(IPizzaStatus stoppedAtStatus) {
        wrappee.setStoppedAtStatus(stoppedAtStatus);
    }

    public Duration getStoppedWithTimeLeft() {
        return wrappee.getStoppedWithTimeLeft();
    }

    public void setStoppedWithTimeLeft(Duration stoppedWithTimeLeft) {
        wrappee.setStoppedWithTimeLeft(stoppedWithTimeLeft);
    }

    public Duration getMinTimeDoughKneeding() {
        return wrappee.getMinTimeDoughKneeding();
    }

    public Duration getMinTimeFillingBeforeBaking() {
        return wrappee.getMinTimeFillingBeforeBaking();
    }

    public Duration getMinTimeBaking() {
        return wrappee.getMinTimeBaking();
    }

    public Duration getMinTimeFillingAfterBaking() {
        return wrappee.getMinTimeFillingAfterBaking();
    }

    public Duration getMinTimePackaging() {
        return wrappee.getMinTimePackaging();
    }
    public void doNextCookingStage(){
        wrappee.doNextCookingStage();
    }
    public void stopCookingStage(){
        wrappee.stopCookingStage();
    }

    public List<Pizzaiolo> getPizzaioloList(){
        return wrappee.getPizzaioloList();
    }

    @Override
    public void addPizzaiolo(Pizzaiolo pizzaiolo) {
        return;
    }

    @Override
    public boolean isCookingNow() {
        return wrappee.isCookingNow();
    }

    @Override
    public void setCookingNow(boolean isCookingNow) {
        wrappee.setCookingNow(isCookingNow);
    }

    @Override
    public IPizzaStatus getCurrentStatus() {
        return null;
    }

}
