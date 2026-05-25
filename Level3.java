public class Level3 extends LevelData{
    @Override
    public void initLevelData(){
        zombieCurrentLevel = new ZombieType[]{ZombieType.NORMALZOMBIE, ZombieType.CONEHEAD, ZombieType.BUCKETZOMBIE};
        chancesAppearZom = new int[][]{{0, 36}, {37, 60}, {61, 99}};
        wave = new WaveOfZombie[]{
                new WaveOfZombie(5, 9000, false),
                new WaveOfZombie(12, 9000, false),
                new WaveOfZombie(7, 9000, false),
                new WaveOfZombie(10, 3000, true),
        };
    }
}
