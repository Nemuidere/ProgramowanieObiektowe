package ProgramowanieObiektowe.P2;

public class Library {
    public static void main(String[] args) {
        Anime a1 = new Anime();
        a1.setStudio("MAPPA");
        a1.setTitle("Jujutsu Kaisen");
        a1.setYear(2020);
        a1.describe();
    }
}
