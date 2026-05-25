import java.io.*;
import java.util.logging.Logger;

public class SaveManager {
    public static String LEVEL_NUMBER = "1";
    static{
        saveCurrentLevel("1");
    }

    public static int loadCurrentLevel(){
        try{
            File f = new File("LEVEL_CONTENT.vbhv");
            if (!f.exists()){
                BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
                bwr.write("1");
                bwr.close();
                LEVEL_NUMBER = "1";
            } else {
                BufferedReader br = new BufferedReader(new FileReader(f));
                LEVEL_NUMBER = br.readLine();
                br.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void saveCurrentLevel(String level){
        File f = new File("LEVEL_CONTENT.vbhv");
        try{
            BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
            bwr.write(level);
            bwr.close();
            LEVEL_NUMBER = level;
        } catch (IOException ex){
            Logger.getLogger(SaveManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public static LevelData getCurrentLevel(){
        int level = Integer.parseInt(LEVEL_NUMBER);
        switch (level){
            case 1:
                return new Level1();
            case 2:
                return new Level2();
            case 3:
                return new Level3();
            case 4:
                return new Level4();
            default:
                return new Level1(); //Case-sensitive
        }
    }
}
