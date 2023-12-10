package com.teamworkcpp.pizzariasimulator.backend.models;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SimplePizza implements IPizza, IPizzaPrototype {
    private String name;
    private double price;
    private String id;
    private Duration minTimeDoughKneeding;
    private Duration minTimeFillingBeforeBaking;
    private Duration minTimeBaking;
    private Duration minTimeFillingAfterBaking;
    private Duration minTimePackaging;
    private IPizzaStatus stoppedAtStatus;
    private Duration stoppedWithTimeLeft;
    private LocalTime nextTime;
    private IPizzaStatus status;
    private List<Pizzaiolo> pizzaioloList = new ArrayList<>();
    private boolean isCookingNow = false;

    public SimplePizza(String name, double price, Duration minTimeDoughKneeding,
                       Duration minTimeFillingBeforeBaking, Duration minTimeBaking,
                       Duration minTimeFeelingAfterBaking, Duration minTimePackaging) {
        this.name = name;
        this.price = price;
        this.minTimeDoughKneeding = minTimeDoughKneeding;
        this.minTimeFillingBeforeBaking = minTimeFillingBeforeBaking;
        this.minTimeBaking = minTimeBaking;
        this.minTimeFillingAfterBaking = minTimeFeelingAfterBaking;
        this.minTimePackaging = minTimePackaging;

        this.status = new Waiting();
        this.status.setContext(this);
    }

    public void doNextCookingStage(){
        this.status.next();
    }

    public void stopCookingStage(){
        this.status.stop();
    }

    public boolean isCookingNow() {return this.isCookingNow;}

    public void setCookingNow(boolean isCookingNow) {this.isCookingNow = isCookingNow;}
    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public Duration getMinTimeDoughKneeding() {
        return minTimeDoughKneeding;
    }

    public Duration getMinTimeFillingBeforeBaking() {
        return minTimeFillingBeforeBaking;
    }

    public Duration getMinTimeBaking() {
        return minTimeBaking;
    }

    public Duration getMinTimeFillingAfterBaking() {
        return minTimeFillingAfterBaking;
    }

    public Duration getMinTimePackaging() {
        return minTimePackaging;
    }

    public void setNextTime(LocalTime nextTime) {
        this.nextTime = nextTime;
    }

    public LocalTime getNextTime() {
        return nextTime;
    }

    public void changeStatus(IPizzaStatus status) {
        this.status = status;
    }

    public IPizzaStatus getCurrentStatus() {return status;}

    public IPizzaStatus getStoppedAtStatus() {
        return stoppedAtStatus;
    }

    public List<Pizzaiolo> getPizzaioloList(){
         return pizzaioloList;
     }

    public void addPizzaiolo(Pizzaiolo pizzaiolo) {pizzaioloList.add(pizzaiolo);}

    public void setStoppedAtStatus(IPizzaStatus stoppedAtStatus) {
        this.stoppedAtStatus = stoppedAtStatus;
    }

    public Duration getStoppedWithTimeLeft() {
        return stoppedWithTimeLeft;
    }

    public void setStoppedWithTimeLeft(Duration stoppedWithTimeLeft) {
        this.stoppedWithTimeLeft = stoppedWithTimeLeft;
    }

    @Override
    public IPizzaPrototype clone() {
        return new SimplePizza(
                this.name,
                this.price,
                this.minTimeDoughKneeding,
                this.minTimeFillingBeforeBaking,
                this.minTimeBaking,
                this.minTimeFillingAfterBaking,
                this.minTimePackaging
        );
    }
}