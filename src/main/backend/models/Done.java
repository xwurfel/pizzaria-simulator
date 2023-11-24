public class Done implements PizzaStatus{
    Pizza context;
    @Override
    public void next() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setContext(Pizza p) {
        this.context = p;
    }

    @Override
    public String getStatusName() {
        return "Done";
    }
}
