public class Bullet extends Projectile {
    public Bullet(int x, int y) {
        super(x, y, 5, 20);
    }//

    @Override
    public void hit(Zombie target) {
        if (isActive() && target != null && !target.isDead()) {
            target.takeDamage(this.damage);
            this.setActive(false);
        }
    }
}