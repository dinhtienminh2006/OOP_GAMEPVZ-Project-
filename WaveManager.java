import javax.swing.Timer;
import java.awt.event.*;
import java.sql.Time;

public class WaveManager {
    private GameManager gameManager;
    private LevelData currentlevelData;
    private int currentLevelNumber;

    private int currentWaveIndex = 0;
    private int zombiesInWve = 0;
    private boolean spawingFinished = false;

    private Timer spawnTime;
    private Timer delayWave;
    private Timer delayNextWave;

    public WaveManager(GameManager gameManager) {
        this.gameManager = gameManager;

        SaveManager.loadCurrentLevel();
        currentlevelData = SaveManager.getCurrentLevel();

        currentLevelNumber = Integer.parseInt(SaveManager.LEVEL_NUMBER);
        System.out.println("====== GAME ĐANG NẠP LEVEL: " + currentLevelNumber + " ======");
    }

    public void startLevel() {
        startWave(0);
    }
    public boolean isSpawingFinished(){
        return spawingFinished;
    }

    public void moveNextLevel(){
        currentLevelNumber++;
        SaveManager.saveCurrentLevel(String.valueOf(currentLevelNumber));
    }

    public void stopAllTimer(){
        if (spawnTime != null){
            spawnTime.stop();
        }
        if (delayWave != null){
            delayWave.stop();
        }
        if (delayNextWave != null){
            delayNextWave.stop();
        }
    }

    private void startWave(int waveIndex) {
        WaveOfZombie[] wavesLevel = currentlevelData.getWaves();

        if (waveIndex >= wavesLevel.length) {
            spawingFinished = true;
            return;
        }

        currentWaveIndex = waveIndex;
        zombiesInWve = 0;

        WaveOfZombie currentWave = wavesLevel[currentWaveIndex];
        boolean isFinalWave = (waveIndex == wavesLevel.length - 1);
        if (currentWave.isHasFlagZombie() || isFinalWave) {
            System.out.println("A HUGE WAVE OF ZOMBIE IS APPROACHING");
            gameManager.showHugeWaveComing();
            gameManager.addZombie(ZombieType.FLAGZOMBIE);
            Timer delayWavve = new Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    beginSpawingZombie(currentWave);
                }
            });
            delayWavve.setRepeats(false);
            delayWavve.start();
        } else {
            beginSpawingZombie(currentWave);
        }
    }
    private void beginSpawingZombie(WaveOfZombie currentWave) {
        spawnTime = new Timer(currentWave.getSpawnDelay(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnSingleZombie();
            }
        });
        spawnTime.start();
        spawnSingleZombie();
    }
    private void spawnSingleZombie(){
        WaveOfZombie currentWave = currentlevelData.getWaves()[currentWaveIndex];
        int totalZombieInWave = currentWave.getTotalZombie();

        ZombieType zombieType = currentlevelData.getRandomZombie();
        gameManager.addZombie(zombieType);

        zombiesInWve++;

        if (zombiesInWve >= totalZombieInWave) {
            spawnTime.stop();

            delayNextWave = new Timer(500, new ActionListener() { // Cứ 1 giây kiểm tra 1 lần
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    // Nếu trên sân đã sạch bóng Zombie (người chơi đã tiêu diệt hết)
                    if (gameManager.isAllZombiesDead()) {
                        delayNextWave.stop(); // Dừng vòng lặp kiểm tra này lại
                        
                        // 1. Lấy tổng số Wave của level hiện tại
                        int totalWaves = currentlevelData.getWaves().length;
                        // 2. Kiểm tra xem người chơi có đang ở Wave cuối cùng không
                        boolean isLastWave = (currentWaveIndex == totalWaves - 1);
                        // 3. Nếu là Wave cuối: delay = 0 (Rớt cúp ngay lập tức)
                        //    Nếu chuyển Wave bình thường: delay = 2000 (Nghỉ 2 giây)
                        int timeDelay = isLastWave ? 0 : 2000;

                        // Chờ 2 giây (2000ms) tạo khoảng nghỉ mượt mà rồi mới thả Wave tiếp theo
                        Timer transitionTimer = new Timer(timeDelay, e2 -> {
                            startWave(currentWaveIndex + 1);
                        });
                        transitionTimer.setRepeats(false);
                        transitionTimer.start();
                    }
                }
            });
            delayNextWave.start();
        }
    }
}
