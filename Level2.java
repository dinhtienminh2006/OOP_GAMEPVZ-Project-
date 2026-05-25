public class Level2 extends LevelData{
    @Override
    public void initLevelData(){
        zombieCurrentLevel = new ZombieType[]{ZombieType.NORMALZOMBIE, ZombieType.CONEHEAD};
        chancesAppearZom = new int[][]{{0, 49}, {50, 99}};
        wave = new WaveOfZombie[]{
                new WaveOfZombie(3, 7000, false),
                new WaveOfZombie(4, 6000, false),
                new WaveOfZombie(5, 4000, true ),
        };
    }
}
