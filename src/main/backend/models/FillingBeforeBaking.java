package models;
import backend.interfaces.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class FillingBeforeBaking implements IPizzaStatus{
    IPizza context;
    @Override
    public void next() {
        IPizzaStatus status = new Baking();
        status.setContext(context);
        context.changeStatus(status);
        Random r = new Random();
        var timeAdd = r.nextInt(0, (int)context.getMinTimeBaking().toSeconds() / 5);
        var time = Duration.ofSeconds(context.getMinTimeBaking().toSeconds() + timeAdd);

        context.setNextTime(
                LocalTime.now().plus(time)
        );
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
        return "Filling Before Baking";
    }
}
