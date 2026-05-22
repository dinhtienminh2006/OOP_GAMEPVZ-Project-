import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class PlantCard extends JPanel implements MouseListener {
    private Image img;
    private ActionListener actionListener;

    public PlantCard(Image img) {
        setSize(65, 85);
        this.img = img;
        addMouseListener(this);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void mouseClicked(MouseEvent e){

    }

    @Override
    public void mousePressed(MouseEvent e){

    }

    @Override
    public void mouseReleased(MouseEvent e){
        if (actionListener != null){
            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, ""));
        }
    }

    @Override
    public void mouseExited(MouseEvent e){
    }

    @Override
    public void mouseEntered(MouseEvent e){
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

}