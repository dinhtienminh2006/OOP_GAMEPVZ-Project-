public class IcePeaShooter extends Pea {
    public IcePeaShooter(int x, int y) {
        super(x, y, 200, 175, 1500); // Giá mua 175, tốc độ bắn 1.5 giây/viên
        getImage("/images/plants/freezepeashooter.gif");
    }

    @Override
    public Projectile shoot() {
        if (isReadyToShoot()) {
            setLastShootTime(System.currentTimeMillis()); // Reset đồng hồ bằng giờ thực hiện tại
            setReadyToShoot(false);
            return new IceBullet(this.getX(), this.getY());
        }
        return null;
    }
}