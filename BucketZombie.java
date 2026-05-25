public class BucketZombie extends Zombie{
    public BucketZombie(int x, int y) {
        super(x, y, 500, 1, 100);
        loadImage("/images/zombies/BucketheadZombie.gif");
    }

    @Override
    public void update(){
        this.updateChill();
        this.move();
    }
}
