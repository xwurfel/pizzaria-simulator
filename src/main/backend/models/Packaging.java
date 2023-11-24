import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class Packaging implements PizzaStatus{
    Pizza context;
    @Override
    public void next() {
        PizzaStatus status = new Done();
        status.setContext(context);
        context.changeStatus(status);
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
        return "Packaging";
    }
}
