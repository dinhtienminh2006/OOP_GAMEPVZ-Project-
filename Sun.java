public class Sun extends GameObject {
    private final int value;
    private final long maxLifespan;
    private long landTime;      // Lưu mốc thời gian chạm đất
    private boolean hasLanded;  // Cờ kiểm tra đã chạm đất chưa
    private boolean isExpired;
    private final int fallSpeed;
    private final int yEnd;
    private GameManager gameManager;

    // Constructor dùng cho Hoa Hướng Dương đẻ
    public Sun(int x, int y) {
        super(x, y);
        this.yEnd = y + 30; // Rớt nhẹ xuống cạnh bông hoa
        this.fallSpeed = 2;
        this.value = 50;
        this.maxLifespan = 15000;
        this.isExpired = false;
        this.hasLanded = false;
    }

    // Constructor dùng cho Bầu trời rơi xuống
    public Sun(int x, int y, int yEnd, GameManager gameManager) {
        super(x, y);
        this.yEnd = yEnd;
        this.gameManager = gameManager;
        this.fallSpeed = 2;
        this.value = 50;
        this.maxLifespan = 8000;
        this.isExpired = false;
        this.hasLanded = false;
    }

    public int collect() {
        this.isExpired = true;
        return this.value;
    }

    @Override
    public void update() {
        // 1. Logic rơi xuống đất
        if (this.y < this.yEnd) {
            this.y += fallSpeed;

            if (this.y >= this.yEnd) {
                this.y = this.yEnd;
                if (!hasLanded) {
                    hasLanded = true;
                    landTime = System.currentTimeMillis(); // Bắt đầu bấm giờ tự hủy từ giây này
                }
            }
        }
        // 2. Chạm đất rồi thì kiểm tra thời gian sống
        else if (!isExpired) {
            if (System.currentTimeMillis() - landTime >= maxLifespan) {
                isExpired = true;
            }
        }
    }

    public int getValue() { return value; }
    public boolean isExpired() { return isExpired; }
}