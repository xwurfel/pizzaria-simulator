package com.teamworkcpp.pizzariasimulator.backend.services;
import com.teamworkcpp.pizzariasimulator.backend.interfaces.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PizzaDecoratorDoubleCheese extends PizzaBaseDecorator{
    private double cheesePrice;
    public PizzaDecoratorDoubleCheese(IPizza wrappee, double cheesePrice) {
        super(wrappee);
        this.cheesePrice = cheesePrice;
    }

    @Override
    public double getPrice() {
        return super.getPrice() + cheesePrice;
    }

    @Override
    public String getName() {
        String name = super.getName();
        Pattern p = Pattern.compile("(?<=Cheese x)\\d+");
        Matcher m = p.matcher(name);
        if(m.find()){
            String curentPortionStr = name.substring(m.start(), m.end());
            int curentPortion = Integer.valueOf(curentPortionStr);
            ++curentPortion;
            return name.substring(0,m.start()) + String.valueOf(curentPortion) + name.substring(m.end());
        }else{
            return super.getName() + " Cheese x2";
        }
    }
}
