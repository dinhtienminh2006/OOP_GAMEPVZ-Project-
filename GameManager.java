import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GameManager extends JLayeredPane implements MouseMotionListener, MouseListener, ActionListener {
    private static final int backgroundWidth = 1000;
    private static final int backgroundHeight = 750;
    private static final int START_Y_OFFSET = 125;
    private static final int SPOT_HEIGHT = 120;
    private static final int total_Rows = 5;

    private boolean isShowingHugeWave = false;
    private boolean isShowingLoseImage = false;
    private final Image backgroundImage;
    private final Image peaImage;
    private final Image icePeaImage;
    private final Image sunImage;
    private final Image hugeWaveImage;
    private boolean isShowingWinImage = false;
    private boolean isGameWon = false;

    private final Timer sunGenerate;
    private Timer gameLoop;
    private WaveManager waveManager;

    private ArrayList<ArrayList<Zombie>> zombieRoad;
    private ArrayList<Sun> activeSun;
    private ArrayList<Projectile> activeProjectiles; // Mảng chứa đạn
    private int[] rows;
    private PlacingSpot[] placingSpots;

    private int lastZombieX; 
    private int lastZombieY;

    private int mouX_coor;
    private int mouY_coor;

    private PlanTypeFolder.PlantType plantingBrush = PlanTypeFolder.PlantType.NONE;
    private int sunScore;
    private JLabel sunScoreBoard;
    private JLabel loseLabel;
    private JLabel winLabel;
    private JLabel levelLabel;

    public void setPlantingBrush(PlanTypeFolder.PlantType type){
        this.plantingBrush = type;
    }

    public GameManager(JLabel sunScoreBoard) {
        setSize(1000, 750); 
        setLayout(null);
        addMouseMotionListener(this);
        addMouseListener(this);

        this.sunScoreBoard = sunScoreBoard;
        this.sunScoreBoard.setBounds(32, 75, 80, 30);
        this.sunScoreBoard.setFont(new Font("Open Sans", 1, 25));
        this.sunScoreBoard.setHorizontalTextPosition(SwingConstants.CENTER);
        this.sunScoreBoard.setForeground(Color.BLACK);
        this.add(this.sunScoreBoard, Integer.valueOf(3));
        setSunScore(150); 

        levelLabel = new JLabel();
        levelLabel.setBounds(870, 4, 120, 32);
        levelLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        levelLabel.setForeground(new Color(220, 0, 0));
        levelLabel.setHorizontalAlignment(SwingConstants.CENTER); 
        levelLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.add(levelLabel, Integer.valueOf(3));

        backgroundImage = new ImageIcon(this.getClass().getResource("/images/mainBG.png")).getImage();
        peaImage = new ImageIcon(this.getClass().getResource("/images/pea.png")).getImage();
        icePeaImage = new ImageIcon(this.getClass().getResource("/images/icepea.png")).getImage();
        sunImage = new ImageIcon(this.getClass().getResource("/images/sun.png")).getImage();
        hugeWaveImage = new ImageIcon(this.getClass().getResource("/images/hugeWage.png")).getImage();

        URL loseURL = this.getClass().getResource("/images/loseImage.png");
        if (loseURL != null) {
            ImageIcon loseIcon = new ImageIcon(loseURL);
            loseLabel = new JLabel(loseIcon);

            int xCenter = (backgroundWidth - loseIcon.getIconWidth()) / 2;
            int yCenter = (backgroundHeight - loseIcon.getIconHeight()) / 2;
            loseLabel.setBounds(xCenter, yCenter, loseIcon.getIconWidth(), loseIcon.getIconHeight());
            loseLabel.setVisible(false); // Ẩn đi khi mới vào game

            // Đặt ở layer 10 để đảm bảo nó đè lên tất cả cây, quái, đạn và nền
            this.add(loseLabel, Integer.valueOf(10));
        } else {
            System.err.println("Lỗi: Không tìm thấy file ảnh /images/zombiesEatYourBrains.png");
        }

        //Hình Cúp
        URL winURL = this.getClass().getResource("/images/cupVictory.png");
        if (winURL != null) {
            ImageIcon winIcon = new ImageIcon(winURL);
            winLabel = new JLabel(winIcon);

            // 1. Đổi con trỏ chuột thành hình bàn tay khi di chuột vào cúp
            winLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
            // 2. Gắn sự kiện Click chuột
            winLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (gameLoop != null) {
                        gameLoop.stop();
                    }

                    int currentSavedLevel = Integer.parseInt(SaveManager.LEVEL_NUMBER);

                    if (currentSavedLevel > 4) { 
                        // BẢNG THÔNG BÁO HOÀN THÀNH TOÀN BỘ GAME (LEVEL 4)
                        ImageIcon championIcon = new ImageIcon(getClass().getResource("/images/cupVictory.png"));
                        
                        String finalMessage = "<html><div style='text-align: center; font-family: Tahoma, sans-serif; "
                                            + "background-color: #FFF8DC; padding: 25px; border: 4px solid #DAA520; border-radius: 15px;'>"
                                            + "<h1 style='color: #FF4500; font-size: 26px; text-shadow: 1px 1px #000; margin-bottom: 5px;'>🏆 CHAMPION 🏆</h1>"
                                            + "<b style='color: #2E8B57; font-size: 16px;'>VICTORY IS YOURS!</b><br><br>"
                                            + "<span style='font-size: 14px; color: #333;'>You have successfully cleared all 4 levels.<br>"
                                            + "The neighborhood is finally safe from the zombie apocalypse!</span><br><br>"
                                            + "<i style='color: #8B0000; font-size: 13px;'>Thank you for playing!</i>"
                                            + "</div></html>";
                        
                        Object[] finalButton = {"Finish Game"};
                        
                        JOptionPane.showOptionDialog(
                                GameManager.this,
                                finalMessage,
                                "Game Cleared!",
                                JOptionPane.DEFAULT_OPTION, 
                                JOptionPane.PLAIN_MESSAGE,
                                championIcon,
                                finalButton,
                                finalButton[0]
                        );
                        
                        // Reset file save về Level 1 để người chơi có thể chơi lại từ đầu vào lần mở game sau
                        SaveManager.saveCurrentLevel("1"); 
                        System.exit(0);

                    } else {
                        ImageIcon dialogIcon = new ImageIcon(getClass().getResource("/images/cupVictory.png"));
                        String customMessage = "<html><div style='text-align: center; font-family: Tahoma, sans-serif;'>"
                                            + "<b style='color: #FF8C00; font-size: 18px;'>CONGRATULATIONS!</b><br><br>"
                                            + "<span style='font-size: 14px;'>You have successfully defended your house!</span><br><br>"
                                            + "<i style='color: #2E8B57; font-size: 13px;'>Do you want to play the next level?</i>"
                                            + "</div></html>";
                        Object[] customButtons = {"Next Level", "Quit Game"};
                        int winning = JOptionPane.showOptionDialog(
                            GameManager.this,       // Cửa sổ cha
                            customMessage,          // Nội dung HTML đã trang trí
                            "Level Cleared!",       // Tiêu đề cửa sổ
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.PLAIN_MESSAGE, // Xóa icon (i) mặc định của Windows
                            dialogIcon,             // Đưa icon chiếc cúp vào
                            customButtons,          // Đưa 2 nút tự tạo vào
                            customButtons[0]);

                        if (winning == JOptionPane.YES_OPTION){
                            goToNextLevel();
                        } else {
                            System.exit(0);
                        }
                    }
                }
            });

            winLabel.setVisible(false);
            this.add(winLabel, Integer.valueOf(10));
        } else {
            System.err.println("Cannot file image");
        }

        zombieRoad = new ArrayList<>();
        for (int i = 0; i < total_Rows; i++) {
            zombieRoad.add(new ArrayList<>());
        }

        rows = new int[total_Rows];
        for (int i = 0; i < total_Rows; i++) {
            rows[i] = START_Y_OFFSET + (i * SPOT_HEIGHT);
        }

        placingSpots = new PlacingSpot[45];
        for (int i = 0; i < 45; i++) {
            PlacingSpot spots = new PlacingSpot();
            spots.setLocation(50 + (i % 9) * 104, START_Y_OFFSET  + (i / 9) * SPOT_HEIGHT + 15);
            spots.setAction(new PlantActionListener((i % 9), (i / 9)));
            placingSpots[i] = spots;
            add(spots, Integer.valueOf(0));
        }

        activeSun = new ArrayList<>();
        activeProjectiles = new ArrayList<>(); 

        sunGenerate = new Timer(15000, (ActionEvent e) -> {
            Random rand = new Random();
            Sun procedure = new Sun(rand.nextInt(backgroundWidth - 300) + 240, 0, rand.nextInt(backgroundHeight - 300) + 100, this);
            activeSun.add(procedure);
        });
        sunGenerate.start();

        createCard("cards/card_sunflower.png", 130, 13, PlanTypeFolder.PlantType.SUNFLOWER);
        createCard("cards/card_peashooter.png", 220, 13, PlanTypeFolder.PlantType.PEASHOOTER);
        createCard("cards/card_freezepeashooter.png", 310, 13, PlanTypeFolder.PlantType.FROZENPEASHOOTER);
        createCard("cards/card_wallnut.png", 400, 13, PlanTypeFolder.PlantType.WALLNUT);
        createCard("cards/shovel.png", 500, 13, PlanTypeFolder.PlantType.SHOVEL);

        gameLoop = new Timer(16, this);
        waveManager = new WaveManager(this);
        waveManager.startLevel();
        gameLoop.start();

        levelLabel.setText("Level: " + SaveManager.LEVEL_NUMBER);
    }

    private void createCard(String fileName, int x, int y, PlanTypeFolder.PlantType type){
        URL imgURL = this.getClass().getResource("/images/" + fileName);
        if (imgURL == null) return;

        Image img = new ImageIcon(imgURL).getImage();
        PlantCard card = new PlantCard(img);
        card.setBounds(x, y, 64, 90);
        card.setActionListener(e -> setPlantingBrush(type));
        this.add(card, Integer.valueOf(2));
    }

    public void addZombie(ZombieType type){
        Random rand = new Random();
        int n = rand.nextInt(5);
        int yRow = rows[n] - 40; // Kéo Zombie lên giữa thảm cỏ
        Zombie newZombie = type.generateZom(backgroundWidth, yRow);
        zombieRoad.get(n).add(newZombie);
    }

    public int getSunScore(){ return sunScore; }
    public void setSunScore(int sunScore){
        this.sunScore = sunScore;
        sunScoreBoard.setText(String.valueOf(sunScore));
    }
    public void showHugeWaveComing(){
        isShowingHugeWave = true;
        repaint();

        Timer hideTimer = new Timer(3000, e -> {
            isShowingHugeWave = false;
            repaint();
        });
        hideTimer.setRepeats(false);
        hideTimer.start();
    }
    public boolean isAllZombiesDead(){
        for (ArrayList<Zombie> lane : zombieRoad) {
            if (!lane.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        updateSun();
        updatePlants();
        updateProjectiles(); 
        updateZombie();
        checkCollision();

        checkWin();
        checkLose();
        repaint();
    }
    public void checkLose(){
        for (ArrayList<Zombie> lane : zombieRoad) {
            for (Zombie z : lane) {
                if (z.getX() < -30){
                    gameOver();
                    return;
                }
            }
        }
    }

    private void gameOver(){
        if (gameLoop != null){
            gameLoop.stop();
        }
        if (sunGenerate != null){
            sunGenerate.stop();
        }
        if (waveManager != null){
            waveManager.stopAllTimer();
        }
        if (loseLabel != null) {
            loseLabel.setVisible(true);
        }

        Timer delayDialog = new Timer(4000, e -> {
            // 1. Dùng mã HTML thiết kế lại bảng thông báo rùng rợn
            String customMessage = "<html>"
                                 + "<div style='text-align: center; width: 280px; font-family: \"Comic Sans MS\", sans-serif; "
                                 + "background-color: #1a1a1a; padding: 15px; border: 3px solid #8B0000; border-radius: 8px;'>"
                                 + "<h1 style='color: #FF0000; font-size: 22px; margin: 0 0 10px 0; "
                                 + "text-shadow: 2px 2px #000000; letter-spacing: 2px;'>GAME OVER</h1>"
                                 + "<b style='color: #90EE90; font-size: 14px;'>THE ZOMBIES<br>ATE YOUR BRAINS!</b><br><br>"
                                 + "<span style='color: #BBBBBB; font-size: 12px;'>Would you like to try again?</span>"
                                 + "</div>"
                                 + "</html>";
            // Thay đổi nút bấm Yes/No
            Object[] options = {"Try Again", "Quit Game"};
            int resultDialog = JOptionPane.showOptionDialog(
                    this, 
                    customMessage, 
                    "Noooo!", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.PLAIN_MESSAGE, // Ẩn cái icon dấu hỏi mặc định đi
                    null,                      // Nếu bạn có icon nhỏ thì bỏ vào đây, không thì để null
                    options, 
                    options[0]);
        if (resultDialog == JOptionPane.YES_OPTION){
            restartGame();
        } else {
            System.exit(0);
        }
    });
    
    delayDialog.setRepeats(false);
    delayDialog.start();
    }
    public void restartGame(){
        setSunScore(150);
        for(ArrayList<Zombie> lane : zombieRoad){
            lane.clear();
        }
        activeSun.clear();
        activeProjectiles.clear();

        for (int i = 0; i < placingSpots.length; i++) {
            if (placingSpots[i] != null && placingSpots[i].assignedPlant != null) {
                placingSpots[i].removePlant();
            }
        }
        isShowingHugeWave = false;
        isShowingLoseImage = false;
        isShowingWinImage = false;
        plantingBrush = PlanTypeFolder.PlantType.NONE;

        if (waveManager != null){
            waveManager.stopAllTimer();
        }
        waveManager = new WaveManager(this);
        waveManager.startLevel();

        if (sunGenerate != null){
            sunGenerate.start();
        }
        if (gameLoop != null){
            gameLoop.start();
        }
        if (loseLabel != null) {
            loseLabel.setVisible(false);
        }
        levelLabel.setText("Level: " + SaveManager.LEVEL_NUMBER);
        repaint();
    }

    private void gameWin(){
        isGameWon = true;
        if (sunGenerate != null){
            sunGenerate.stop();
        }
        if (waveManager != null){
            waveManager.stopAllTimer();
        }
        if (winLabel !=null){
            int cupWidth = winLabel.getIcon().getIconWidth();
            int cupHeight = winLabel.getIcon().getIconHeight();
            
            // Đặt chiếc cúp xuất hiện ngay tại vị trí ngẫu nhiên mà con zombie cuối cùng ngã xuống
            // (Trừ đi 20 pixel ở trục Y để chiếc cúp nổi lên trên mặt cỏ một chút cho đẹp)
            winLabel.setBounds(lastZombieX + 10, lastZombieY + 25, cupWidth, cupHeight);
            winLabel.setVisible(true);
        }
        repaint();
    }

    public void checkWin(){
        if (isGameWon) return;
        if (waveManager != null && waveManager.isSpawingFinished() && isAllZombiesDead()){
            waveManager.moveNextLevel();
            gameWin();
        }
    }

    public void goToNextLevel(){
        isGameWon = false;
        setSunScore(150);
        for  (ArrayList<Zombie> lane : zombieRoad) {
            lane.clear();
        }
        activeSun.clear();
        activeProjectiles.clear();

        for (int i = 0; i < placingSpots.length; i++) {
            if (placingSpots[i] != null && placingSpots[i].assignedPlant != null) {
                placingSpots[i].removePlant();
            }
        }
        isShowingHugeWave = false;
        isShowingLoseImage = false;
        isShowingWinImage = false;
        plantingBrush = PlanTypeFolder.PlantType.NONE;
        if (waveManager != null){
            waveManager.stopAllTimer();
        }
        waveManager = new WaveManager(this);
        waveManager.startLevel();
        if (sunGenerate != null){
            sunGenerate.start();
        }
        if (gameLoop != null){
            gameLoop.start();
        }
        if (loseLabel != null) {
            loseLabel.setVisible(false);
        }
        if (winLabel != null) {
            winLabel.setVisible(false);
        }
        levelLabel.setText("Level: " + SaveManager.LEVEL_NUMBER);
        repaint();
    }

    private void updateZombie(){
        for (int i = 0; i < zombieRoad.size(); i++){
            ArrayList<Zombie> currentLane = zombieRoad.get(i);
            for (int j = currentLane.size() - 1; j >= 0; j--) {
                Zombie newZom = currentLane.get(j);
                if (newZom.isAlive()) {
                    newZom.update(); 
                } else {
                    lastZombieX = newZom.getX();
                    lastZombieY = newZom.getY();
                    currentLane.remove(j); 
                }
            }
        }
    }

    private void updateSun(){
        for (int i = activeSun.size() - 1; i >= 0; i--){
            Sun s = activeSun.get(i);
            s.update();
            if (s.isExpired()) {
                activeSun.remove(i); 
            }
        }
    }

    private void updateProjectiles() {
        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = activeProjectiles.get(i);
            p.update();
            if (!p.isActive()) {
                activeProjectiles.remove(i); 
            }
        }
    }

    private void updatePlants(){
        for (int i = 0; i < placingSpots.length; i++){
            PlacingSpot spot = placingSpots[i];
            if (spot.assignedPlant != null){
                Plant p = spot.assignedPlant;
                p.update();

                // 1. Thu hoạch Sun từ hoa hướng dương (Giữ nguyên)
                if (p instanceof Sunflower){
                    Sun newSun = ((Sunflower) p).produceSun();
                    if (newSun != null) activeSun.add(newSun);
                }
                // 2. Thu hoạch Đạn từ cây bắn đậu (BỔ SUNG LOGIC TẦM NHÌN)
                if (p instanceof Pea) {
                    int laneIndex = i / 9; // Tính xem súng đang nằm ở làn thứ mấy (0 đến 4)
                    boolean hasTarget = false; // Cờ kiểm tra có mục tiêu không

                    // Quét dọc làn đường xem có Zombie nào trước mặt không
                    for (Zombie z : zombieRoad.get(laneIndex)) {
                        // Nếu Zombie còn sống và X của Zombie lớn hơn X của súng (đang ở phía trước)
                        if (!z.isDead() && z.getX() > p.getX()) {
                            hasTarget = true;
                            break; // Thấy 1 con là đủ để bắn rồi, không cần quét thêm
                        }
                    }
                    // Chỉ khi có mục tiêu thì mới bóp cò!
                    if (hasTarget) {
                        Projectile bullet = ((Pea) p).shoot();
                        if (bullet != null) activeProjectiles.add(bullet);
                    }
                }
            }
        }
    }

    private void checkCollision(){
        // 1. VA CHẠM: ĐẠN TRÚNG ZOMBIE
        for (int i = 0; i < zombieRoad.size(); i++) {
            ArrayList<Zombie> lane = zombieRoad.get(i);
            int laneY = rows[i];

            for (Zombie z : lane) {
                for (Projectile p : activeProjectiles) {
                    if (p.isActive() && !z.isDead() && Math.abs(p.getY() - laneY) < 50) {
                        if (p.getX() + 20 >= z.getX() && p.getX() <= z.getX() + 60) {
                            p.hit(z); 
                        }
                    }
                }
            }
        }

        // 2. VA CHẠM: ZOMBIE CẮN CÂY
        for (int i = 0; i < total_Rows; i++) {
            ArrayList<Zombie> lane = zombieRoad.get(i);
            
            for (Zombie z : lane) {
                if (z.isDead()) continue;

                boolean isHittingAnyPlant = false;

                // Quét qua 9 ô đất
                for (int j = 0; j < 9; j++) {
                    PlacingSpot spot = placingSpots[j + i * 9];
                    
                    if (spot.assignedPlant != null && !spot.assignedPlant.isDead()) {
                        Plant p = spot.assignedPlant;
                        
                        // Zombie chạm cây
                        if (z.getX() <= p.getX() + 50 && z.getX() >= p.getX() - 10) {
                            z.attack(p); 
                            isHittingAnyPlant = true;
                            break; 
                        }
                    }
                }

                if (!isHittingAnyPlant) {
                    z.setEating(false);
                }
            }
        }

        // 3. DỌN DẸP XÁC CÂY
        for (int i = 0; i < placingSpots.length; i++) {
            PlacingSpot spot = placingSpots[i];
            if (spot.assignedPlant != null && spot.assignedPlant.isDead()) {
                spot.removePlant();
            }
        }
    }

    private class PlantActionListener implements ActionListener {
        int x, y;
        public PlantActionListener(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isGameWon) return;
            int realX = 50 + x * 104;
            int realY = START_Y_OFFSET + y * SPOT_HEIGHT + 15;

            // LOGIC MỚI: NẾU ĐANG CẦM XẺNG
            if (plantingBrush == PlanTypeFolder.PlantType.SHOVEL) {
                PlacingSpot spot = placingSpots[x + y * 9];
                
                // Kiểm tra xem ô đất đó có cây hay không
                if (spot.assignedPlant != null) {
                    // 1. Lấy giá tiền Sun của cái cây đang đứng ở ô này
                    int refundValue = spot.assignedPlant.getCost(); 
                    
                    // 2. Cộng số tiền Sun đó vào tổng điểm hiện tại của người chơi
                    setSunScore(getSunScore() + refundValue); 
                    
                    // 3. Tiến hành đào cây (Xóa cây khỏi ô đất)
                    spot.removePlant(); 
                }
                plantingBrush = PlanTypeFolder.PlantType.NONE; // Trả lại con trỏ chuột bình thường
                return; // Kết thúc thao tác
            }

            if (placingSpots[x + y * 9].assignedPlant != null) return;

            switch (plantingBrush) {
                case SUNFLOWER:
                    planting(new Sunflower(realX, realY), 50, x, y); 
                    break;
                case PEASHOOTER:
                    planting(new PeaShooter(realX, realY), 100, x, y);
                    break;
                case FROZENPEASHOOTER:
                    planting(new IcePeaShooter(realX, realY), 175, x, y); 
                    break;
                case WALLNUT:
                    planting(new WallNut(realX, realY), 50, x, y);
                default:
                    break;
            }
            plantingBrush = PlanTypeFolder.PlantType.NONE;
        }
    }

    private void planting(Plant place_plant, int sunCost, int x, int y){
        if (getSunScore() >= sunCost){
            placingSpots[x + y * 9].setPlant(place_plant);
            setSunScore(getSunScore() - sunCost);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouX_coor = e.getX();
        mouY_coor = e.getY();
    }
    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth() + 10, getHeight(), null);

        for (int i = 0; i < 45; i++){
            PlacingSpot newSpot = placingSpots[i];
            if (newSpot.assignedPlant != null){
                Plant newPlant = newSpot.assignedPlant;
                g.drawImage(newPlant.getImage(), newPlant.getX(), newPlant.getY(), null);
            }
        }

        for (Projectile p : activeProjectiles) {
            if (p instanceof IceBullet) {
                g.drawImage(icePeaImage, p.getX(), p.getY(), null); // Vẽ đạn xanh dương
            } else {
                g.drawImage(peaImage, p.getX(), p.getY(), null);    // Vẽ đạn xanh lá
            }
        }
    }

    // HÀM 2: VẼ ZOMBIE SAU CÙNG ĐỂ ZOMBIE ĐÈ LÊN GIAO DIỆN
    @Override
    public void paint(Graphics g) {
        // hoàn thành việc vẽ Hàm 1 (Nền, Cây, Đạn...) và vẽ các Thẻ bài trước
        super.paint(g); 

        // Sau khi mọi thứ đã vẽ xong, ta mới vẽ Zombie đè lên trên cùng
        for (int i = 0; i < zombieRoad.size(); i++){
            for (Zombie z : zombieRoad.get(i)){
                g.drawImage(z.getImage(), z.getX(), z.getY(), null);
            }
        }

        for (int i = 0; i < activeSun.size(); i++){
            Sun s = activeSun.get(i);
            g.drawImage(sunImage, s.getX(), s.getY(), null);
        }

        if (isShowingHugeWave && hugeWaveImage != null){
            // 1. Đặt chiều ngang tối đa cho hình chữ (ví dụ 800 pixel để lọt thỏm giữa màn 1000px)
            int targetWidth = 800;

            // 2. Tính chiều cao tỷ lệ thuận để chữ không bị bóp méo
            int targetHeight = (hugeWaveImage.getHeight(null) * targetWidth) / hugeWaveImage.getWidth(null);

            // 3. Tính toán tọa độ X, Y để căn chính giữa hoàn hảo với kích thước mới
            int x = (backgroundWidth - targetWidth) / 2;
            int y = (backgroundHeight - targetHeight) / 2;
            // 4. Vẽ ảnh với kích thước đã thu nhỏ
            g.drawImage(hugeWaveImage, x, y, targetWidth, targetHeight, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        int myX = e.getX();
        int myY = e.getY();

        for (int i = activeSun.size() - 1; i >= 0; i--){
            Sun s = activeSun.get(i);
            if(myX >= s.getX() && myX <= s.getX() + 60 && myY >= s.getY() && myY <= s.getY() + 60) {
                activeSun.remove(i);
                setSunScore(getSunScore() + 50);
                return;
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}