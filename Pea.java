public abstract class Pea extends Plant {
    protected final long shootCooldown; // Đổi sang long
    protected long lastShootTime;      // Ghi nhớ mốc thời gian bắn cuối
    protected boolean isReadyToShoot;

    public Pea(int x, int y, int hp, int cost, long cooldownMs) {
        super(x, y, hp, cost);
        this.shootCooldown = cooldownMs;
        this.lastShootTime = System.currentTimeMillis(); // Tính giờ từ lúc trồng xuống
        this.isReadyToShoot = false;
    }

    @Override
    public void update() {
        if (!isReadyToShoot) {
            // Kiểm tra xem khoảng cách thời gian thực đã đủ chưa
            if (System.currentTimeMillis() - lastShootTime >= shootCooldown) {
                isReadyToShoot = true;
            }
        }
    }

    public abstract Projectile shoot();

//----------Getters and setters----------
    public long getShootCooldown() { return shootCooldown; }
    public boolean isReadyToShoot() { return isReadyToShoot; }
    public void setReadyToShoot(boolean readyToShoot) { this.isReadyToShoot = readyToShoot; }
    public void setLastShootTime(long lastShootTime) { this.lastShootTime = lastShootTime; }
}