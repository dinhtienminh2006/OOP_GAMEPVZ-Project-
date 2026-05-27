import java.util.Random;

public abstract class LevelData {
    protected ZombieType[] zombieCurrentLevel;
    protected int[][] chancesAppearZom;
    protected WaveOfZombie[] wave;
    private Random rand = new Random();     //Private so this random cannot change

    public abstract void initLevelData();

    public LevelData(){
        initLevelData();
    }
    public WaveOfZombie[] getWaves(){
        return wave;
    }
    public void setWave(WaveOfZombie[] wave) {
        this.wave = wave;
    }
    public ZombieType getRandomZombie(){
        int executing = rand.nextInt(100);
        for (int i = 0; i < chancesAppearZom.length; i++){
            if (executing >= chancesAppearZom[i][0] && executing <= chancesAppearZom[i][1]){
                return zombieCurrentLevel[i];
            }
        }
        return ZombieType.NORMALZOMBIE;
    }
}
