//Define one enum Zombie so it will automatically update to LevelData
public enum ZombieType {

    NORMALZOMBIE{
        @Override
        public Zombie generateZom(int x, int y){
            return new NormalZombie(x, y);
        }
    },
    CONEHEAD{
        @Override
        public Zombie generateZom(int x, int y){
            return new ConeHeadZombie(x, y);
        }
    },
    FLAGZOMBIE{
        @Override
        public Zombie generateZom(int x, int y){
            return new FlagZombie(x, y);
        }
    },
    BUCKETZOMBIE{
        @Override
        public Zombie generateZom(int x, int y){
            return new BucketZombie(x, y);
        }
    },
    FOOTBALLZOMBIE{
        @Override
        public Zombie generateZom(int x, int y){
            return new FootBallZom(x, y);
        }
    };
    public abstract Zombie generateZom(int x, int y);
}
