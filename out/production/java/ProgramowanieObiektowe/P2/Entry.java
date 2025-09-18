package ProgramowanieObiektowe.P2;

public abstract class Entry {
    private String title;
    private int year;
    private float score;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setYear(int year){
        this.year = year;
    }
    public int getYear(){
        return year;
    }

    public void setScore(float score){
        this.score = score;
    }
    public float getScore(){
        return score;
    }

}
