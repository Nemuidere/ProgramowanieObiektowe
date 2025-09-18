package ProgramowanieObiektowe.P2;

public class Anime extends Entry{
    private int episodes;
    private String studio;

    public void setStudio(String studio){
        this.studio = studio;
    }
    public String getStudio(){
        return studio;
    }

    public void setEpisodes(int episodes){
        this.episodes = episodes;
    }
    public int getEpisodes(){
        return episodes;
    }

    public void describe(){
        System.out.printf("Name: %s; Studio: %s; Release year: %d; Score: %.1f; Episodes: %d ",super.getTitle(),getStudio(),super.getYear(),super.getScore(),getEpisodes());
    }
}