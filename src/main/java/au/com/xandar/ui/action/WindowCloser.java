package au.com.xandar.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class WindowCloser extends WindowAdapter {
	
	private final ActionListener action;
	
	public WindowCloser() {
		this.action = null;
	}
	
	public WindowCloser(ActionListener action) {
		this.action = action;
	}
	
	/**
	 * Hides the Window that is closing and disposes of its resources.
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		if (this.action != null) {
			final ActionEvent actionEvent = new ActionEvent(event.getSource(), 0, "exit");
			this.action.actionPerformed(actionEvent);
		}
		System.exit(0);
	}
}
