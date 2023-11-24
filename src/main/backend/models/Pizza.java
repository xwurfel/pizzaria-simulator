import java.time.Duration;
import java.time.LocalTime;

public interface Pizza{
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
    public void changeStatus(PizzaStatus status);
    public PizzaStatus getStoppedAtStatus();
    public void setStoppedAtStatus(PizzaStatus stoppedAtStatus);
    public Duration getStoppedWithTimeLeft();
    public void setStoppedWithTimeLeft(Duration stoppedWithTimeLeft);
    // public List<Pizzaiolo> getPizzaioloList();
    public void doNextCookingStage();
    public void stopCookingStage();
}