public class WaveOfZombie {
    private int totalZombie;
    private int spawnDelay;
    private boolean hasFlagZombie;

    public WaveOfZombie(int totalZombie, int spawnDelay,  boolean hasFlagZombie) {
        this.totalZombie = totalZombie;
        this.spawnDelay = spawnDelay;
        this.hasFlagZombie = hasFlagZombie;
    }

    public int getTotalZombie() {
        return totalZombie;
    }
    public int getSpawnDelay() {
        return spawnDelay;
    }
    public boolean isHasFlagZombie() {
        return hasFlagZombie;
    }
}
