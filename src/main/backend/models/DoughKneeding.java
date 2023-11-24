import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class DoughKneeding implements PizzaStatus{
    Pizza context;
    @Override
    public void next() {
        PizzaStatus status = new FillingBeforeBaking();
        status.setContext(context);
        context.changeStatus(status);
        Random r = new Random();
        var timeAdd = r.nextInt(0, (int)context.getMinTimeFillingBeforeBaking().toSeconds() / 5);
        var time = Duration.ofSeconds(context.getMinTimeFillingBeforeBaking().toSeconds() + timeAdd);

        context.setNextTime(
                LocalTime.now().plus(time)
        );
    }

    @Override
    public void stop() {
        PizzaStatus status = new Waiting();
        status.setContext(context);
        context.changeStatus(status);
        context.setStoppedAtStatus(this);
        context.setStoppedWithTimeLeft(Duration.between(LocalTime.now(), context.getNextTime()));
    }

    @Override
    public void setContext(Pizza p) {
        this.context = p;
    }

    @Override
    public String getStatusName() {
        return "Dough Kneeding";
    }
}
