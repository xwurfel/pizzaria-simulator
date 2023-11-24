package services;
import backend.interfaces.*;
import java.time.Duration;
import java.time.LocalTime;

public class PizzaBaseDecorator implements IPizza{
    private IPizza wrappee;

    public PizzaBaseDecorator(IPizza wrappee) {
        this.wrappee = wrappee;
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
    // public List<Pizzaiolo> getPizzaioloList(){
    //  return wrappee.getPizzaioloList();
    //}
}
