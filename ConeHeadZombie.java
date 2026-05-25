public class ConeHeadZombie extends Zombie {
    public ConeHeadZombie(int x, int y) {
        super(x, y, 300, 1, 100); // Máu 560, Tốc độ 1, Sát thương 100
        loadImage("/images/zombies/ConeheadZombie.gif");
    }

    @Override
    public void update() {
        this.updateChill(); 
        this.move();
    }
}