package au.com.xandar.swimclub.awards;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Renders the JList with just File name, not complete paths.
 * 
 * @author william
 */
public final class ListRenderer extends JLabel implements ListCellRenderer {
	
    public Component getListCellRendererComponent(
      JList list,              // the list
      Object value,            // value to display
      int index,               // cell index
      boolean isSelected,      // is the cell selected
      boolean cellHasFocus)    // does the cell have focus
    {
    	final File file = (File) value;
        final String s = file.getName();
        this.setText(s);
/*        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
*/        
        return this;
    }
}
