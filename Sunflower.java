public class Sunflower extends Plant {
    private final long produceCooldown;
    private long lastProduceTime;
    private boolean isReadyToProduce;

    public Sunflower(int x, int y) {
        super(x, y, 300, 50);
        this.produceCooldown = 10000; // 24000ms = 24 giây đẻ Sun 1 lần
        this.lastProduceTime = System.currentTimeMillis();
        this.isReadyToProduce = false;
        getImage("/images/plants/sunflower.gif");
    }

    public Sun produceSun() {
        if (isReadyToProduce) {
            lastProduceTime = System.currentTimeMillis(); // Lưu lại mốc vừa đẻ
            isReadyToProduce = false;
            return new Sun(this.getX(), this.getY());
        }
        return null;
    }

    @Override
    public void update() {
        if (!isReadyToProduce) {
            if (System.currentTimeMillis() - lastProduceTime >= produceCooldown) {
                isReadyToProduce = true;
            }
        }
    }
}