public class PeaShooter extends Pea {
    public PeaShooter(int x, int y) {
        super(x, y, 300, 100, 1500); // 1500ms = 1.5 giây bắn 1 lần
        getImage("/images/plants/Peashooter.gif");
    }

    @Override
    public Projectile shoot() {
        if (isReadyToShoot()) {
            setLastShootTime(System.currentTimeMillis()); // Reset đồng hồ bằng giờ thực hiện tại
            setReadyToShoot(false);
            return new Bullet(this.getX(), this.getY());
        }
        return null;
    }
}