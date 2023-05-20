package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Keyboard handling for the game along with the mouse setup for game handling.
 * Menus are being moved to edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.gui.gms
 * @author SergeDavid
 * @version 0.1
 */
@SuppressWarnings("unused")
public class InputHandler implements KeyListener,MouseListener,ActionListener {
	
	//Development buttons and the exit game button (escape key)
	private final int dev1 = KeyEvent.VK_NUMPAD1;
	private final int dev2 = KeyEvent.VK_NUMPAD2;
	private final int dev3 = KeyEvent.VK_NUMPAD3;
	private final int dev4 = KeyEvent.VK_NUMPAD4;
	private final int dev5 = KeyEvent.VK_NUMPAD5;
	private final int dev6 = KeyEvent.VK_NUMPAD6;
	private final int dev7 = KeyEvent.VK_NUMPAD7;
	private final int dev8 = KeyEvent.VK_NUMPAD8;
	private final int dev9 = KeyEvent.VK_NUMPAD9;
	private final int exit = KeyEvent.VK_ESCAPE;
	
	//Movement buttons
	private final int up = KeyEvent.VK_UP;
	private final int down = KeyEvent.VK_DOWN;
	private final int left = KeyEvent.VK_LEFT;
	private final int right = KeyEvent.VK_RIGHT;

	//Command buttons
	private final int select = KeyEvent.VK_Z;
	private final int cancel = KeyEvent.VK_X;
	private final int start = KeyEvent.VK_ENTER;
	
	//Mouse (right/left clicks)
	private final int main = MouseEvent.BUTTON1;
	private final int alt = MouseEvent.BUTTON1;

	private edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game game;

	public InputHandler(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game game) {
		this.game = game;
		edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.gui.addKeyListener(this);
		edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.gui.addMouseListener(this);
	}

	int DevPathing = 1;
	public void keyPressed(KeyEvent e) {
		if(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.currentplayer != edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getPlayerId())
			// aceitar apenas teclas se for a vez do jogador
			return;
		int i=e.getKeyCode();
		if (i==exit) {System.exit(0);}
		if (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.GameState== edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.State.PLAYING) {
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players.Base ply = edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.player.get(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.currentplayer);
			//SubjectRI subject = edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getSubjectRI();
			try {
				if (i == up) {
					Game.game.getObserver().sendMessage(Game.game.getPlayerId() +":down");
				} else if (i == down) {
					//subject.setState(new State(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getGameId(), "down"));
				} else if (i == left) {
					//subject.setState(new State(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getGameId(), "left"));
				} else if (i == right) {
					//subject.setState(new State(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getGameId(), "right"));
				} else if (i == select) {
					//subject.setState(new State(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getGameId(), "select"));
				} else if (i == cancel) {
					//subject.setState(new State(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getGameId(), "cancel"));
				} else if (i == start) {
					//subject.setState(new State(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.getGameId(), "start"));
				}
			} catch(IOException ex) {System.out.println(ex);}
		}
		if (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.GameState== edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.State.EDITOR) {
			if (i==up) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selecty--;if (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selecty<0) {
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selecty++;} edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.moved = true;}
			else if (i==down) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selecty++;if (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selecty>= edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.map.height) {
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selecty--;} edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.moved = true;}
			else if (i==left) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selectx--;if (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selectx<0) {
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selectx++;} edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.moved = true;}
			else if (i==right) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selectx++;if (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selectx>= edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.map.width) {
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.selectx--;} edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.moved = true;}
			else if (i==select) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.holding = true;}
			else if (i==cancel) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.ButtButton();}
			else if (i==start) {
				new edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.menus.EditorMenu();
			}
		}
		
		if (i==dev1) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.gui.LoginScreen();}
		else if (i==dev2) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.load.LoadTexturePack("Test");}
		else if (i==dev3) {
			DevPathing++;
			switch (DevPathing) {
				case 1:
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.pathing.ShowCost=false;break;
				case 2:
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.pathing.ShowHits=true;break;
				case 3:
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.pathing.ShowHits=false;
                    edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.pathing.ShowCost=true;DevPathing=0;break;
			}
		}
		else if (i==dev4) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.EndTurn();}
		else if (i==dev5) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.player.get(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.currentplayer).npc = !edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.player.get(edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.currentplayer).npc; edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.EndTurn();}
		else if (i==dev6) {new edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.menus.StartMenu(game);}
	}
	public void keyReleased(KeyEvent e) {
		int i=e.getKeyCode();
		if (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.GameState== edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.State.EDITOR) {
			if (i==select) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.edit.holding = false;}
		}
	}
	public void keyTyped(KeyEvent arg0) {}
	public void mousePressed() {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		Game.gui.requestFocusInWindow();
		Object s = e.getSource();
	}
}
