public class Level1 extends LevelData {
    @Override
    public void initLevelData() {
        zombieCurrentLevel = new ZombieType[]{ZombieType.NORMALZOMBIE};
        chancesAppearZom = new int[][]{{0, 99}};
        wave = new WaveOfZombie[]{
                new WaveOfZombie(1, 7000, false),
                //Thêm 1 khoảng delay giua cac wave
                new WaveOfZombie(1, 4000, true),
        };
        //First wave have 5 zoms
        //Second wave have 10 zoms
    }
}
