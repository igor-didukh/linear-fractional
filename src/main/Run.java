package main;
import java.awt.EventQueue;

public class Run {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Common.showFrame(new MainWindow());
			}
		});
	}
}
