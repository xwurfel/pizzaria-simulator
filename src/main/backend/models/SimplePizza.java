import java.time.Duration;
import java.time.LocalTime;

public class SimplePizza implements Pizza, PizzaPrototype {
    private String name;
    private double price;
    private String id;
    private Duration minTimeDoughKneeding;

    public SimplePizza(String id, String name, double price, Duration minTimeDoughKneeding,
                       Duration minTimeFillingBeforeBaking, Duration minTimeBaking,
                       Duration minTimeFeelingAfterBaking, Duration minTimePackaging) {
        this.name = name;
        this.id = id;
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

    public String getName() {
        return name;
    }

    @Override
    public PizzaPrototype clone() {
        return new SimplePizza(
                this.id,
                this.name,
                this.price,
                this.minTimeDoughKneeding,
                this.minTimeFillingBeforeBaking,
                this.minTimeBaking,
                this.minTimeFillingAfterBaking,
                this.minTimePackaging
        );
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

    private Duration minTimeFillingBeforeBaking;
    private Duration minTimeBaking;
    private Duration minTimeFillingAfterBaking;
    private Duration minTimePackaging;

    public void setNextTime(LocalTime nextTime) {
        this.nextTime = nextTime;
    }

    public LocalTime getNextTime() {
        return nextTime;
    }
    public void changeStatus(PizzaStatus status) {
        this.status = status;
    }

    private LocalTime nextTime;
    // private List<Pizzaiolo> pizzaioloList = new ArrayList<Pizzaiolo>();
    // public List<Pizzaiolo> getPizzaioloList(){
    //  return pizzaioloList;
    //}
    private PizzaStatus status;

    public PizzaStatus getStoppedAtStatus() {
        return stoppedAtStatus;
    }

    public void setStoppedAtStatus(PizzaStatus stoppedAtStatus) {
        this.stoppedAtStatus = stoppedAtStatus;
    }

    public Duration getStoppedWithTimeLeft() {
        return stoppedWithTimeLeft;
    }

    public void setStoppedWithTimeLeft(Duration stoppedWithTimeLeft) {
        this.stoppedWithTimeLeft = stoppedWithTimeLeft;
    }

    private PizzaStatus stoppedAtStatus;
    private Duration stoppedWithTimeLeft;
}