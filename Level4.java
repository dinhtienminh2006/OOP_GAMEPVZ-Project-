public class Level4 extends LevelData{
    @Override
    public void initLevelData(){
        zombieCurrentLevel = new ZombieType[]{ZombieType.NORMALZOMBIE, ZombieType.CONEHEAD, ZombieType.BUCKETZOMBIE, ZombieType.FOOTBALLZOMBIE};
        chancesAppearZom = new int[][]{{0, 25}, {26, 40}, {41, 70}, {71, 99}};
        wave = new WaveOfZombie[]{
                new WaveOfZombie(5, 9000, false),
                new WaveOfZombie(12, 9000, false),
                new WaveOfZombie(7, 9000, false),
                new WaveOfZombie(15, 2000, true),
        };
    }
}
