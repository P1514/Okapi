package general;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;				

/** @see https://stackoverflow.com/a/3002830/230513 */
class SetSettings {
	private static Logger LOGGER = new Logging().create(SetSettings.class.getName(), false);
    private static void display() {
        JTextField DBuri = new JTextField("127.0.0.1");
        JTextField DBport = new JTextField("3306");
        JTextField DBsettings = new JTextField("autoReconnect=true&useSSL=false");
        JTextField DBuser = new JTextField("user");
        JTextField DBpass = new JTextField("password");
        JTextField DBname = new JTextField("Okapi");
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("MYSQL"));
        panel.add(new JLabel("Configurations"));
        panel.add(new JLabel("URI:"));
        panel.add(DBuri);
        panel.add(new JLabel("Port:"));
        panel.add(DBport);
        panel.add(new JLabel("Schema Name:"));
        panel.add(DBname);
        panel.add(new JLabel("Settings:"));
        panel.add(DBsettings);
        panel.add(new JLabel("User:"));
        panel.add(DBuser);
        panel.add(new JLabel("Password:"));
        panel.add(DBpass);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Test",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Settings.writeXML(DBuri.getText(), DBport.getText(), DBname.getText(), DBsettings.getText(),
            		DBuser.getText(), DBpass.getText());
        } else {
            LOGGER.log(Level.INFO,"Cancelled");
        }
    }

    public static void main(String[] args) {
        display();
    }
    
    public static void LogtoDB() {
		LOGGER = new Logging().create(Settings.class.getName(), true);
	}
}

