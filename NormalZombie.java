public class NormalZombie extends Zombie {
    public NormalZombie(int x, int y) {
        super(x, y, 200, 1, 100); // Máu 200, Tốc độ 1, Sát thương 100
        loadImage("/images/zombies/NormalZombie.gif");
    }

    @Override
    public void update() {
       this.updateChill(); // Phải có dòng này để nó tự rã đông
       this.move();
    }
}