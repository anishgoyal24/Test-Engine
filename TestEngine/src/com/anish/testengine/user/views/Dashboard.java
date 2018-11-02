package com.anish.testengine.user.views;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.anish.testengine.user.dto.RightDTO;
import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ICommonConstants;
import com.anish.testengine.utils.ICommonUtils;
import org.apache.log4j.Logger;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.MenuItem;
import java.awt.Window.Type;

public class Dashboard extends JFrame {

	private JPanel contentPane;
	private Logger logger = Logger.getLogger(Dashboard.class);

	public Dashboard(UserDTO userDTO) {
		setResizable(false);
	    logger.debug("Dashboard design started");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu file = new JMenu(userDTO.getRole());
		menuBar.add(file);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		logger.debug("Creating menu");
		for (RightDTO rightDTO : userDTO.getRights()) {
			JMenuItem menuItem = new JMenuItem(rightDTO.getRightName());
			menuItem.addActionListener((arg0)->{
				String screenName = rightDTO.getScreen();
				try {
					Object object = Class.forName(ICommonConstants.BASE_PATH + screenName).getDeclaredConstructor(UserDTO.class).newInstance(userDTO);
					Method method = object.getClass().getMethod("setVisible", boolean.class);
					method.invoke(object, true);
					this.dispose();
				} catch (InstantiationException e) {
                    logger.error(ICommonUtils.getStackTraceString(e));
                    JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalAccessException e) {
                    logger.error(ICommonUtils.getStackTraceString(e));
                    JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException e) {
                    logger.error(ICommonUtils.getStackTraceString(e));
                    JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (NoSuchMethodException e) {
                    logger.error(ICommonUtils.getStackTraceString(e));
                    JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (SecurityException e) {
                    logger.error(ICommonUtils.getStackTraceString(e));
                    JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (IllegalArgumentException e) {
                    logger.error(ICommonUtils.getStackTraceString(e));
                    JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (InvocationTargetException e) {
                    logger.error(ICommonUtils.getStackTraceString(e));
                    JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				}
		});
			file.add(menuItem);
		}
		JMenuItem menuItem = new JMenuItem("Logout");
		menuItem.addActionListener((argo)->{
			LoginView loginView = new LoginView();
			loginView.frame.setVisible(true);
			this.dispose();
		});
		file.add(menuItem);
		logger.debug("Dashboard design ended");
	}
}
