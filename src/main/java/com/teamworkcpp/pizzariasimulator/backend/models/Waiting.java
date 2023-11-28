package com.teamworkcpp.pizzariasimulator.backend.models;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class Waiting implements IPizzaStatus{
    IPizza context;
    @Override
    public void next() {
        if(context.getStoppedAtStatus() == null) {
            IPizzaStatus status = new DoughKneeding();
            status.setContext(context);
            context.changeStatus(status);
            Random r = new Random();
            var timeAdd = r.nextInt(0, (int) context.getMinTimeDoughKneeding().toSeconds() / 5);
            var time = Duration.ofSeconds(context.getMinTimeDoughKneeding().toSeconds() + timeAdd);

            context.setNextTime(
                    LocalTime.now().plus(time)
            );
        }else{
            context.changeStatus(context.getStoppedAtStatus());
            context.setNextTime(
                    LocalTime.now().plus(context.getStoppedWithTimeLeft())
            );
            context.setStoppedAtStatus(null);
            context.setStoppedWithTimeLeft(null);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void setContext(IPizza p) {
        this.context = p;
    }

    @Override
    public String getStatusName() {
        return "Waiting";
    }
}
