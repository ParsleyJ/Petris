package petris.gui;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import parsleyj.utils.GlobalUtils;
import petris.GlobalVarSet;

public class DebugConsole extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JLabel labelFpsW;
	public JLabel labelFpsR;
	public JLabel labelDelay;
	public JLabel labelProfile;
	public JLabel labelPetrisVersion;
	public JLabel labelJavaVersion;
	public JLabel labelHostOs;
	public JLabel labelInGame;
	public JLabel labelPoints;
	public JLabel labelSpeedLevel;
	public JLabel labelSpeed;
	public JLabel labelMultiplier;
	public JLabel labelLinesRemoved;
	public JLabel labelPower;
	public JLabel labelAmmo;
	public JLabel labelMaxAmmo;
	public JLabel labelSquareStyle;
	public JLabel labelClassicShape;
	public JLabel labelCurPieceCoords;
	public JLabel labelSpawnPoint;
	public JLabel labelSquareSize;
	
	private GlobalVarSet globals;
	
	private Timer timer;

	public DebugConsole(GlobalVarSet g) 
	{
		setSize(350,450);
		setResizable(false);
		globals = g;
		setBackground(Color.WHITE);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblFpswanted = new JLabel("FPS(ideal)");
		panel.add(lblFpswanted);
		
		labelFpsW = new JLabel("00");
		panel.add(labelFpsW);
		
		JLabel lblNewLabel = new JLabel("FPS(real)");
		panel.add(lblNewLabel);
		
		labelFpsR = new JLabel("00");
		panel.add(labelFpsR);
		
		JLabel lblNewLabel_1 = new JLabel("Rendering Delay");
		panel.add(lblNewLabel_1);
		
		labelDelay = new JLabel("00");
		panel.add(labelDelay);
		
		JLabel lblCurrentProfile = new JLabel("Current Profile");
		panel.add(lblCurrentProfile);
		
		labelProfile = new JLabel("guest");
		panel.add(labelProfile);
		
		JLabel lblPetrisVersion = new JLabel("Petris Version");
		panel.add(lblPetrisVersion);
		
		labelPetrisVersion = new JLabel("null");
		panel.add(labelPetrisVersion);
		
		JLabel lblJavaVersion = new JLabel("Java Version");
		panel.add(lblJavaVersion);
		
		labelJavaVersion = new JLabel("null");
		panel.add(labelJavaVersion);
		
		JLabel lblOs = new JLabel("OS");
		panel.add(lblOs);
		
		labelHostOs = new JLabel("null");
		panel.add(labelHostOs);
		
		JLabel lblIngame = new JLabel("InGame");
		panel.add(lblIngame);
		
		labelInGame = new JLabel("null");
		panel.add(labelInGame);
		
		JLabel lblPoints = new JLabel("Points");
		panel.add(lblPoints);
		
		labelPoints = new JLabel("00");
		panel.add(labelPoints);
		
		JLabel lblSpeedLevel = new JLabel("Speed Level");
		panel.add(lblSpeedLevel);
		
		labelSpeedLevel = new JLabel("00");
		panel.add(labelSpeedLevel);
		
		JLabel lblSpeed = new JLabel("Speed");
		panel.add(lblSpeed);
		
		labelSpeed = new JLabel("00");
		panel.add(labelSpeed);
		
		JLabel lblMultiplier = new JLabel("Multiplier");
		panel.add(lblMultiplier);
		
		labelMultiplier = new JLabel("00");
		panel.add(labelMultiplier);
		
		JLabel lblLinesRemoved = new JLabel("Lines Removed");
		panel.add(lblLinesRemoved);
		
		labelLinesRemoved = new JLabel("00");
		panel.add(labelLinesRemoved);
		
		JLabel lblCurrentPower = new JLabel("Current Power");
		panel.add(lblCurrentPower);
		
		labelPower = new JLabel("NO POWER");
		panel.add(labelPower);
		
		JLabel lblPowerAmmo = new JLabel("Power Ammo");
		panel.add(lblPowerAmmo);
		
		labelAmmo = new JLabel("00");
		panel.add(labelAmmo);
		
		JLabel lblMaxAmmo = new JLabel("Max Ammo");
		panel.add(lblMaxAmmo);
		
		labelMaxAmmo = new JLabel("00");
		panel.add(labelMaxAmmo);
		
		JLabel lblCurrentSquareStyle = new JLabel("Current Square Style");
		panel.add(lblCurrentSquareStyle);
		
		labelSquareStyle = new JLabel("NoBorder");
		panel.add(labelSquareStyle);
		
		JLabel lblCurpiececlassicshape = new JLabel("CurPiece Classic Shape");
		panel.add(lblCurpiececlassicshape);
		
		labelClassicShape = new JLabel("NoShape");
		panel.add(labelClassicShape);
		
		JLabel lblNewLabel_2 = new JLabel("CurPiece Coords");
		panel.add(lblNewLabel_2);
		
		labelCurPieceCoords = new JLabel("null");
		panel.add(labelCurPieceCoords);
		
		JLabel lblSpawnPointCoords = new JLabel("Spawn Point Coords");
		panel.add(lblSpawnPointCoords);
		
		labelSpawnPoint = new JLabel("null");
		panel.add(labelSpawnPoint);
		
		JLabel lblSquareSize= new JLabel("Square Size");
		panel.add(lblSquareSize);
		
		labelSquareSize = new JLabel("null");
		panel.add(labelSquareSize);
		
		timer = new Timer(1000,this);
	}
	
	public void showDebug()
	{
		timer.start();
		this.setVisible(true);
		labelPetrisVersion.setText(globals.petrisVersion);
		labelJavaVersion.setText(System.getProperty("java.version"));
		labelHostOs.setText(System.getProperty("os.name"));
	}
	
	public void closeDebug()
	{
		timer.stop();
		this.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{		
		labelFpsW.setText("" + globals.currentRender.getIdealFps());
		labelFpsR.setText("" + globals.currentRender.getSampledRealFps());
		labelDelay.setText("" + globals.currentRender.getFrameDelay());
		labelProfile.setText(globals.currentProfile.getName());
		labelInGame.setText(GlobalUtils.boolToString(globals.currentGame.isInGame()));
		labelPoints.setText("" + globals.currentGame.getScore());
		labelSpeedLevel.setText("" + globals.currentGame.getSpeedLevel());
		labelSpeed.setText("" + globals.currentGame.getSpeed());
		labelMultiplier.setText("" + globals.currentGame.getMultiplier());
		labelLinesRemoved.setText("" + globals.currentGame.getTotalRemovedLines());
		labelPower.setText(globals.currentGame.getPowerName());
		labelAmmo.setText("" + globals.currentGame.getPowerAmmo());
		labelMaxAmmo.setText(""+ globals.currentGame.getPowerMaxAmmo());
		labelSquareStyle.setText(globals.currentGame.getCurrentSquareStyle());
		labelClassicShape.setText(globals.currentGame.getCurrentPiece().getShape().toString());
		labelCurPieceCoords.setText("(" + globals.currentGame.getCurrentPiece().x() +
									", " + globals.currentGame.getCurrentPiece().y() + ")" );
		labelSquareSize.setText(""+ globals.currentGame.getTetrisGrid().squareWidth() + " x " + globals.currentGame.getTetrisGrid().squareHeight());
	}

	public void toggleShow() {
		if (this.isVisible()) this.closeDebug();
		else this.showDebug();
		
	}
	
	
	
}
