public abstract class Projectile extends GameObject {
    protected int speed;
    protected int damage;
    protected boolean isActive;

    public Projectile(int x, int y, int speed, int damage) {
        super(x, y);
        this.speed = speed;// Chỉnh sửa tốc độ của đạn sau
        this.damage = damage;
        this.isActive = true;
    }

    @Override
    public void update() {
        if (isActive) {
            this.x += speed;
            if (this.x > 1000) {
                this.isActive = false;
            }
        }
    }

    public abstract void hit(Zombie target);

    // ----------Getters and setters----------
    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
