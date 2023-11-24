import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PizzaDecoratorDoubleMeat extends PizzaBaseDecorator{
    private double meatPrice;
    public PizzaDecoratorDoubleMeat(Pizza wrappee, double meatPrice) {
        super(wrappee);
        this.meatPrice = meatPrice;
    }

    @Override
    public double getPrice() {
        return super.getPrice() + meatPrice;
    }

    @Override
    public String getName() {
        String name = super.getName();
        Pattern p = Pattern.compile("(?<=Meat x)\\d+");
        Matcher m = p.matcher(name);
        if(m.find()){
            String curentPortionStr = name.substring(m.start(), m.end());
            int curentPortion = Integer.valueOf(curentPortionStr);
            ++curentPortion;
            return name.substring(0,m.start()) + String.valueOf(curentPortion) + name.substring(m.end());
        }else{
            return super.getName() + " Meat x2";
        }
    }
}