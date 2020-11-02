package dad.recursos;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Classe que representa um JTextField com um ícone e placeholder
 * @author dariopereiradp
 *
 */
public class IconTextField extends JTextField implements FocusListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4768453631465945471L;
	private IconTextComponentHelper mHelper = new IconTextComponentHelper(this);

    public IconTextField() {
        super();
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