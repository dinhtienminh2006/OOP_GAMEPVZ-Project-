public class FlagZombie extends Zombie {
    public FlagZombie(int x, int y) {
        super(x, y, 400, 1, 100);
        loadImage("/images/zombies/FlagZombie.gif");
    }
    @Override
    public void update(){
        this.updateChill();
        this.move();
    }
}
