import java.io.Serializable;

public class TestObject implements Serializable {
    private String name;

    public TestObject(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestObject{" + "name='" + name + '\'' + '}';
    }
}