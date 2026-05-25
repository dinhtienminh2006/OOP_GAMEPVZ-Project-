import java.awt.Image;
import javax.swing.*;

public abstract class Zombie extends GameObject {
    protected int hp;
    protected final int maxHp;
    protected int speed; 
    protected int damage;
    protected boolean isEating;
    
    protected long lastAttackTime;
    protected final long attackCooldown = 1000;

    protected long lastMoveTime;
    protected long moveInterval; 
    protected long originalMoveInterval;

    protected boolean isChilled;
    protected long lastChillTime;
    protected final long chillDuration = 5000;

    protected Image zomImage;

    public Zombie(int x, int y, int hp, int speed, int damage) {
        super(x, y);
        this.hp = hp;
        this.maxHp = hp;
        this.speed = speed;
        this.damage = damage;
        this.isEating = false;

        this.lastAttackTime = System.currentTimeMillis();

        this.moveInterval = 35; 
        this.originalMoveInterval = 35;
        this.lastMoveTime = System.currentTimeMillis();

        this.isChilled = false;
        this.lastChillTime = 0;
    }

    @Override
    public abstract void update();

    public void takeDamage(int damage) {
        hp -= damage;
        if (this.hp < 0) this.hp = 0;
    }
    
    protected void loadImage(String imagePath){
        java.net.URL imageURL = this.getClass().getResource(imagePath);
        if (imageURL != null){
            this.zomImage = new ImageIcon(imageURL).getImage();
        }
    }
    
    public Image getImage(){ return this.zomImage; }
    public boolean isAlive() { return hp > 0; }
    public boolean isDead() { return this.hp <= 0; }

    public void move() {
        if (!isEating) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMoveTime >= moveInterval) {
                x -= speed;
                lastMoveTime = currentTime;
            }
        }
    }

    public void attack(Plant targetPlant) {
        if (targetPlant != null && !targetPlant.isDead()) {
            isEating = true;
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAttackTime >= attackCooldown) {
                targetPlant.takeBite(this.damage);
                lastAttackTime = currentTime; 
            }
        } else {
            isEating = false;
        }
    }

    public void applyChill() {
        this.lastChillTime = System.currentTimeMillis(); 
        if (!isChilled) {
            this.isChilled = true;
            this.moveInterval = this.originalMoveInterval * 2; 
        }
    }

    public void updateChill() {
        if (isChilled) {
            if (System.currentTimeMillis() - lastChillTime >= chillDuration) {
                isChilled = false;
                this.moveInterval = originalMoveInterval;
            }
        }
    }

//----------Getters and setters----------
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public boolean isEating() { return isEating; }
    public void setEating(boolean isEating) { this.isEating = isEating; }
    public int getMaxHp() { return maxHp; }
    public boolean isChilled() { return isChilled; }
}