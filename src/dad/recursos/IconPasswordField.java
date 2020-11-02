package dad.recursos;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Icon;
import javax.swing.JPasswordField;
import javax.swing.border.Border;

/**
 * Classe que representa um JPasswordField com um ícone dentro e uma placeholder
 * @author dariopereiradp
 *
 */
public class IconPasswordField extends JPasswordField implements FocusListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4380741490035110257L;
	private IconTextComponentHelper mHelper = new IconTextComponentHelper(this);

    public IconPasswordField() {
        super();
        addFocusListener(this);
    }

    public IconPasswordField(int cols) {
        super(cols);
        addFocusListener(this);
    }

    private IconTextComponentHelper getHelper() {
        if (mHelper == null)
            mHelper = new IconTextComponentHelper(this);

        return mHelper;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        getHelper().onPaintComponent(graphics);
    }

    public void setIcon(Icon icon) {
        getHelper().onSetIcon(icon);
    }
    
    public void setHint(String hint) {
    	getHelper().onSetHint(hint);
    }

    @Override
    public void setBorder(Border border) {
        getHelper().onSetBorder(border);
        super.setBorder(getHelper().getBorder());
    }
    
    @Override
    public void focusGained(FocusEvent arg0) {
        this.repaint();
    }

    @Override
    public void focusLost(FocusEvent arg0) {
        this.repaint();
    }
}