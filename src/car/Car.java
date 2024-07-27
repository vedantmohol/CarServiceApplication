package car;
import java.util.ArrayList;
import java.util.List;

public class Car {
    private final List<String> carList = new ArrayList<>();

    public Car() {
        carList.add("suv");
        carList.add("sedan");
        carList.add("xuv");
        carList.add("creta");
        carList.add(("electric"));
    }
    public List<String> getCarList() {
        return carList;
    }
}
