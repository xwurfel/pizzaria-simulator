public interface PizzaStatus {
    public void next();
    public void stop();
    public void setContext(Pizza p);
    public String getStatusName();
}
