import java.awt.event.*;
import javax.swing.*;

public class PlacingSpot extends JPanel implements MouseListener{

    private ActionListener detector;

    public PlacingSpot(){
        //setBorder(new LineBorder(Color.RED));
        setOpaque(false);
        addMouseListener(this);
        setSize(100, 120);
    }

    public Plant assignedPlant;

    public void setPlant(Plant p){
        this.assignedPlant = p;
    }
    public void removePlant(){
        assignedPlant.stop();
        assignedPlant = null;
    }

    public boolean isInsideSpot(int placing_spot){
        return (placing_spot > getLocation().x) && (placing_spot < getLocation().x + 100);
    }
    public void setAction(ActionListener detector){
        this.detector = detector;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Update
        java.awt.Component parent = getParent();
        if (parent != null){
            //Convert mouse coordination to the main coordination
            MouseEvent event = javax.swing.SwingUtilities.convertMouseEvent(this, e, parent);
            parent.dispatchEvent(event);  //Restrict the gameManager follow this
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {        //Active when the user release their mouse
        if (detector != null){              //Check if the mouse of the user are in the PlaceSpot or not
            detector.actionPerformed(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, ""));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //Nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //Nothing
    }
}
