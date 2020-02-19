package RailWay;

public class Son extends Base implements testinter{
    public Son(int num, int num2) {
        super(num);
        this.num2 = num2;
    }
    public int num2;

    @Override
    public void f() {
        System.out.println(num2);
    }
}
