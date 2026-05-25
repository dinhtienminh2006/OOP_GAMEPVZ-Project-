public class FootballZombie extends Zombie {
    public FootballZombie(int x, int y) {
        super(x, y, 600, 3, 100);
        loadImage("/images/zombies/FootballZombie.gif");
    }

    @Override
    public void update(){
        this.updateChill();
        this.move();
    }
}
