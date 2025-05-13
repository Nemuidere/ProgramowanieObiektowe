package ProgramowanieObiektowe;

public class point {
    public double x, y;
    public point (double x, double y) {
    this.x = x;
    this.y = y;
    }

    @Override
    public String toString(){
        return "x = " + x + "; y = " + y;
    }

    }
