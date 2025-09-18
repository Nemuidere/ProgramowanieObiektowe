package ProgramowanieObiektowe.P2;

public class Manga extends Entry{
    private int volumes, chapters;
    private String author;

    public void setVolumes(int volumes){
        this.volumes = volumes;
    }
    public int getVolumes(){
        return volumes;
    }

    public void setChapters(int chapters){
        this.chapters = chapters;
    }
    public int getChapters(){
        return chapters;
    }

    public void getAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return author;
    }

    public void describe(){
        System.out.printf("Name: %s; Author: %s; Release year: %d; Score: %.1f; Volumes: %d; Chapters: %d",super.getTitle(),getAuthor(),super.getYear(),super.getScore(),getVolumes(),getChapters());
    }
    // uh?
}
