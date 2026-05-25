public class WallNut extends Plant {
    public WallNut(int x, int y) {
        super(x, y, 4000, 50);
        getImage("/images/plants/Wallnut.gif"); // Nạp ảnh Wallnut
    }
    @Override
    public void update() {
        // Wallnut chỉ đứng chịu đòn, không có logic tấn công gì cả
    }
}